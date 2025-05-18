package ru.iuturakulov.tartib.dsl

import java.util.concurrent.ConcurrentHashMap

/**
 * Validation context that can be used to pass additional data to validation rules
 */
class ValidationContext(
    private val data: MutableMap<String, Any> = mutableMapOf()
) {
    /**
     * Get value from context
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T? = data[key] as? T

    /**
     * Set value in context
     */
    fun <T> set(key: String, value: T) {
        data[key] = value as Any
    }

    /**
     * Check if context contains key
     */
    fun contains(key: String): Boolean = data.containsKey(key)

    /**
     * Remove value from context
     */
    fun remove(key: String) {
        data.remove(key)
    }

    /**
     * Clear all data from context
     */
    fun clear() {
        data.clear()
    }
}

/**
 * Cache for validation results
 */
class ValidationCache {
    private val cache = ConcurrentHashMap<String, ValidationResult>()

    /**
     * Get cached validation result
     */
    fun get(key: String): ValidationResult? = cache[key]

    /**
     * Cache validation result
     */
    fun put(key: String, result: ValidationResult) {
        cache[key] = result
    }

    /**
     * Remove cached validation result
     */
    fun remove(key: String) {
        cache.remove(key)
    }

    /**
     * Clear all cached results
     */
    fun clear() {
        cache.clear()
    }
}

/**
 * Extension function to create validation context
 */
fun validationContext(init: ValidationContext.() -> Unit): ValidationContext {
    return ValidationContext().apply(init)
}

/**
 * Extension function to validate with context
 */
fun <T> T.validateWithContext(
    context: ValidationContext,
    cache: ValidationCache? = null,
    init: ValidationRule<T>.() -> Unit
): List<ValidationResult> {
    val rule = ValidationRule<T>().apply(init)
    return rule.validate(this, context, cache)
}

/**
 * Extension function to check if object is valid with context
 */
fun <T> T.isValidWithContext(
    context: ValidationContext,
    cache: ValidationCache? = null,
    init: ValidationRule<T>.() -> Unit
): Boolean {
    return validateWithContext(context, cache, init).all { it is ValidationResult.Success }
} 