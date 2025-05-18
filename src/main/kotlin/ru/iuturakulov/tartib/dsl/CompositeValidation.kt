package ru.iuturakulov.tartib.dsl

/**
 * Composite validation rules
 */
fun <T> ValidationRule<T>.and(init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("AND validation")
        condition { context ->
            context.isValid(init)
        }
        message("AND validation failed")
    }
}

fun <T> ValidationRule<T>.or(init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("OR validation")
        condition { context ->
            context.isValid(init)
        }
        message("OR validation failed")
    }
}

fun <T> ValidationRule<T>.not(init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("NOT validation")
        condition { context ->
            !context.isValid(init)
        }
        message("NOT validation failed")
    }
} 