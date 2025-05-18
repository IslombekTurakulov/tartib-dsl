package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Collection validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T, E> ValidationScope<T>.all(collection: Collection<E>?, validator: (E) -> Boolean, message: String = "All elements must satisfy the condition") {
   rule<T> {
        name("All elements validation")
        condition { collection != null && collection.all(validator) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.any(collection: Collection<E>?, validator: (E) -> Boolean, message: String = "At least one element must satisfy the condition") {
   rule<T> {
        name("Any element validation")
        condition { collection != null && collection.any(validator) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.none(collection: Collection<E>?, validator: (E) -> Boolean, message: String = "No elements should satisfy the condition") {
   rule<T> {
        name("None validation")
        condition { collection != null && collection.none(validator) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.forEach(collection: Collection<E>?, init: ValidationScope<E>.() -> Unit) {
   rule<T> {
        name("For each validation")
        condition { 
            collection != null && collection.all { element ->
                val scope = ValidationScope<E>().apply(init)
                scope.build().evaluate(element).all { it.isSuccess }
            }
        }
        message("Collection validation failed")
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.distinct(collection: Collection<E>?, message: String = "Collection must contain distinct elements") {
   rule<T> {
        name("Distinct validation")
        condition { collection != null && collection.size == collection.distinct().size }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.contains(element: E, collection: Collection<E>?, message: String = "Collection must contain the specified element") {
   rule<T> {
        name("Contains validation")
        condition { collection != null && collection.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.notContains(element: E, collection: Collection<E>?, message: String = "Collection must not contain the specified element") {
   rule<T> {
        name("Not contains validation")
        condition { collection != null && !collection.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.subsetOf(superset: Collection<E>, collection: Collection<E>?, message: String = "Collection must be a subset of the specified collection") {
   rule<T> {
        name("Subset validation")
        condition { collection != null && superset.containsAll(collection) }
        message(message)
    }
}

@ValidationDsl
fun <T, E> ValidationScope<T>.supersetOf(subset: Collection<E>, collection: Collection<E>?, message: String = "Collection must be a superset of the specified collection") {
   rule<T> {
        name("Superset validation")
        condition { collection != null && collection.containsAll(subset) }
        message(message)
    }
}

/**
 * Collection validation rules
 */

// Обёртка для коллекционных правил
abstract class CollectionRuleBuilder<T, C : Collection<T>>(
    protected val validationRule: ValidationRule<C>,
    private val dsl: ValidationRule<T>.() -> Unit = {}
) {
    open fun rule(init: ValidationRule<T>.() -> Unit) {
        validationRule.all(init)
    }
    
    open fun rule(condition: (T) -> Boolean, message: String = "Invalid element") {
        validationRule.all {
           rule<T> {
                this.condition(condition)
                this.message(message)
            }
        }
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.all(): CollectionRuleBuilder<T, C> = object : CollectionRuleBuilder<T, C>(this) {
    override fun rule(init: ValidationRule<T>.() -> Unit) {
        validationRule.rule {
            name("All elements validation")
            condition { collection ->
                collection.all { element ->
                    element.isValidInternal(init)
                }
            }
            message("Not all elements are valid")
        }
    }
    
    override fun rule(condition: (T) -> Boolean, message: String) {
        validationRule.rule {
            name("All elements validation")
            condition { collection ->
                collection.all { element ->
                    condition(element)
                }
            }
            message(message)
        }
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.any(): CollectionRuleBuilder<T, C> = object : CollectionRuleBuilder<T, C>(this) {
    override fun rule(init: ValidationRule<T>.() -> Unit) {
        validationRule.rule {
            name("Any element validation")
            condition { collection ->
                collection.any { element ->
                    element.isValidInternal(init)
                }
            }
            message("No valid elements found")
        }
    }
    
    override fun rule(condition: (T) -> Boolean, message: String) {
        validationRule.rule {
            name("Any element validation")
            condition { collection ->
                collection.any { element ->
                    condition(element)
                }
            }
            message(message)
        }
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.none(): CollectionRuleBuilder<T, C> = object : CollectionRuleBuilder<T, C>(this) {
    override fun rule(init: ValidationRule<T>.() -> Unit) {
        validationRule.rule {
            name("No elements validation")
            condition { collection ->
                collection.none { element ->
                    element.isValidInternal(init)
                }
            }
            message("Found valid elements when none should be valid")
        }
    }
    
    override fun rule(condition: (T) -> Boolean, message: String) {
        validationRule.rule {
            name("No elements validation")
            condition { collection ->
                collection.none { element ->
                    condition(element)
                }
            }
            message(message)
        }
    }
}

// count
class CountRuleBuilder<T, C : Collection<T>>(
    private val validationRule: ValidationRule<C>,
    private val min: Int? = null,
    private val max: Int? = null
) {
    fun rule(init: ValidationRule<T>.() -> Unit) {
        validationRule.rule {
            name("Count validation")
            condition { collection ->
                val count = collection.count { element ->
                    element.isValidInternal(init)
                }
                (min == null || count >= min) && (max == null || count <= max)
            }
            message("Collection count is not within required range")
        }
    }
    
    fun rule(condition: (T) -> Boolean, message: String = "Invalid element") {
        validationRule.rule {
            name("Count validation")
            condition { collection ->
                val count = collection.count { element ->
                    condition(element)
                }
                (min == null || count >= min) && (max == null || count <= max)
            }
            message(message)
        }
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.count(min: Int? = null, max: Int? = null): CountRuleBuilder<T, C> =
    CountRuleBuilder(this, min, max)

// Оригинальные функции для обратной совместимости
fun <T, C : Collection<T>> ValidationRule<C>.all(validator: ValidationRule<T>.() -> Unit) {
   rule<C> {
        name("All elements validation")
        condition { collection ->
            collection.all { element ->
                element.isValidInternal(validator)
            }
        }
        message("Not all elements are valid")
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.any(validator: ValidationRule<T>.() -> Unit) {
   rule<C> {
        name("Any element validation")
        condition { collection ->
            collection.any { element ->
                element.isValidInternal(validator)
            }
        }
        message("No valid elements found")
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.none(validator: ValidationRule<T>.() -> Unit) {
   rule<C> {
        name("No elements validation")
        condition { collection ->
            collection.none { element ->
                element.isValidInternal(validator)
            }
        }
        message("Found valid elements when none should be valid")
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.unique(selector: (T) -> Any) {
   rule<C> {
        name("Unique elements validation")
        condition { collection ->
            collection.map(selector).distinct().size == collection.size
        }
        message("Collection contains duplicate elements")
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.ordered(comparator: Comparator<T>) {
   rule<C> {
        name("Ordered elements validation")
        condition { collection ->
            collection.zipWithNext().all { (a, b) ->
                comparator.compare(a, b) <= 0
            }
        }
        message("Collection is not properly ordered")
    }
}

fun <T, C : Collection<T>> ValidationRule<C>.grouped(
    groupBy: (T) -> Any,
    validator: ValidationRule<List<T>>.() -> Unit
) {
   rule<C> {
        name("Grouped elements validation")
        condition { collection ->
            collection.groupBy(groupBy).values.all { group ->
                group.isValidInternal(validator)
            }
        }
        message("Not all groups are valid")
    }
}

// Вспомогательная функция для внутренней валидации
private fun <T> T.isValidInternal(validator: ValidationRule<T>.() -> Unit): Boolean {
    val rule = ValidationRule<T>(validate = { true }, message = "Internal validation").apply(validator)
    return rule.validate(this).all { it is ValidationResult.Success }
} 