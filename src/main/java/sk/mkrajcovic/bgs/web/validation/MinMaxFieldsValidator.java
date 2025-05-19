package sk.mkrajcovic.bgs.web.validation;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sk.mkrajcovic.bgs.InfrastructureException;
import sk.mkrajcovic.bgs.web.MessageCodeConstants;

/**
 * Validator for the {@link MinMaxFields} annotation.
 * <p>
 * This validator checks whether the value of the first field (typically a
 * "min") is less than or equal to the value of the second field (typically a
 * "max"), depending on whether the {@code strict} flag is enabled.
 * <p>
 * It supports any class-level target with two {@link Integer} fields.
 */
public class MinMaxFieldsValidator implements ConstraintValidator<MinMaxFields, Object> {

	private String firstField;
	private String secondField;
	private boolean strict;

	@Override
	public void initialize(MinMaxFields constraintAnnotation) {
		firstField = constraintAnnotation.minField();
		secondField = constraintAnnotation.maxField();
		strict = constraintAnnotation.strict();
	}

	// class-level constraints go into BindingResult.getGlobalErrors()
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			Field first = value.getClass().getDeclaredField(firstField);
			Field second = value.getClass().getDeclaredField(secondField);

			first.setAccessible(true);
			second.setAccessible(true);

			Integer firstValue = (Integer) first.get(value);
			Integer secondValue = (Integer) second.get(value);

			if (firstValue == null || secondValue == null) {
				return true;
			}

			boolean valid = strict ? firstValue < secondValue : firstValue <= secondValue;
			if (!valid) {
				// when we're using a class-level constraint like @MinMaxFields, we must manually
				// attach a constraint violation to a specific property or to the object itself;
				// otherwise Spring won't know what to do with it, even if isValid(...) returns false
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
					  // attach violation to one of the fields, or to the class
					  .addPropertyNode(firstField).addConstraintViolation();
			}

			return valid;

		} catch (NoSuchFieldException | IllegalAccessException error) {
			// programmer error; can happen when the property is wrongly specified
			throw new InfrastructureException(
				MessageCodeConstants.ERROR,
				"Error accessing fields in MinMaxFieldsValidator",
				error
			);
		}
	}
}
