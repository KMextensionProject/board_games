package sk.mkrajcovic.bgs.web.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] buffer;
	private final ByteArrayInputStream byteArrayInputStream;

	public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		buffer = StreamUtils.copyToByteArray(request.getInputStream());
		this.byteArrayInputStream = new ByteArrayInputStream(buffer);
	}

	@Override
	public ServletInputStream getInputStream() {
		return new ServletInputStream() {
			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return byteArrayInputStream.available() == 0;
			}

			@Override
			public void setReadListener(ReadListener listener) {
			}
		};
	}

	public String getRequestBody() {
		return getRequestBody(Charset.defaultCharset());
	}

	public String getRequestBody(Charset charset) {
		return new String(buffer, charset);
	}

}
