package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import kotlin.sequences.sorted
import kotlin.sequences.sortedDescending

/**
 * Sequence validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.sequenceNotEmpty(sequence: Sequence<*>?, message: String = "Sequence must not be empty") {
    rule<T> {
        name("Sequence not empty validation")
        condition { sequence != null && sequence.any() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceEmpty(sequence: Sequence<*>?, message: String = "Sequence must be empty") {
    rule<T> {
        name("Sequence empty validation")
        condition { sequence != null && !sequence.any() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceSize(
    sequence: Sequence<*>?,
    size: Int,
    message: String = "Sequence size must be $size"
) {
    rule<T> {
        name("Sequence size validation")
        condition { sequence != null && sequence.count() == size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceMinSize(
    sequence: Sequence<*>?,
    minSize: Int,
    message: String = "Sequence size must be at least $minSize"
) {
    rule<T> {
        name("Sequence min size validation")
        condition { sequence != null && sequence.count() >= minSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceMaxSize(
    sequence: Sequence<*>?,
    maxSize: Int,
    message: String = "Sequence size must be at most $maxSize"
) {
    rule<T> {
        name("Sequence max size validation")
        condition { sequence != null && sequence.count() <= maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceSizeBetween(
    sequence: Sequence<*>?,
    minSize: Int,
    maxSize: Int,
    message: String = "Sequence size must be between $minSize and $maxSize"
) {
    rule<T> {
        name("Sequence size between validation")
        condition { sequence != null && sequence.count() in minSize..maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceContains(
    sequence: Sequence<*>?,
    element: Any?,
    message: String = "Sequence must contain $element"
) {
    rule<T> {
        name("Sequence contains validation")
        condition { sequence != null && sequence.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceNotContains(
    sequence: Sequence<*>?,
    element: Any?,
    message: String = "Sequence must not contain $element"
) {
    rule<T> {
        name("Sequence not contains validation")
        condition { sequence != null && !sequence.contains(element) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceContainsAll(
    sequence: Sequence<*>?,
    elements: Collection<*>,
    message: String = "Sequence must contain all elements"
) {
    rule<T> {
        name("Sequence contains all validation")
        condition { sequence != null && elements.all { sequence.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceContainsAny(
    sequence: Sequence<*>?,
    elements: Collection<*>,
    message: String = "Sequence must contain at least one element"
) {
    rule<T> {
        name("Sequence contains any validation")
        condition { sequence != null && elements.any { sequence.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceContainsNone(
    sequence: Sequence<*>?,
    elements: Collection<*>,
    message: String = "Sequence must not contain any element"
) {
    rule<T> {
        name("Sequence contains none validation")
        condition { sequence != null && elements.none { sequence.contains(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceDistinct(
    sequence: Sequence<*>?,
    message: String = "Sequence must contain distinct elements"
) {
    rule<T> {
        name("Sequence distinct validation")
        condition { sequence != null && sequence.distinct().count() == sequence.count() }
        message(message)
    }
}

@ValidationDsl
fun <T, E : Comparable<E>> ValidationScope<T>.sequenceSorted(
    sequence: Sequence<E>?,
    message: String = "Sequence must be sorted"
) {
    rule<T> {
        name("Sequence sorted validation")
        condition { sequence != null && sequence.toList() == sequence.sorted().toList() }
        message(message)
    }
}

@ValidationDsl
fun <T, E : Comparable<E>> ValidationScope<T>.sequenceSortedDescending(
    sequence: Sequence<E>?,
    message: String = "Sequence must be sorted in descending order"
) {
    rule<T> {
        name("Sequence sorted descending validation")
        condition { sequence != null && sequence.toList() == sequence.sortedDescending().toList() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceForEach(sequence: Sequence<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("Sequence for each validation")
        condition {
            sequence != null && sequence.all { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("Sequence validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceForEachIndexed(
    sequence: Sequence<*>?,
    init: ValidationScope<IndexedValue<*>>.() -> Unit
) {
    rule<T> {
        name("Sequence for each indexed validation")
        condition {
            sequence != null && sequence.withIndex().all { element ->
                ValidationScope<IndexedValue<*>>().apply(init).build().evaluate(element).all { it.isSuccess }
            }
        }
        message("Sequence validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceFirst(sequence: Sequence<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("Sequence first validation")
        condition {
            sequence != null && sequence.firstOrNull()?.let { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            } ?: false
        }
        message("Sequence first element validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceLast(sequence: Sequence<*>?, init: ValidationScope<Any?>.() -> Unit) {
    rule<T> {
        name("Sequence last validation")
        condition {
            sequence != null && sequence.lastOrNull()?.let { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            } ?: false
        }
        message("Sequence last element validation failed")
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.sequenceElementAt(
    sequence: Sequence<*>?,
    index: Int,
    init: ValidationScope<Any?>.() -> Unit
) {
    rule<T> {
        name("Sequence element at validation")
        condition {
            sequence != null && sequence.elementAtOrNull(index)?.let { element ->
                ValidationScope<Any?>().apply(init).build().evaluate(element).all { it.isSuccess }
            } ?: false
        }
        message("Sequence element at index $index validation failed")
    }
} 