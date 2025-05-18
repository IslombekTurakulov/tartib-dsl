package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult

/**
 * Map validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.mapNotEmpty(map: Map<*, *>?, message: String = "Map must not be empty") {
    rule<T> {
        name("Map not empty validation")
        condition { map != null && map.isNotEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapEmpty(map: Map<*, *>?, message: String = "Map must be empty") {
    rule<T> {
        name("Map empty validation")
        condition { map != null && map.isEmpty() }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapSize(map: Map<*, *>?, size: Int, message: String = "Map size must be $size") {
    rule<T> {
        name("Map size validation")
        condition { map != null && map.size == size }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapMinSize(map: Map<*, *>?, minSize: Int, message: String = "Map size must be at least $minSize") {
    rule<T> {
        name("Map min size validation")
        condition { map != null && map.size >= minSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapMaxSize(map: Map<*, *>?, maxSize: Int, message: String = "Map size must be at most $maxSize") {
    rule<T> {
        name("Map max size validation")
        condition { map != null && map.size <= maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapSizeBetween(map: Map<*, *>?, minSize: Int, maxSize: Int, message: String = "Map size must be between $minSize and $maxSize") {
    rule<T> {
        name("Map size between validation")
        condition { map != null && map.size in minSize..maxSize }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsKey(map: Map<*, *>?, key: Any?, message: String = "Map must contain key $key") {
    rule<T> {
        name("Map contains key validation")
        condition { map != null && map.containsKey(key) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapNotContainsKey(map: Map<*, *>?, key: Any?, message: String = "Map must not contain key $key") {
    rule<T> {
        name("Map not contains key validation")
        condition { map != null && !map.containsKey(key) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsValue(map: Map<*, *>?, value: Any?, message: String = "Map must contain value $value") {
    rule<T> {
        name("Map contains value validation")
        condition { map != null && map.containsValue(value) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapNotContainsValue(map: Map<*, *>?, value: Any?, message: String = "Map must not contain value $value") {
    rule<T> {
        name("Map not contains value validation")
        condition { map != null && !map.containsValue(value) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsAllKeys(map: Map<*, *>?, keys: Set<*>, message: String = "Map must contain all keys") {
    rule<T> {
        name("Map contains all keys validation")
        condition { map != null && map.keys.containsAll(keys) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsAnyKey(map: Map<*, *>?, keys: Set<*>, message: String = "Map must contain at least one key") {
    rule<T> {
        name("Map contains any key validation")
        condition { map != null && keys.any { map.containsKey(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsNoneKey(map: Map<*, *>?, keys: Set<*>, message: String = "Map must not contain any key") {
    rule<T> {
        name("Map contains none key validation")
        condition { map != null && keys.none { map.containsKey(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsAllValues(map: Map<*, *>?, values: Collection<*>, message: String = "Map must contain all values") {
    rule<T> {
        name("Map contains all values validation")
        condition { map != null && map.values.containsAll(values) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsAnyValue(map: Map<*, *>?, values: Collection<*>, message: String = "Map must contain at least one value") {
    rule<T> {
        name("Map contains any value validation")
        condition { map != null && values.any { map.containsValue(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapContainsNoneValue(map: Map<*, *>?, values: Collection<*>, message: String = "Map must not contain any value") {
    rule<T> {
        name("Map contains none value validation")
        condition { map != null && values.none { map.containsValue(it) } }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.mapForEach(map: Map<*, *>?, init: ValidationScope<Map.Entry<*, *>>.() -> Unit) {
    rule<T> {
        name("Map for each validation")
        condition { 
            map != null && map.entries.all { entry ->
                ValidationScope<Map.Entry<*, *>>().apply(init).build().evaluate(entry).all { it.isSuccess }
            }
        }
        message("Map validation failed")
    }
} 