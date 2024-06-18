package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Target(ElementType.FIELD)
@Past
public @interface MinimumDate {
    String message() default "Дата должна быть не ранее {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
