package ru.iuturakulov.tartib.dsl

import java.util.*

/**
 * Класс для управления локализацией сообщений об ошибках
 */
class ValidationLocalization {
    private val messages = mutableMapOf<String, Map<String, String>>()
    private var defaultLocale: Locale = Locale.getDefault()
    
    /**
     * Устанавливает сообщения для указанного ключа и локали
     */
    fun setMessage(key: String, locale: Locale, message: String) {
        messages.getOrPut(key) { mutableMapOf() }.toMutableMap().apply {
            put(locale.toString(), message)
            messages[key] = this
        }
    }
    
    /**
     * Устанавливает сообщения для указанного ключа и нескольких локалей
     */
    fun setMessages(key: String, messages: Map<Locale, String>) {
        messages.forEach { (locale, message) ->
            setMessage(key, locale, message)
        }
    }
    
    /**
     * Получает сообщение для указанного ключа и локали
     */
    fun getMessage(key: String, locale: Locale = defaultLocale): String {
        return messages[key]?.get(locale.toString())
            ?: messages[key]?.get(defaultLocale.toString())
            ?: key
    }
    
    /**
     * Устанавливает локаль по умолчанию
     */
    fun setDefaultLocale(locale: Locale) {
        defaultLocale = locale
    }
    
    companion object {
        private val instance = ValidationLocalization()
        
        /**
         * Получает экземпляр ValidationLocalization
         */
        fun getInstance(): ValidationLocalization = instance
    }
}

/**
 * Расширение для RuleBuilder для поддержки локализованных сообщений
 */
fun <T> RuleBuilder<T>.localizedMessage(key: String, locale: Locale? = null) {
    val message = ValidationLocalization.getInstance().getMessage(key, locale ?: Locale.getDefault())
    this.message(message)
}

/**
 * Предопределенные ключи сообщений
 */
object ValidationMessages {
    const val REQUIRED = "validation.required"
    const val EMAIL = "validation.email"
    const val PHONE = "validation.phone"
    const val URL = "validation.url"
    const val RANGE = "validation.range"
    const val MIN = "validation.min"
    const val MAX = "validation.max"
    const val NOT_BLANK = "validation.not_blank"
    const val NOT_EMPTY = "validation.not_empty"
    const val PATTERN = "validation.pattern"
} 