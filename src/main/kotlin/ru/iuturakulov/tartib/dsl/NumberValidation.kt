package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Number validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.positive(value: Number?, message: String = "Value must be positive") {
    rule<T> {
        name("Positive validation")
        condition { value != null && value.toDouble() > 0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.negative(value: Number?, message: String = "Value must be negative") {
    rule<T> {
        name("Negative validation")
        condition { value != null && value.toDouble() < 0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.nonNegative(value: Number?, message: String = "Value must be non-negative") {
    rule<T> {
        name("Non-negative validation")
        condition { value != null && value.toDouble() >= 0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.nonPositive(value: Number?, message: String = "Value must be non-positive") {
    rule<T> {
        name("Non-positive validation")
        condition { value != null && value.toDouble() <= 0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.even(value: Number?, message: String = "Value must be even") {
    rule<T> {
        name("Even validation")
        condition { value != null && value.toLong() % 2 == 0L }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.odd(value: Number?, message: String = "Value must be odd") {
    rule<T> {
        name("Odd validation")
        condition { value != null && value.toLong() % 2 != 0L }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.divisibleBy(value: Number?, divisor: Number, message: String = "Value must be divisible by $divisor") {
    rule<T> {
        name("Divisible by validation")
        condition { value != null && value.toDouble() % divisor.toDouble() == 0.0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notDivisibleBy(value: Number?, divisor: Number, message: String = "Value must not be divisible by $divisor") {
    rule<T> {
        name("Not divisible by validation")
        condition { value != null && value.toDouble() % divisor.toDouble() != 0.0 }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isInteger(value: Number?, message: String = "Value must be an integer") {
    rule<T> {
        name("Integer validation")
        condition { value != null && value.toDouble() == value.toLong().toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isDecimal(value: Number?, message: String = "Value must be a decimal") {
    rule<T> {
        name("Decimal validation")
        condition { value != null && value.toDouble() != value.toLong().toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isFinite(value: Number?, message: String = "Value must be finite") {
    rule<T> {
        name("Finite validation")
        condition { value != null && value.toDouble().isFinite() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isInfinite(value: Number?, message: String = "Value must be infinite") {
    rule<T> {
        name("Infinite validation")
        condition { value != null && value.toDouble().isInfinite() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isNaN(value: Number?, message: String = "Value must be NaN") {
    rule<T> {
        name("NaN validation")
        condition { value != null && value.toDouble().isNaN() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isNotNaN(value: Number?, message: String = "Value must not be NaN") {
    rule<T> {
        name("Not NaN validation")
        condition { value != null && !value.toDouble().isNaN() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isBigInteger(value: Number?, message: String = "Value must be a BigInteger") {
    rule<T> {
        name("BigInteger validation")
        condition { value != null && value is BigInteger }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isBigDecimal(value: Number?, message: String = "Value must be a BigDecimal") {
    rule<T> {
        name("BigDecimal validation")
        condition { value != null && value is BigDecimal }
        message(message)
    }
} 