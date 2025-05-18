package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Boolean validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.isTrue(value: Boolean?, message: String = "Value must be true") {
    rule<T> {
        name("True validation")
        condition { value == true }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isFalse(value: Boolean?, message: String = "Value must be false") {
    rule<T> {
        name("False validation")
        condition { value == false }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isNotNull(value: Boolean?, message: String = "Value must not be null") {
    rule<T> {
        name("Not null validation")
        condition { value != null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isNull(value: Boolean?, message: String = "Value must be null") {
    rule<T> {
        name("Null validation")
        condition { value == null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.equals(value: Boolean?, expected: Boolean, message: String = "Value must be equal to $expected") {
    rule<T> {
        name("Equals validation")
        condition { value == expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notEquals(value: Boolean?, expected: Boolean, message: String = "Value must not be equal to $expected") {
    rule<T> {
        name("Not equals validation")
        condition { value != expected }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.and(value: Boolean?, other: Boolean, message: String = "Value must be true when combined with $other using AND") {
    rule<T> {
        name("AND validation")
        condition { value == true && other }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.or(value: Boolean?, other: Boolean, message: String = "Value must be true when combined with $other using OR") {
    rule<T> {
        name("OR validation")
        condition { value == true || other }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.xor(value: Boolean?, other: Boolean, message: String = "Value must be true when combined with $other using XOR") {
    rule<T> {
        name("XOR validation")
        condition { value == true != other }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.not(value: Boolean?, message: String = "Value must be false") {
    rule<T> {
        name("NOT validation")
        condition { value == false }
        message(message)
    }
} 