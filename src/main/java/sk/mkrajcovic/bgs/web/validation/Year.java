package sk.mkrajcovic.bgs.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Year {

	String message() default "Year must be between 1970 and the current year (inclusive)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
