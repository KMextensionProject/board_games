package sk.mkrajcovic.bgs.utils;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jett.transform.ExcelTransformer;
import sk.mkrajcovic.bgs.InfrastructureException;

/**
 * Utility class for working with Excel files (XLSX format).<br>
 * This class provides methods for generating XLSX files mostly based on
 * templates.
 */
public class XlsxUtils {

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+?)}");

	private XlsxUtils() {
		throw new UnsupportedOperationException("XlsxUtils was not designed to be instantiated");
	}

	/**
	 * Sets the HTTP response headers to indicate that the response contains an XLSX file.
	 * <p>
	 * This method configures the HTTP headers to:
	 * <ul>
	 * <li>Specify that the content type is an Excel spreadsheet in the XLSX
	 * format.</li>
	 * <li>Set the Content-Disposition header to prompt the browser to download the
	 * file with the specified name.</li>
	 * </ul>
	 * The filename is Base64-encoded and included in the Content-Disposition header
	 * to support special characters and ensure correct display in the browser's
	 * download prompt.
	 *
	 * @param response   the {@link HttpServletResponse} object used to set the
	 *                   response headers. Must not be null.
	 * @param targetName the name of the file to be downloaded, which will be used
	 *                   in the Content-Disposition header. This value will be
	 *                   Base64-encoded to handle special characters properly.
	 */
	public static void setXlsxResponseHeaders(HttpServletResponse response, String targetName) {
		response.addHeader("Content-type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		String encoded = Base64.getEncoder().encodeToString(targetName.getBytes(StandardCharsets.UTF_8));
		String contentDisposition = "attachment; filename=\"=?UTF-8?B?" + encoded + "?=\"";
		response.addHeader("Content-disposition", contentDisposition);
	}

	/**
	 * Generates a template-based XLSX file using {@code data} where keys are binded
	 * to template column variables.
	 * <p>
	 * This method adds proper HTTP response headers for XLSX file to
	 * {@link HttpServletResponse} argument and directly writes the file contents
	 * into it.
	 *
	 * @param template   - path to XLSX template
	 * @param data       to be bind to template variables and written
	 * @param targetName - the custom name of the downloaded file
	 * @throws IOException if reading template or writing data to XLSX or HTTP
	 *                     response fails
	 */
	public static void generateXlsx(String template, List<TypeMap> data, String targetName, HttpServletResponse response) throws IOException {
		setXlsxResponseHeaders(response, targetName);
		generateXlsx(template, data, response.getOutputStream());
	}

	public static void generateXlsx(String template, Stream<TypeMap> data, String targetName, HttpServletResponse response) throws IOException {
		setXlsxResponseHeaders(response, targetName);
		generateXlsx(template, data, response.getOutputStream());
	}

	/**
	 * Generates a template-based XLSX file using {@code data} where keys are
	 * binded to template column variables.
	 *
	 * @param template     - path to XLSX template
	 * @param data         to be bind to template variables and written
	 * @param outputStream for resulting the XLSX file
	 * @throws IOException if reading template or writing data to XLSX fails
	 */
	public static void generateXlsx(String template, List<TypeMap> data, OutputStream outputStream) throws IOException {
		try (var inputStream = ResourceLoader.loadResource(template).getInputStream()) {
			ExcelTransformer transformer = new ExcelTransformer();
			Workbook workbook = transformer.transform(inputStream, new TypeMap("table", data));
			workbook.write(outputStream);
		} catch (InvalidFormatException ife) {
			throw new InfrastructureException("Unable to create Workbook object", ife);
		}
	}

	/**
	 * Generates a template-based XLSX file by applying the streamed data row by row,
	 * dynamically mapping placeholders. This avoids collecting the entire stream 
	 * into memory and dynamically maps data to placeholders found in the template.
	 */
	public static void generateXlsx(String template, Stream<TypeMap> data, OutputStream outputStream) {
		try (var inputStream = ResourceLoader.loadResource(template).getInputStream();
			 var workbook = transform(inputStream, data)) {
			workbook.write(outputStream);
		} catch (IOException ex) {
			throw new InfrastructureException("Unable to create Workbook object", ex);
		}
	}

    /**
     * Transforms an Excel template by applying the streamed data row by row, dynamically mapping placeholders.
     * This avoids collecting the entire stream into memory and dynamically maps data to placeholders found in the template.
     *
     * @param xlsxTemplate InputStream of the Excel template (XLSX format).
     * @param dataStream Stream of maps containing data to be applied to the template.
     * @return The transformed Workbook.
     * @throws IOException if an error occurs during transformation or writing to the workbook.
     */
	private static Workbook transform(InputStream xlsxTemplate, Stream<TypeMap> dataStream) throws IOException {
		Workbook workbook = new XSSFWorkbook(xlsxTemplate);
		Sheet sheet = workbook.getSheetAt(0);
		List<String> placeholders = findPlaceholdersInSheet(sheet);

		// using an array to hold the mutable row index - placeholder row
		int[] currentRowIndex = { sheet.getLastRowNum() };

		// we do not create cells for writing nulls, so we must get rid of all the
		// placeholder values; plus cell's default value cannot be guessed here
		sheet.getRow(currentRowIndex[0]).forEach(cell -> cell.setCellType(CELL_TYPE_BLANK));

		dataStream.forEach(data -> {
			try {
				applyDataToRow(sheet, currentRowIndex[0]++, placeholders, data);
			} catch (Exception e) {
				throw new RuntimeException("Error applying transformation for streamed data", e);
			}
		});

		return workbook;
	}

	private static List<String> findPlaceholdersInSheet(Sheet sheet) {
		for (Row row : sheet) {
			// check if the first column contains a placeholder
			Cell firstCell = row.getCell(0);
			if (firstCell != null) {
				String firstCellValue = getCellValueAsString(firstCell);
				Matcher firstCellMatcher = PLACEHOLDER_PATTERN.matcher(firstCellValue);

				// if the first cell doesn't contain a placeholder, skip the row
				if (!firstCellMatcher.find()) {
					continue;
				}
			} else {
				// skip the row if the first cell is null
				continue;
			}

			// if the first column contains a placeholder, proceed with the rest of the row
			List<String> placeholders = new ArrayList<>();
			for (Cell cell : row) {
				String cellValue = getCellValueAsString(cell);
				Matcher matcher = PLACEHOLDER_PATTERN.matcher(cellValue);
				if (matcher.find()) {
					// extract all placeholders in the cell and add them to the list
					// should we allow multiple placeholders in a single cell ?
					// generator does not work in this way yet
					do {
						placeholders.add(matcher.group(1));
					} while (matcher.find());
				}
			}

			if (!placeholders.isEmpty()) {
				return placeholders;
			}
		}
		throw new IllegalStateException("No placeholders found in the template.");
	}

	private static String getCellValueAsString(Cell cell) {
		return switch (cell.getCellType()) {
			case CELL_TYPE_STRING -> cell.getStringCellValue();
			case CELL_TYPE_NUMERIC -> Double.toString(cell.getNumericCellValue());
			case CELL_TYPE_BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
			case CELL_TYPE_FORMULA -> cell.getCellFormula();
			default -> "";
		};
	}

	private static void applyDataToRow(Sheet sheet, int rowIndex, List<String> placeholders, TypeMap data) {
		Row row = getOrCreateRow(sheet, rowIndex);
		for (int cellIndex = 0; cellIndex < placeholders.size(); ++cellIndex) {
			String placeholder = placeholders.get(cellIndex);
			Object value = data.get(placeholder);
			if (value != null) {
				Cell cell = getOrCreateCell(row, cellIndex);
				setCellValue(cell, value);
			}
		}
	}

	private static Row getOrCreateRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			return sheet.createRow(rowIndex);
		}
		return row;
	}

	private static Cell getOrCreateCell(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return row.createCell(cellIndex);
		}
		return cell;
	}

	private static void setCellValue(Cell cell, Object value) {
		switch (value) {
			case String str -> cell.setCellValue(str);
			case Integer i -> cell.setCellValue(i);
			case Long l -> cell.setCellValue(l);
			case Double d -> cell.setCellValue(d);
			case Boolean b -> cell.setCellValue(b);
			case LocalDateTime ldt -> cell.setCellValue(Date.from(ldt.toInstant(ZoneOffset.UTC)));
			case LocalDate ld -> cell.setCellValue(Date.from(ld.atStartOfDay(ZoneOffset.UTC).toInstant()));
			default -> cell.setCellValue(value.toString());
		}
	}
}
