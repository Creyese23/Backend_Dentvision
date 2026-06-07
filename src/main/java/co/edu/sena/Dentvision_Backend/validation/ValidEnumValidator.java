package co.edu.sena.Dentvision_Backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementacion de la validacion @ValidEnum.
 * Acepta null (usa @NotNull por separado si es obligatorio).
 */
public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> allowed;

    @Override
    public void initialize(ValidEnum annotation) {
        this.allowed = Arrays.stream(annotation.allowed())
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) return true;   // null se valida con @NotNull por separado
        return allowed.contains(value.toUpperCase());
    }
}
