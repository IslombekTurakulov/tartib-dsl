package ru.iuturakulov.tartib.dsl

/**
 * Предопределенные паттерны для валидации
 */
object ValidationPatterns {
    // Email validation
    const val EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    
    // Phone number validation (international format)
    const val PHONE_PATTERN = "^\\+?[1-9]\\d{1,14}$"
    
    // URL validation
    const val URL_PATTERN = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$"
    
    // Date validation (YYYY-MM-DD)
    const val DATE_PATTERN = "^\\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])$"
    
    // Time validation (HH:mm:ss)
    const val TIME_PATTERN = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$"
    
    // DateTime validation (YYYY-MM-DD HH:mm:ss)
    const val DATETIME_PATTERN = "^\\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01]) (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$"
    
    // IPv4 address validation
    const val IPV4_PATTERN = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$"
    
    // IPv6 address validation
    const val IPV6_PATTERN = "^(?:[A-F0-9]{1,4}:){7}[A-F0-9]{1,4}$"
    
    // Credit card number validation
    const val CREDIT_CARD_PATTERN = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9]{2})[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$"
    
    // Password validation (at least 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character)
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,}$"
    
    // Username validation (3-20 characters, letters, numbers, underscores)
    const val USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$"
    
    // Hex color code validation
    const val HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
    
    // ISBN validation
    const val ISBN_PATTERN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ])?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    
    // MAC address validation
    const val MAC_ADDRESS_PATTERN = "^(?:[0-9A-Fa-f]{2}[:-]){5}(?:[0-9A-Fa-f]{2})$"
    
    // UUID validation
    const val UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
    
    // Alpha (letters only)
    const val ALPHA_PATTERN = "^[a-zA-Z]*$"

    // Alpha-numeric (letters and numbers only)
    const val ALPHA_NUMERIC_PATTERN = "^[a-zA-Z0-9]*$"

    // Numeric (numbers only)
    const val NUMERIC_PATTERN = "^[0-9]*$"
}

/**
 * Расширения для ValidationScope для использования предопределенных паттернов
 */
fun <T> ValidationScope<T>.matchesPattern(
    pattern: String,
    property: (T) -> String,
    message: String = "Invalid format"
) {
    matches(pattern, property, message)
}

fun <T> ValidationScope<T>.isEmail(property: (T) -> String, message: String = "Invalid email format") {
    matchesPattern(ValidationPatterns.EMAIL_PATTERN, property, message)
}

fun <T> ValidationScope<T>.isPhone(property: (T) -> String, message: String = "Invalid phone format") {
    matchesPattern(ValidationPatterns.PHONE_PATTERN, property, message)
}

fun <T> ValidationScope<T>.url(property: (T) -> String, message: String = "Invalid URL format") {
    matchesPattern(ValidationPatterns.URL_PATTERN, property, message)
}

fun <T> ValidationScope<T>.date(property: (T) -> String, message: String = "Invalid date format") {
    matchesPattern(ValidationPatterns.DATE_PATTERN, property, message)
}

fun <T> ValidationScope<T>.time(property: (T) -> String, message: String = "Invalid time format") {
    matchesPattern(ValidationPatterns.TIME_PATTERN, property, message)
}

fun <T> ValidationScope<T>.datetime(property: (T) -> String, message: String = "Invalid datetime format") {
    matchesPattern(ValidationPatterns.DATETIME_PATTERN, property, message)
}

fun <T> ValidationScope<T>.ipv4(property: (T) -> String, message: String = "Invalid IPv4 address") {
    matchesPattern(ValidationPatterns.IPV4_PATTERN, property, message)
}

fun <T> ValidationScope<T>.ipv6(property: (T) -> String, message: String = "Invalid IPv6 address") {
    matchesPattern(ValidationPatterns.IPV6_PATTERN, property, message)
}

fun <T> ValidationScope<T>.creditCard(property: (T) -> String, message: String = "Invalid credit card number") {
    matchesPattern(ValidationPatterns.CREDIT_CARD_PATTERN, property, message)
}

fun <T> ValidationScope<T>.password(property: (T) -> String, message: String = "Invalid password format") {
    matchesPattern(ValidationPatterns.PASSWORD_PATTERN, property, message)
}

fun <T> ValidationScope<T>.username(property: (T) -> String, message: String = "Invalid username format") {
    matchesPattern(ValidationPatterns.USERNAME_PATTERN, property, message)
}

fun <T> ValidationScope<T>.hexColor(property: (T) -> String, message: String = "Invalid hex color code") {
    matchesPattern(ValidationPatterns.HEX_COLOR_PATTERN, property, message)
}

fun <T> ValidationScope<T>.isbn(property: (T) -> String, message: String = "Invalid ISBN") {
    matchesPattern(ValidationPatterns.ISBN_PATTERN, property, message)
}

fun <T> ValidationScope<T>.macAddress(property: (T) -> String, message: String = "Invalid MAC address") {
    matchesPattern(ValidationPatterns.MAC_ADDRESS_PATTERN, property, message)
}

fun <T> ValidationScope<T>.uuid(property: (T) -> String, message: String = "Invalid UUID") {
    matchesPattern(ValidationPatterns.UUID_PATTERN, property, message)
} 