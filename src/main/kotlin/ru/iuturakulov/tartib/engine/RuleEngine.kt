package ru.iuturakulov.tartib.engine

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Engine for executing business rules.
 */
class RuleEngine<T> {
    private val rules = mutableListOf<Rule<T>>()
    
    /**
     * Adds a rule to the engine.
     */
    fun addRule(rule: Rule<T>) {
        rules.add(rule)
    }
    
    /**
     * Adds multiple rules to the engine.
     */
    fun addRules(rules: Collection<Rule<T>>) {
        this.rules.addAll(rules)
    }
    
    /**
     * Evaluates all rules against the given context.
     * @return List of results for each rule
     */
    fun evaluate(context: T): List<RuleResult> {
        return rules.map { it.evaluate(context) }
    }
    
    /**
     * Evaluates all rules and returns true only if all rules pass.
     */
    fun validateAll(context: T): Boolean {
        return evaluate(context).all { it.isSuccess }
    }
    
    /**
     * Evaluates rules until first failure.
     * @return First failure result or success if all rules pass
     */
    fun validateUntilFailure(context: T): RuleResult {
        for (rule in rules) {
            val result = rule.evaluate(context)
            if (!result.isSuccess) {
                return result
            }
        }
        return RuleResult.success()
    }
    
    /**
     * Clears all rules from the engine.
     */
    fun clearRules() {
        rules.clear()
    }
    
    /**
     * Gets the current number of rules in the engine.
     */
    fun getRuleCount(): Int = rules.size
} 