package ru.iuturakulov.tartib.dsl

import ru.iuturakulov.tartib.core.Rule
import ru.iuturakulov.tartib.core.RuleResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Date validation rules that can be used in any validation scope
 */
@ValidationDsl
fun <T> ValidationScope<T>.dateBefore(date: LocalDate?, before: LocalDate, message: String = "Date must be before $before") {
    rule<T> {
        name("Date before validation")
        condition { date != null && date.isBefore(before) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateAfter(date: LocalDate?, after: LocalDate, message: String = "Date must be after $after") {
    rule<T> {
        name("Date after validation")
        condition { date != null && date.isAfter(after) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateBetween(date: LocalDate?, start: LocalDate, end: LocalDate, message: String = "Date must be between $start and $end") {
    rule<T> {
        name("Date between validation")
        condition { date != null && !date.isBefore(start) && !date.isAfter(end) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeBefore(dateTime: LocalDateTime?, before: LocalDateTime, message: String = "DateTime must be before $before") {
    rule<T> {
        name("DateTime before validation")
        condition { dateTime != null && dateTime.isBefore(before) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeAfter(dateTime: LocalDateTime?, after: LocalDateTime, message: String = "DateTime must be after $after") {
    rule<T> {
        name("DateTime after validation")
        condition { dateTime != null && dateTime.isAfter(after) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.dateTimeBetween(dateTime: LocalDateTime?, start: LocalDateTime, end: LocalDateTime, message: String = "DateTime must be between $start and $end") {
    rule<T> {
        name("DateTime between validation")
        condition { dateTime != null && !dateTime.isBefore(start) && !dateTime.isAfter(end) }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.ageAtLeast(date: LocalDate?, years: Int, message: String = "Age must be at least $years years") {
    rule<T> {
        name("Age validation")
        condition { 
            date != null && ChronoUnit.YEARS.between(date, LocalDate.now()) >= years
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.ageAtMost(date: LocalDate?, years: Int, message: String = "Age must be at most $years years") {
    rule<T> {
        name("Age validation")
        condition { 
            date != null && ChronoUnit.YEARS.between(date, LocalDate.now()) <= years
        }
        message(message)
    }
}

@ValidationDsl
fun <T> ValidationScope<T>.ageBetween(date: LocalDate?, minYears: Int, maxYears: Int, message: String = "Age must be between $minYears and $maxYears years") {
    rule<T> {
        name("Age validation")
        condition { 
            date != null && ChronoUnit.YEARS.between(date, LocalDate.now()) in minYears..maxYears
        }
        message(message)
    }
} 