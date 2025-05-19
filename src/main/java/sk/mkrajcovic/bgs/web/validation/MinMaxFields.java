package sk.mkrajcovic.bgs.web.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MinMaxFieldsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinMaxFields {

	String message() default "Invalid field relation: expected {minField} <= {maxField}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * The name of the field representing the minimum value.
	 */
	String minField();

	/**
	 * The name of the field representing the maximum value.
	 */
	String maxField();

	/**
	 * Whether the comparison should be strict (i.e.,
	 * {@code firstField < secondField}). If false, allows equality (i.e.,
	 * {@code firstField <= secondField}).
	 */
	boolean strict() default false;
}
