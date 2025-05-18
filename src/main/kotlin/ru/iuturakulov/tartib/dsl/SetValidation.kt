package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Set validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.setNotEmpty(set: Set<*>?, message: String = "Set must not be empty") {
    rule<T> {
        name("Set not empty validation")
        condition { set != null && set.isNotEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setEmpty(set: Set<*>?, message: String = "Set must be empty") {
    rule<T> {
        name("Set empty validation")
        condition { set != null && set.isEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setSize(set: Set<*>?, size: Int, message: String = "Set size must be $size") {
    rule<T> {
        name("Set size validation")
        condition { set != null && set.size == size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setMinSize(set: Set<*>?, minSize: Int, message: String = "Set size must be at least $minSize") {
    rule<T> {
        name("Set min size validation")
        condition { set != null && set.size >= minSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setMaxSize(set: Set<*>?, maxSize: Int, message: String = "Set size must be at most $maxSize") {
    rule<T> {
        name("Set max size validation")
        condition { set != null && set.size <= maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setSizeBetween(set: Set<*>?, minSize: Int, maxSize: Int, message: String = "Set size must be between $minSize and $maxSize") {
    rule<T> {
        name("Set size between validation")
        condition { set != null && set.size in minSize..maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setContains(set: Set<*>?, element: Any?, message: String = "Set must contain $element") {
    rule<T> {
        name("Set contains validation")
        condition { set != null && set.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setNotContains(set: Set<*>?, element: Any?, message: String = "Set must not contain $element") {
    rule<T> {
        name("Set not contains validation")
        condition { set != null && !set.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setContainsAll(set: Set<*>?, elements: Set<*>, message: String = "Set must contain all elements") {
    rule<T> {
        name("Set contains all validation")
        condition { set != null && set.containsAll(elements) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setContainsAny(set: Set<*>?, elements: Set<*>, message: String = "Set must contain at least one element") {
    rule<T> {
        name("Set contains any validation")
        condition { set != null && elements.any { set.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setContainsNone(set: Set<*>?, elements: Set<*>, message: String = "Set must not contain any element") {
    rule<T> {
        name("Set contains none validation")
        condition { set != null && elements.none { set.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setIsSubsetOf(set: Set<*>?, superset: Set<*>, message: String = "Set must be a subset of the specified set") {
    rule<T> {
        name("Set is subset of validation")
        condition { set != null && superset.containsAll(set) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setIsSupersetOf(set: Set<*>?, subset: Set<*>, message: String = "Set must be a superset of the specified set") {
    rule<T> {
        name("Set is superset of validation")
        condition { set != null && set.containsAll(subset) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setIsDisjoint(set: Set<*>?, other: Set<*>, message: String = "Set must be disjoint with the specified set") {
    rule<T> {
        name("Set is disjoint validation")
        condition { set != null && set.none { other.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.setForEach(set: Set<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("Set for each validation")
        condition { 
            set != null && set.all { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("Set validation failed")
    }
} 