package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Array validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.arrayNotEmpty(array: Array<*>?, message: String = "Array must not be empty") {
    rule<T> {
        name("Array not empty validation")
        condition { array != null && array.isNotEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayEmpty(array: Array<*>?, message: String = "Array must be empty") {
    rule<T> {
        name("Array empty validation")
        condition { array != null && array.isEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arraySize(array: Array<*>?, size: Int, message: String = "Array size must be $size") {
    rule<T> {
        name("Array size validation")
        condition { array != null && array.size == size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayMinSize(array: Array<*>?, minSize: Int, message: String = "Array size must be at least $minSize") {
    rule<T> {
        name("Array min size validation")
        condition { array != null && array.size >= minSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayMaxSize(array: Array<*>?, maxSize: Int, message: String = "Array size must be at most $maxSize") {
    rule<T> {
        name("Array max size validation")
        condition { array != null && array.size <= maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arraySizeBetween(array: Array<*>?, minSize: Int, maxSize: Int, message: String = "Array size must be between $minSize and $maxSize") {
    rule<T> {
        name("Array size between validation")
        condition { array != null && array.size in minSize..maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayContains(array: Array<*>?, element: Any?, message: String = "Array must contain $element") {
    rule<T> {
        name("Array contains validation")
        condition { array != null && array.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayNotContains(array: Array<*>?, element: Any?, message: String = "Array must not contain $element") {
    rule<T> {
        name("Array not contains validation")
        condition { array != null && !array.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayContainsAll(array: Array<*>?, elements: Array<*>, message: String = "Array must contain all elements") {
    rule<T> {
        name("Array contains all validation")
        condition { array != null && elements.all { array.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayContainsAny(array: Array<*>?, elements: Array<*>, message: String = "Array must contain at least one element") {
    rule<T> {
        name("Array contains any validation")
        condition { array != null && elements.any { array.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayContainsNone(array: Array<*>?, elements: Array<*>, message: String = "Array must not contain any element") {
    rule<T> {
        name("Array contains none validation")
        condition { array != null && elements.none { array.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayDistinct(array: Array<*>?, message: String = "Array must contain distinct elements") {
    rule<T> {
        name("Array distinct validation")
        condition { array != null && array.size == array.distinct().size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.arrayForEach(array: Array<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("Array for each validation")
        condition { 
            array != null && array.all { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("Array validation failed")
    }
} 