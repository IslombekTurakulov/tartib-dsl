package ru.iuturakulov.tartib.dsl

/**
 * String validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.length(value: String?, min: Int, max: Int, message: String = "Length must be between $min and $max") {
    rule<T> {
        name("Length validation")
        condition { value != null && value.length in min..max }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.minLength(value: String?, min: Int, message: String = "Length must be at least $min") {
    rule<T> {
        name("Min length validation")
        condition { value != null && value.length >= min }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.maxLength(value: String?, max: Int, message: String = "Length must be at most $max") {
    rule<T> {
        name("Max length validation")
        condition { value != null && value.length <= max }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.matches(value: String?, pattern: String, message: String = "Value does not match required pattern") {
    rule<T> {
        name("Pattern validation")
        condition { value != null && value.matches(Regex(pattern)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.startsWith(value: String?, prefix: String, message: String = "Value must start with $prefix") {
    rule<T> {
        name("Starts with validation")
        condition { value != null && value.startsWith(prefix) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.endsWith(value: String?, suffix: String, message: String = "Value must end with $suffix") {
    rule<T> {
        name("Ends with validation")
        condition { value != null && value.endsWith(suffix) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.contains(value: String?, substring: String, message: String = "Value must contain $substring") {
    rule<T> {
        name("Contains validation")
        condition { value != null && value.contains(substring) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notContains(value: String?, substring: String, message: String = "Value must not contain $substring") {
    rule<T> {
        name("Not contains validation")
        condition { value != null && !value.contains(substring) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.alpha(value: String?, message: String = "Value must contain only letters") {
    rule<T> {
        name("Alpha validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.ALPHA_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.alphaNumeric(value: String?, message: String = "Value must contain only letters and numbers") {
    rule<T> {
        name("Alpha numeric validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.ALPHA_NUMERIC_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.numeric(value: String?, message: String = "Value must contain only numbers") {
    rule<T> {
        name("Numeric validation")
        condition { value != null && value.matches(Regex(ValidationPatterns.NUMERIC_PATTERN)) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.lowerCase(value: String?, message: String = "Value must be in lower case") {
    rule<T> {
        name("Lower case validation")
        condition { value != null && value == value.lowercase() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.upperCase(value: String?, message: String = "Value must be in upper case") {
    rule<T> {
        name("Upper case validation")
        condition { value != null && value == value.uppercase() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.trimmed(value: String?, message: String = "Value must not contain leading or trailing whitespace") {
    rule<T> {
        name("Trimmed validation")
        condition { value != null && value == value.trim() }
        message(message)
    }
} 