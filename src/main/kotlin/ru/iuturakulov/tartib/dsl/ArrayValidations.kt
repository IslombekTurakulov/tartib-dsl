package ru.iuturakulov.tartib.dsl

import kotlin.collections.sorted
import kotlin.collections.sortedDescending

/**
 * Валидации для массивов
 */
@ValidationDsl
inline fun <T, reified E : Comparable<E>> ValidationScope<T>.arraySorted(array: Array<E>?, message: String = "Array must be sorted") {
    rule<T> {
        name("Array sorted validation")
        condition { 
            array != null && array.contentEquals(array.sorted().toTypedArray())
        }
        message(message)
    }
}

@ValidationDsl
inline fun <T, reified E : Comparable<E>> ValidationScope<T>.arraySortedDescending(array: Array<E>?, message: String = "Array must be sorted in descending order") {
    rule<T> {
        name("Array sorted descending validation")
        condition { 
            array != null && array.contentEquals(array.sortedDescending().toTypedArray())
        }
        message(message)
    }
} 