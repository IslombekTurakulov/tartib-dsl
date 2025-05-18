package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * DSL marker annotation for rule building blocks.
 * This ensures proper scoping of rule-related DSL functions.
 */
@DslMarker
annotation class RulesDsl

/**
 * Builder class for creating individual validation rules.
 * This class provides a fluent interface for configuring rule properties.
 *
 * @param T The type of object to validate
 */
@RulesDsl
class RuleBuilder<T> {
    private var name: String = ""
    private var description: String = ""
    private var condition: ((T) -> Boolean)? = null
    private var message: String = ""
    
    /**
     * Sets the name of the rule.
     * This is used for identification and error reporting.
     *
     * @param name The name of the rule
     */
    fun name(name: String) {
        this.name = name
    }
    
    /**
     * Sets the description of the rule.
     * This provides additional context about the rule's purpose.
     *
     * @param description The description of the rule
     */
    fun description(description: String) {
        this.description = description
    }
    
    /**
     * Sets the validation condition for the rule.
     * This is the core logic that determines if the rule passes or fails.
     *
     * @param predicate The validation condition
     */
    fun condition(predicate: (T) -> Boolean) {
        this.condition = predicate
    }
    
    /**
     * Sets the error message for when the rule fails.
     * This message will be included in the validation result.
     *
     * @param message The error message
     */
    fun message(message: String) {
        this.message = message
    }
    
    /**
     * Builds the rule with the configured properties.
     * This is an internal method that creates the actual Rule implementation.
     *
     * @return A configured Rule instance
     * @throws IllegalStateException if required properties are not set
     */
    internal fun build(): Rule<T> {
        require(condition != null) { "Condition must be specified" }
        require(name.isNotBlank()) { "Name must be specified" }
        
        return object : Rule<T> {
            override fun evaluate(context: T): RuleResult {
                return if (condition!!(context)) {
                    RuleResult.success()
                } else {
                    RuleResult.failure(message.ifBlank { "Rule '$name' failed" })
                }
            }
            
            override fun getName(): String = name
            
            override fun getDescription(): String = description
        }
    }
}

/**
 * Creates a new validation rule.
 * This is the main entry point for creating individual rules in the DSL.
 *
 * @param T The type of object to validate
 * @param init Lambda function that configures the rule
 * @return A configured Rule instance
 */
fun <T> rule(init: RuleBuilder<T>.() -> Unit): Rule<T> {
    return RuleBuilder<T>().apply(init).build()
}

/**
 * Creates a collection of validation rules.
 * This is used for creating multiple related rules at once.
 *
 * @param T The type of object to validate
 * @param init Lambda function that defines multiple rules
 * @return A list of configured Rule instances
 */
fun <T> rules(init: RulesBuilder<T>.() -> Unit): List<Rule<T>> {
    return RulesBuilder<T>().apply(init).build()
}

/**
 * Builder class for creating collections of rules.
 * This class manages multiple rules and provides a way to group them together.
 *
 * @param T The type of object to validate
 */
@RulesDsl
class RulesBuilder<T> {
    private val rules = mutableListOf<Rule<T>>()
    
    /**
     * Adds a rule to the collection.
     *
     * @param init Lambda function that configures the rule
     */
    fun rule(init: RuleBuilder<T>.() -> Unit) {
        rules.add(RuleBuilder<T>().apply(init).build())
    }
    
    /**
     * Builds the collection of rules.
     * This is an internal method that returns the configured rules.
     *
     * @return A list of configured Rule instances
     */
    internal fun build(): List<Rule<T>> = rules.toList()
} 