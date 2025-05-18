package ru.iuturakulov.tartib.dsl

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Async validation rule builder
 */
class AsyncValidationRule<T>(
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) {
    private val rules = mutableListOf<suspend (T) -> ValidationResult>()

    /**
     * Add a new async validation rule
     */
    fun rule(
        name: String,
        condition: suspend (T) -> Boolean,
        message: String
    ) {
        rules.add { context ->
            try {
                val result = condition(context)
                if (result) ValidationResult.Success else ValidationResult.Error(message)
            } catch (e: Exception) {
                ValidationResult.Error("Validation failed: ${e.message}")
            }
        }
    }

    /**
     * Execute all validation rules in parallel
     */
    suspend fun validate(context: T): List<ValidationResult> = coroutineScope {
        rules.map { rule ->
            async(coroutineContext) { rule(context) }
        }.awaitAll()
    }
}

/**
 * DSL for async validation
 */
fun <T> asyncValidate(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    init: AsyncValidationRule<T>.() -> Unit
): AsyncValidationRule<T> {
    return AsyncValidationRule<T>(coroutineContext).apply(init)
}

/**
 * Extension function to validate object asynchronously
 */
suspend fun <T> T.validateAsync(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    init: AsyncValidationRule<T>.() -> Unit
): List<ValidationResult> {
    return asyncValidate(coroutineContext, init).validate(this)
}

/**
 * Extension function to check if object is valid asynchronously
 */
suspend fun <T> T.isValidAsync(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    init: AsyncValidationRule<T>.() -> Unit
): Boolean {
    return validateAsync(coroutineContext, init).all { it is ValidationResult.Success }
} 