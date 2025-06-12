package sk.mkrajcovic.bgs.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearRangeValidator implements ConstraintValidator<Year, Integer> {

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		int currentYear = java.time.Year.now().getValue();
		return value >= 1970 && value <= currentYear;
	}
}
