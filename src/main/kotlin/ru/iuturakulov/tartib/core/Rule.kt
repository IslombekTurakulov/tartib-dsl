package ru.iuturakulov.tartib.core

/**
 * Represents a business rule that can be evaluated against a context.
 * This is the core interface for all validation rules in the system.
 *
 * @param T The type of object to validate
 */
interface Rule<T> {
    /**
     * Evaluates the rule against the given context.
     * This is the main method that determines if the rule passes or fails.
     *
     * @param context The object to validate
     * @return Result of the rule evaluation
     */
    fun evaluate(context: T): RuleResult
    
    /**
     * Gets the name of the rule.
     * This is used for identification and error reporting.
     *
     * @return The name of the rule
     */
    fun getName(): String
    
    /**
     * Gets the description of the rule.
     * This provides additional context about the rule's purpose.
     *
     * @return The description of the rule
     */
    fun getDescription(): String
}

/**
 * Represents the result of a rule evaluation.
 * This class contains information about whether the rule passed or failed,
 * along with any relevant messages or details.
 *
 * @property isSuccess Whether the rule evaluation was successful
 * @property message Error message if the rule failed, empty string otherwise
 * @property details Additional information about the validation result
 */
data class RuleResult(
    val isSuccess: Boolean,
    val message: String = "",
    val details: Map<String, Any> = emptyMap()
) {
    companion object {
        /**
         * Creates a successful validation result.
         *
         * @return A RuleResult indicating success
         */
        fun success() = RuleResult(true)
        
        /**
         * Creates a failed validation result with a message.
         *
         * @param message The error message
         * @return A RuleResult indicating failure
         */
        fun failure(message: String) = RuleResult(false, message)
        
        /**
         * Creates a failed validation result with a message and additional details.
         *
         * @param message The error message
         * @param details Additional information about the failure
         * @return A RuleResult indicating failure with details
         */
        fun failure(message: String, details: Map<String, Any>) = RuleResult(false, message, details)
    }
}

/**
 * Base class for implementing composite rules.
 * This class provides common functionality for rules that combine multiple other rules.
 *
 * @param T The type of object to validate
 */
abstract class CompositeRule<T> : Rule<T> {
    /**
     * Collection of rules that make up this composite rule.
     */
    protected val rules: MutableList<Rule<T>> = mutableListOf()
    
    /**
     * Adds a rule to the composite rule.
     *
     * @param rule The rule to add
     */
    fun addRule(rule: Rule<T>) {
        rules.add(rule)
    }
    
    /**
     * Removes a rule from the composite rule.
     *
     * @param rule The rule to remove
     */
    fun removeRule(rule: Rule<T>) {
        rules.remove(rule)
    }
} 