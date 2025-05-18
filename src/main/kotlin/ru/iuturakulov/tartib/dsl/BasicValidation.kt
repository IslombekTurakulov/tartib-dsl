package ru.iuturakulov.tartib.dsl

import java.util.regex.Pattern

/**
 * Basic validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.required(value: Any?, message: String = "Field is required") {
    rule<T> {
        name("Required validation")
        condition { value != null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notBlank(value: String?, message: String = "Field cannot be blank") {
    rule<T> {
        name("Not blank validation")
        condition { value?.isNotBlank() == true }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isEmail(value: String?, message: String = "Invalid email format") {
    rule<T> {
        name("Email validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.EMAIL_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.isPhone(value: String?, message: String = "Invalid phone format") {
    rule<T> {
        name("Phone validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.PHONE_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.url(value: String?, message: String = "Invalid URL format") {
    rule<T> {
        name("URL validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.URL_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.password(value: String?, message: String = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character") {
    rule<T> {
        name("Password validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.PASSWORD_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.range(min: Number, max: Number, value: Number?, message: String = "Value must be between $min and $max") {
    rule<T> {
        name("Range validation")
        condition { value != null && value.toDouble() in min.toDouble()..max.toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.min(min: Number, value: Number?, message: String = "Value must be greater than or equal to $min") {
    rule<T> {
        name("Min validation")
        condition { value != null && value.toDouble() >= min.toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.max(max: Number, value: Number?, message: String = "Value must be less than or equal to $max") {
    rule<T> {
        name("Max validation")
        condition { value != null && value.toDouble() <= max.toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.size(min: Int, max: Int, value: Collection<*>?, message: String = "Size must be between $min and $max") {
    rule<T> {
        name("Size validation")
        condition { value != null && value.size in min..max }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.unique(value: Collection<*>?, message: String = "Collection must contain unique elements") {
    rule<T> {
        name("Unique validation")
        condition { value != null && value.size == value.toSet().size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notEmpty(value: Collection<*>?, message: String = "Collection cannot be empty") {
    rule<T> {
        name("Not empty validation")
        condition { value != null && value.isNotEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.custom(condition: () -> Boolean, message: String) {
    rule<T> {
        name("Custom validation")
        condition { condition() }
        message(message)
    }
}
