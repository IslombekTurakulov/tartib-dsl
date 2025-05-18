package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import kotlin.collections.sorted
import kotlin.collections.sortedDescending

/**
 * List validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.listNotEmpty(list: List<*>?, message: String = "List must not be empty") {
    rule<T> {
        name("List not empty validation")
        condition { list != null && list.isNotEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listEmpty(list: List<*>?, message: String = "List must be empty") {
    rule<T> {
        name("List empty validation")
        condition { list != null && list.isEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listSize(list: List<*>?, size: Int, message: String = "List size must be $size") {
    rule<T> {
        name("List size validation")
        condition { list != null && list.size == size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listMinSize(list: List<*>?, minSize: Int, message: String = "List size must be at least $minSize") {
    rule<T> {
        name("List min size validation")
        condition { list != null && list.size >= minSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listMaxSize(list: List<*>?, maxSize: Int, message: String = "List size must be at most $maxSize") {
    rule<T> {
        name("List max size validation")
        condition { list != null && list.size <= maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listSizeBetween(list: List<*>?, minSize: Int, maxSize: Int, message: String = "List size must be between $minSize and $maxSize") {
    rule<T> {
        name("List size between validation")
        condition { list != null && list.size in minSize..maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listContains(list: List<*>?, element: Any?, message: String = "List must contain $element") {
    rule<T> {
        name("List contains validation")
        condition { list != null && list.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listNotContains(list: List<*>?, element: Any?, message: String = "List must not contain $element") {
    rule<T> {
        name("List not contains validation")
        condition { list != null && !list.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listContainsAll(list: List<*>?, elements: List<*>, message: String = "List must contain all elements") {
    rule<T> {
        name("List contains all validation")
        condition { list != null && list.containsAll(elements) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listContainsAny(list: List<*>?, elements: List<*>, message: String = "List must contain at least one element") {
    rule<T> {
        name("List contains any validation")
        condition { list != null && elements.any { list.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listContainsNone(list: List<*>?, elements: List<*>, message: String = "List must not contain any element") {
    rule<T> {
        name("List contains none validation")
        condition { list != null && elements.none { list.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listDistinct(list: List<*>?, message: String = "List must contain distinct elements") {
    rule<T> {
        name("List distinct validation")
        condition { list != null && list.size == list.distinct().size }
        message(message)
    }
}

@ValidationDsl
fun <T, E : Comparable<E>> ValidationScope<T>.listSorted(list: List<E>?, message: String = "List must be sorted") {
    rule<T> {
        name("List sorted validation")
        condition { list != null && list == list.sorted() }
        message(message)
    }
}

@ValidationDsl
fun <T, E : Comparable<E>> ValidationScope<T>.listSortedDescending(list: List<E>?, message: String = "List must be sorted in descending order") {
    rule<T> {
        name("List sorted descending validation")
        condition { list != null && list == list.sortedDescending() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listForEach(list: List<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("List for each validation")
        condition { 
            list != null && list.all { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("List validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listForEachIndexed(list: List<*>?, init: ValidationScope<IndexedValue<*>>.() -> Unit) {
    rule<T> {
        name("List for each indexed validation")
        condition { 
            list != null && list.withIndex().all { element ->
                ValidationScope<IndexedValue<*>>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("List validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listFirst(list: List<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("List first validation")
        condition { 
            list != null && list.isNotEmpty() && ValidationScope<Any?>().apply(init).build().evaluate(list.first()).all { it.isSuccess }
        }
        message("List first element validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listLast(list: List<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("List last validation")
        condition { 
            list != null && list.isNotEmpty() && ValidationScope<Any?>().apply(init).build().evaluate(list.last()).all { it.isSuccess }
        }
        message("List last element validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.listElementAt(list: List<*>?, index: Int, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("List element at validation")
        condition { 
            list != null && index in list.indices && ValidationScope<Any?>().apply(init).build().evaluate(list[index]).all { it.isSuccess }
        }
        message("List element at index $index validation failed")
    }
} 