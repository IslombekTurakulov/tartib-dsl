package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import ru.iuturakulov.tartib.engine.RuleEngine
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.regex.Pattern

/**
 * Property-based validation extensions
 */
@ValidationDsl
fun <T> ValidationScope<T>.required(property: (T) -> Any?, message: String = "Field is required") {
    rule {
        name("Required field validation")
        condition { property(it) != null }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notBlank(property: (T) -> String?, message: String = "Field cannot be blank") {
    rule {
        name("Not blank validation")
        condition { property(it)?.isNotBlank() == true }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.notEmpty(property: (T) -> Collection<*>?, message: String = "Collection cannot be empty") {
    rule {
        name("Not empty validation")
        condition { property(it)?.isNotEmpty() == true }
        message(message)
    }
}

/**
 * Валидации для строк с регулярными выражениями
 */
@ValidationDsl
fun <T> ValidationScope<T>.matches(regex: String, property: (T) -> String, message: String = "Invalid format") {
    rule {
        name("Pattern matching validation")
        condition { property(it).matches(Regex(regex)) }
        message(message)
    }
}

/**
 * Валидации для числовых значений
 */
@ValidationDsl
fun <T> ValidationScope<T>.range(min: Number, max: Number, property: (T) -> Number, message: String = "Value out of range") {
    rule {
        name("Range validation")
        condition { 
            val value = property(it).toDouble()
            value >= min.toDouble() && value <= max.toDouble()
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.min(min: Number, property: (T) -> Number, message: String = "Value must be greater than or equal to $min") {
    rule {
        name("Min validation")
        condition { property(it).toDouble() >= min.toDouble() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.max(max: Number, property: (T) -> Number, message: String = "Value must be less than or equal to $max") {
    rule {
        name("Max validation")
        condition { property(it).toDouble() <= max.toDouble() }
        message(message)
    }
}

/**
 * Валидации для дат
 */
@ValidationDsl
fun <T> ValidationScope<T>.dateBefore(date: LocalDate, property: (T) -> LocalDate?, message: String = "Date must be before $date") {
    rule {
        name("Date before validation")
        condition { 
            val value = property(it)
            value != null && value.isBefore(date)
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateAfter(date: LocalDate, property: (T) -> LocalDate?, message: String = "Date must be after $date") {
    rule {
        name("Date after validation")
        condition { 
            val value = property(it)
            value != null && value.isAfter(date)
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateBetween(start: LocalDate, end: LocalDate, property: (T) -> LocalDate, message: String = "Date must be between ${start} and ${end}") {
    rule {
        name("Date between validation")
        condition { 
            val date = property(it)
            !date.isBefore(start) && !date.isAfter(end)
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeBefore(dateTime: LocalDateTime, property: (T) -> LocalDateTime?, message: String = "DateTime must be before $dateTime") {
    rule {
        name("DateTime before validation")
        condition { 
            val value = property(it)
            value != null && value.isBefore(dateTime)
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeAfter(dateTime: LocalDateTime, property: (T) -> LocalDateTime?, message: String = "DateTime must be after $dateTime") {
    rule {
        name("DateTime after validation")
        condition { 
            val value = property(it)
            value != null && value.isAfter(dateTime)
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeBetween(start: LocalDateTime, end: LocalDateTime, property: (T) -> LocalDateTime, message: String = "DateTime must be between ${start} and ${end}") {
    rule {
        name("DateTime between validation")
        condition { 
            val dateTime = property(it)
            !dateTime.isBefore(start) && !dateTime.isAfter(end)
        }
        message(message)
    }
}

/**
 * Валидации для коллекций
 */
@ValidationDsl
fun <T> ValidationScope<T>.size(min: Int, max: Int, property: (T) -> Collection<*>?, message: String = "Size must be between $min and $max") {
    rule {
        name("Size validation")
        condition { 
            val value = property(it)
            value != null && value.size in min..max
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.unique(property: (T) -> Collection<*>?, message: String = "Collection must contain unique elements") {
    rule {
        name("Unique validation")
        condition { 
            val value = property(it)
            value != null && value.size == value.toSet().size
        }
        message(message)
    }
}

/**
 * Валидации для пользовательских правил
 */
@ValidationDsl
fun <T> ValidationScope<T>.custom(condition: (T) -> Boolean, message: String) {
    rule {
        name("Custom validation")
        condition { condition(it) }
        message(message)
    }
}

/**
 * Составные правила
 */
private class CompositeAndRule<T>(private val rules: List<Rule<T>>) : Rule<T> {
    override fun evaluate(context: T): RuleResult {
        for (rule in rules) {
            val result = rule.evaluate(context)
            if (!result.isSuccess) {
                return result
            }
        }
        return RuleResult.success()
    }
    
    override fun getName() = "AND rule"
    override fun getDescription() = "All rules must pass"
}

private class CompositeOrRule<T>(private val rules: List<Rule<T>>) : Rule<T> {
    override fun evaluate(context: T): RuleResult {
        for (rule in rules) {
            val result = rule.evaluate(context)
            if (result.isSuccess) {
                return result
            }
        }
        return RuleResult.failure("None of the rules passed")
    }
    
    override fun getName() = "OR rule"
    override fun getDescription() = "At least one rule must pass"
}

private class CompositeNotRule<T>(private val rules: List<Rule<T>>) : Rule<T> {
    override fun evaluate(context: T): RuleResult {
        for (rule in rules) {
            val result = rule.evaluate(context)
            if (result.isSuccess) {
                return RuleResult.failure("Rule should not pass")
            }
        }
        return RuleResult.success()
    }
    
    override fun getName() = "NOT rule"
    override fun getDescription() = "Rules must not pass"
}

/**
 * Расширения для RuleEngine
 */
fun <T> RuleEngine<T>.withContext(context: T): RuleEngineWithContext<T> {
    return RuleEngineWithContext(this, context)
}

class RuleEngineWithContext<T>(private val engine: RuleEngine<T>, private val context: T) {
    fun isValid(): Boolean = engine.validateAll(context)
    fun validate(): List<RuleResult> = engine.evaluate(context)
    fun validateUntilFailure(): RuleResult = engine.validateUntilFailure(context)
} 