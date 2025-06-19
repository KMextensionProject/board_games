package sk.mkrajcovic.bgs.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearRangeValidator implements ConstraintValidator<Year, Integer> {

	private int fromYear;
	private int toYear;

	@Override
	public void initialize(Year constraintAnnotation) {
		fromYear = constraintAnnotation.from();
		toYear = constraintAnnotation.to();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		int maxYear = toYear;
		if (Year.CURRENT_YEAR == toYear) {
			maxYear = java.time.Year.now().getValue();
		}
		return value >= fromYear && value <= maxYear;
	}
}
