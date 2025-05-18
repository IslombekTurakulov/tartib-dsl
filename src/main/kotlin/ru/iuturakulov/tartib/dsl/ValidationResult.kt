package ru.iuturakulov.tartib.dsl

/**
 * Sealed class representing the result of a validation rule
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
} 