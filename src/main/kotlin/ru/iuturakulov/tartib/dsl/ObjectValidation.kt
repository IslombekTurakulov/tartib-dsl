package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Object validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.isNull(value: Any?, message: String = "Value must be null") {
    rule<T> {
        name("Null validation")
        condition { value == null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isNotNull(value: Any?, message: String = "Value must not be null") {
    rule<T> {
        name("Not null validation")
        condition { value != null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.equals(value: Any?, expected: Any?, message: String = "Value must be equal to $expected") {
    rule<T> {
        name("Equals validation")
        condition { value == expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notEquals(value: Any?, expected: Any?, message: String = "Value must not be equal to $expected") {
    rule<T> {
        name("Not equals validation")
        condition { value != expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.same(value: Any?, expected: Any?, message: String = "Value must be the same instance as $expected") {
    rule<T> {
        name("Same validation")
        condition { value === expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notSame(value: Any?, expected: Any?, message: String = "Value must not be the same instance as $expected") {
    rule<T> {
        name("Not same validation")
        condition { value !== expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.instanceOf(value: Any?, type: Class<*>, message: String = "Value must be an instance of ${type.name}") {
    rule<T> {
        name("Instance of validation")
        condition { value != null && type.isInstance(value) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notInstanceOf(value: Any?, type: Class<*>, message: String = "Value must not be an instance of ${type.name}") {
    rule<T> {
        name("Not instance of validation")
        condition { value == null || !type.isInstance(value) }
        message(message)
    }
}

@ValidationDsl
fun <T, N> ValidationScope<T>.nested(value: N?, init: ValidationScope<N>.() -> Unit) {
    rule<T> {
        name("Nested validation")
        condition {
            value != null && ValidationScope<N>().apply(init).build().evaluate(value).all { it.isSuccess }
        }
        message("Nested validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.validateWhen(condition: (T) -> Boolean, init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("Conditional validation")
        condition { context ->
            if (condition(context)) {
                ValidationScope<T>().apply(init).build().evaluate(context).all { it.isSuccess }
            } else {
                true
            }
        }
        message("Conditional validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.validateOptional(init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("Optional validation")
        condition { context ->
            if (context != null) {
                ValidationScope<T>().apply(init).build().evaluate(context).all { it.isSuccess }
            } else {
                true
            }
        }
        message("Optional validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.required(init: ValidationScope<T>.() -> Unit) {
    rule<T> {
        name("Required validation")
        condition { context ->
            ValidationScope<T>().apply(init).build().evaluate(context).all { it.isSuccess }
        }
        message("Required validation failed")
    }
}
