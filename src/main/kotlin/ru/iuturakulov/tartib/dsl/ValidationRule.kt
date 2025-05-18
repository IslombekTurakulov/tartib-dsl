package ru.iuturakulov.tartib.dsl

/**
 * Class for defining validation rules
 */
class ValidationRule<T> {
    private val rules = mutableListOf<Rule<T>>()

    /**
     * Add a new validation rule
     */
    fun rule(init: Rule<T>.() -> Unit) {
        rules.add(Rule<T>().apply(init))
    }

    /**
     * Validate an object against all rules
     */
    fun validate(
        context: T,
        validationContext: ValidationContext? = null,
        cache: ValidationCache? = null
    ): List<ValidationResult> {
        return rules.map { rule ->
            val cacheKey = "${rule.name}:${context.hashCode()}"
            cache?.get(cacheKey) ?: run {
                val result = try {
                    if (rule.condition(context)) {
                        ValidationResult.Success
                    } else {
                        ValidationResult.Error(rule.message)
                    }
                } catch (e: Exception) {
                    ValidationResult.Error("Validation failed: ${e.message}")
                }
                cache?.put(cacheKey, result)
                result
            }
        }
    }
}

/**
 * Class representing a single validation rule
 */
class Rule<T> {
    var name: String = ""
    var condition: (T) -> Boolean = { true }
    var message: String = ""

    /**
     * Set the rule name
     */
    fun name(name: String) {
        this.name = name
    }

    /**
     * Set the validation condition
     */
    fun condition(condition: (T) -> Boolean) {
        this.condition = condition
    }

    /**
     * Set the error message
     */
    fun message(message: String) {
        this.message = message
    }
}
