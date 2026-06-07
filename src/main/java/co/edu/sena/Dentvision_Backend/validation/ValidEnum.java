package co.edu.sena.Dentvision_Backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Valida que un String sea uno de los valores permitidos (case-insensitive).
 *
 * Uso en DTO:
 * <pre>
 * @ValidEnum(allowed = {"PENDIENTE","CONFIRMADA","CANCELADA"},
 *            message = "El estado debe ser PENDIENTE, CONFIRMADA o CANCELADA")
 * String estado;
 * </pre>
 */
@Documented
@Constraint(validatedBy = ValidEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String[]  allowed();
    String    message()  default "Valor no permitido para este campo";
    Class<?>[] groups()  default {};
    Class<? extends Payload>[] payload() default {};
}
