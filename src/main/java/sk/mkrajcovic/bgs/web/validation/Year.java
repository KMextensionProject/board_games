package sk.mkrajcovic.bgs.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = YearRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Year {

	public static final int CURRENT_YEAR = -1;

	String message() default "Selected year value does not meet the min/max constraint";

	int from() default 0;

	int to() default CURRENT_YEAR;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
