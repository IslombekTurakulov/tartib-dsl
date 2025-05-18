package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import ru.iuturakulov.tartib.engine.RuleEngine
import java.time.LocalDate
import java.time.LocalDateTime
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * DSL marker annotation to prevent scope pollution in nested DSL blocks.
 * This ensures that only the most immediate receiver can be used in the DSL.
 */
@DslMarker
annotation class ValidationDsl

/**
 * High-level DSL builder for creating validation rules.
 * This class serves as the entry point for the validation DSL and manages the collection of rules.
 *
 * @param T The type of object to validate
 */
@ValidationDsl
class ValidationBuilder<T> {
    private val rules = mutableListOf<Rule<T>>()
    
    /**
     * Creates a validation scope and adds all rules defined within it to the builder.
     * This is the main method for defining validation rules in the DSL.
     *
     * @param init Lambda function that defines the validation rules
     */
    fun validate(init: ValidationScope<T>.() -> Unit) {
        ValidationScope<T>().apply(init).rules.forEach { rules.add(it) }
    }
    
    /**
     * Builds a RuleEngine with all the defined rules.
     * This is an internal method used by the DSL functions.
     *
     * @return A configured RuleEngine instance
     */
    internal fun build(): RuleEngine<T> = RuleEngine<T>().apply { addRules(rules) }
}

/**
 * Scope for defining validation rules.
 * This class provides the context for defining individual rules and rule groups.
 *
 * @param T The type of object to validate
 */
@ValidationDsl
class ValidationScope<T> {
    internal val rules = mutableListOf<Rule<T>>()
    
    private fun rule(init: RuleBuilder<T>.() -> Unit) {
        val rule = RuleBuilder<T>().apply(init).build()
        logger.debug { "Adding rule: ${rule.getName()}" }
        rules.add(rule)
    }

    // Basic validations
    fun required(value: Any?, message: String = "Field is required") {
        rule {
            name("Required validation")
            condition { 
                val result = value != null
                logger.debug { "Required validation: value=$value, result=$result" }
                result
            }
            message(message)
        }
    }

    fun required(value: () -> Any?, message: String = "Field is required") {
        rule {
            name("Required validation")
            condition { value() != null }
            message(message)
        }
    }

    fun notBlank(value: String?, message: String = "Field cannot be blank") {
        rule {
            name("Not blank validation")
            condition { value?.isNotBlank() == true }
            message(message)
        }
    }

    // String validations
    fun isEmail(value: String?, message: String = "Invalid email format") {
        rule {
            name("Email validation")
            condition { value?.matches(Regex(ValidationPatterns.EMAIL_PATTERN)) == true }
            message(message)
        }
    }

    fun isEmail(value: () -> String?, message: String = "Invalid email format") {
        rule {
            name("Email validation")
            condition { value()?.matches(Regex(ValidationPatterns.EMAIL_PATTERN)) == true }
            message(message)
        }
    }

    fun isPhone(value: String?, message: String = "Invalid phone format") {
        rule {
            name("Phone validation")
            condition { value?.matches(Regex(ValidationPatterns.PHONE_PATTERN)) == true }
            message(message)
        }
    }

    fun isPhone(value: () -> String?, message: String = "Invalid phone format") {
        rule {
            name("Phone validation")
            condition { value()?.matches(Regex(ValidationPatterns.PHONE_PATTERN)) == true }
            message(message)
        }
    }

    fun url(value: String?, message: String = "Invalid URL format") {
        rule {
            name("URL validation")
            condition { value?.matches(Regex(ValidationPatterns.URL_PATTERN)) == true }
            message(message)
        }
    }

    fun password(value: String?, message: String = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character") {
        rule {
            name("Password validation")
            condition { 
                val result = value?.matches(Regex(ValidationPatterns.PASSWORD_PATTERN)) == true
                logger.debug { "Password validation: value=$value, pattern=${ValidationPatterns.PASSWORD_PATTERN}, result=$result" }
                result
            }
            message(message)
        }
    }

    // Numeric validations
    fun range(min: Number, max: Number, value: Number?, message: String = "Value must be between $min and $max") {
        rule {
            name("Range validation")
            condition { value != null && value.toDouble() in min.toDouble()..max.toDouble() }
            message(message)
        }
    }

    fun min(min: Number, value: Number?, message: String = "Value must be greater than or equal to $min") {
        rule {
            name("Min validation")
            condition { value != null && value.toDouble() >= min.toDouble() }
            message(message)
        }
    }

    fun max(max: Number, value: Number?, message: String = "Value must be less than or equal to $max") {
        rule {
            name("Max validation")
            condition { value != null && value.toDouble() <= max.toDouble() }
            message(message)
        }
    }

    // Collection validations
    fun size(min: Int, max: Int, value: Collection<*>?, message: String = "Size must be between $min and $max") {
        rule {
            name("Size validation")
            condition { value != null && value.size in min..max }
            message(message)
        }
    }

    fun unique(value: Collection<*>?, message: String = "Collection must contain unique elements") {
        rule {
            name("Unique validation")
            condition { value != null && value.size == value.toSet().size }
            message(message)
        }
    }

    fun notEmpty(value: Collection<*>?, message: String = "Collection cannot be empty") {
        rule {
            name("Not empty validation")
            condition { value != null && value.isNotEmpty() }
            message(message)
        }
    }

    // Date validations
    fun dateBefore(date: LocalDate, value: LocalDate?, message: String = "Date must be before $date") {
        rule {
            name("Date before validation")
            condition { value != null && value.isBefore(date) }
            message(message)
        }
    }

    fun dateAfter(date: LocalDate, value: LocalDate?, message: String = "Date must be after $date") {
        rule {
            name("Date after validation")
            condition { value != null && value.isAfter(date) }
            message(message)
        }
    }

    fun dateTimeBefore(dateTime: LocalDateTime, value: LocalDateTime?, message: String = "DateTime must be before $dateTime") {
        rule {
            name("DateTime before validation")
            condition { value != null && value.isBefore(dateTime) }
            message(message)
        }
    }

    fun dateTimeAfter(dateTime: LocalDateTime, value: LocalDateTime?, message: String = "DateTime must be after $dateTime") {
        rule {
            name("DateTime after validation")
            condition { value != null && value.isAfter(dateTime) }
            message(message)
        }
    }

    // Composite validations
    fun whenever(condition: (T) -> Boolean, init: ValidationScope<T>.() -> Unit) {
        rule {
            name("Conditional validation")
            condition { context ->
                val conditionResult = condition(context)
                logger.debug { "Conditional validation: context=$context, conditionResult=$conditionResult" }
                
                if (conditionResult) {
                    val scope = ValidationScope<T>().apply(init)
                    val results = scope.build().evaluate(context)
                    logger.debug { "Conditional validation results: ${results.map { it.isSuccess }}" }
                    results.all { it.isSuccess }
                } else {
                    true
                }
            }
            message("Conditional validation failed")
        }
    }

    fun optional(init: ValidationScope<T>.() -> Unit) {
        val scope = ValidationScope<T>().apply(init)
        logger.debug { "Optional validation: adding ${scope.rules.size} rules" }
        rules.addAll(scope.rules)
    }

    fun <N> nested(value: N?, init: ValidationScope<N>.(N) -> Unit) {
        if (value != null) {
            logger.debug { "Nested validation for value: $value" }
            val scope = ValidationScope<N>().apply { init(value) }
            scope.rules.forEach { nestedRule ->
                rule {
                    name("Nested validation: ${nestedRule.getName()}")
                    condition { 
                        val result = nestedRule.evaluate(value).isSuccess
                        logger.debug { "Nested validation ${nestedRule.getName()}: result=$result" }
                        result
                    }
                    message(nestedRule.evaluate(value).message)
                }
            }
        }
    }

    // Custom validation
    fun custom(condition: () -> Boolean, message: String) {
        rule {
            name("Custom validation")
            condition { 
                val result = condition()
                logger.debug { "Custom validation: result=$result, message=$message" }
                result
            }
            message(message)
        }
    }

    internal fun build(): RuleEngine<T> {
        logger.debug { "Building RuleEngine with ${rules.size} rules" }
        return RuleEngine<T>().apply { addRules(rules) }
    }
}

/**
 * Creates a validation engine with the specified rules.
 * This is the main entry point for creating a reusable validation engine.
 *
 * @param T The type of object to validate
 * @param init Lambda function that defines the validation rules
 * @return A configured RuleEngine instance
 */
fun <T> validate(init: ValidationBuilder<T>.() -> Unit): RuleEngine<T> {
    return ValidationBuilder<T>().apply(init).build()
}

/**
 * Validates an object and returns all validation results.
 * This extension function provides a convenient way to validate objects and get detailed results.
 *
 * @param T The type of object to validate
 * @param init Lambda function that defines the validation rules
 * @return List of validation results
 */
fun <T> T.validate(init: ValidationScope<T>.() -> Unit): List<RuleResult> {
    logger.debug { "Starting validation for: $this" }
    val results = ValidationScope<T>().apply(init).build().evaluate(this)
    logger.debug { "Validation results: ${results.map { it.isSuccess }}" }
    return results
}

/**
 * Checks if an object is valid according to the specified rules.
 * This extension function provides a quick way to check object validity.
 *
 * @param T The type of object to validate
 * @param init Lambda function that defines the validation rules
 * @return true if the object is valid, false otherwise
 */
fun <T> T.isValid(init: ValidationScope<T>.() -> Unit): Boolean {
    logger.debug { "Checking if valid: $this" }
    val result = validate(init).all { it.isSuccess }
    logger.debug { "Is valid: $result" }
    return result
}

/**
 * Validates an object until the first failure is encountered.
 * This extension function is useful when you only need to know if there's at least one validation error.
 *
 * @param T The type of object to validate
 * @param init Lambda function that defines the validation rules
 * @return The first validation result that indicates a failure, or a success result if all rules pass
 */
fun <T> T.validateUntilFailure(init: ValidationScope<T>.() -> Unit): RuleResult {
    logger.debug { "Validating until failure: $this" }
    val engine = ValidationScope<T>().apply(init).build()
    val result = engine.validateUntilFailure(this)
    logger.debug { "Validation until failure result: ${result.isSuccess}" }
    return result
}
