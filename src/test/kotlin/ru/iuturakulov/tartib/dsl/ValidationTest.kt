package ru.iuturakulov.tartib.dsl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime

class ValidationTest {

    data class User(
        val name: String?,
        val email: String?,
        val phone: String?,
        val age: Int?,
        val password: String?,
        val birthDate: LocalDate?,
        val registrationDate: LocalDateTime?,
        val tags: List<String>?,
        val roles: Set<String>?,
        val settings: Map<String, Any>?,
        val scores: Array<Int>?,
        val isActive: Boolean?,
        val notes: Sequence<String>?
    )

    @Test
    fun `test basic validations`() {
        val user = User(
            name = "John Doe",
            email = "john@example.com",
            phone = "+1234567890",
            age = 25,
            password = "Password123!",
            birthDate = LocalDate.now().minusYears(25),
            registrationDate = LocalDateTime.now(),
            tags = listOf("user", "premium"),
            roles = setOf("admin", "user"),
            settings = mapOf("theme" to "dark", "notifications" to true),
            scores = arrayOf(1, 2, 3),
            isActive = true,
            notes = sequenceOf("note1", "note2")
        )

        val results = user.validate {
            // Basic validations
            required(user.name)
            notBlank(user.name)
            
            // String validations
            isEmail(user.email)
            isPhone(user.phone)
            password(user.password)
            length(user.name, 3, 50)
            matches(user.name, "^[A-Za-z ]+$")
            
            // Number validations
            range(0, 120, user.age)
            positive(user.age)
            even(user.age)
            
            // Date validations
            dateBefore(LocalDate.now(), user.birthDate)
            dateTimeAfter(LocalDateTime.now().minusDays(1), user.registrationDate)
            ageAtLeast(user.birthDate, 18)
            
            // List validations
            listNotEmpty(user.tags)
            listSize(user.tags, 2)
            listContains(user.tags, "user")
            listDistinct(user.tags)
            
            // Set validations
            setNotEmpty(user.roles)
            setContainsAll(user.roles, setOf("user"))
            setIsSubsetOf(user.roles, setOf("admin", "user", "guest"))
            
            // Map validations
            mapNotEmpty(user.settings)
            mapContainsKey(user.settings, "theme")
            mapContainsValue(user.settings, "dark")
            
            // Array validations
            arrayNotEmpty(user.scores)
            arraySize(user.scores, 3)
            arraySorted(user.scores)
            
            // Boolean validations
            isTrue(user.isActive)
            
            // Sequence validations
            sequenceNotEmpty(user.notes)
            sequenceSize(user.notes, 2)
            sequenceContains(user.notes, "note1")
        }

        assertTrue(results.all { it.isSuccess })
    }

    @Test
    fun `test validation failures`() {
        val user = User(
            name = "Jo", // Too short
            email = "invalid-email", // Invalid email
            phone = "123", // Invalid phone
            age = -1, // Negative age
            password = "weak", // Weak password
            birthDate = LocalDate.now().plusYears(1), // Future date
            registrationDate = LocalDateTime.now().minusDays(2), // Too old
            tags = listOf("user", "user"), // Duplicate
            roles = setOf("unknown"), // Invalid role
            settings = mapOf(), // Empty map
            scores = arrayOf(3, 2, 1), // Unsorted
            isActive = false, // Inactive
            notes = sequenceOf() // Empty sequence
        )

        val results = user.validate {
            // Basic validations
            required(user.name)
            notBlank(user.name)
            
            // String validations
            isEmail(user.email)
            isPhone(user.phone)
            password(user.password)
            length(user.name, 3, 50)
            
            // Number validations
            positive(user.age)
            
            // Date validations
            dateBefore(LocalDate.now(), user.birthDate)
            dateTimeAfter(LocalDateTime.now().minusDays(1), user.registrationDate)
            
            // List validations
            listDistinct(user.tags)
            
            // Set validations
            setContainsAll(user.roles, setOf("user"))
            
            // Map validations
            mapNotEmpty(user.settings)
            
            // Array validations
            arraySorted(user.scores)
            
            // Boolean validations
            isTrue(user.isActive)
            
            // Sequence validations
            sequenceNotEmpty(user.notes)
        }

        assertFalse(results.all { it.isSuccess })
    }

    @Test
    fun `test nested validations`() {
        data class Address(
            val street: String?,
            val city: String?,
            val zipCode: String?
        )

        data class Profile(
            val user: User?,
            val address: Address?,
            val preferences: Map<String, Any>?
        )

        val profile = Profile(
            user = User(
                name = "John Doe",
                email = "john@example.com",
                phone = "+1234567890",
                age = 25,
                password = "Password123!",
                birthDate = LocalDate.now().minusYears(25),
                registrationDate = LocalDateTime.now(),
                tags = listOf("user"),
                roles = setOf("user"),
                settings = mapOf("theme" to "dark"),
                scores = arrayOf(1, 2, 3),
                isActive = true,
                notes = sequenceOf("note1")
            ),
            address = Address(
                street = "Main St",
                city = "New York",
                zipCode = "10001"
            ),
            preferences = mapOf(
                "notifications" to true,
                "language" to "en"
            )
        )

        val results = profile.validate {
            // Nested user validation
            nested(profile.user) { user ->
                required(user.name)
                isEmail(user.email)
                isPhone(user.phone)
            }

            // Nested address validation
            nested(profile.address) { address ->
                required(address.street)
                required(address.city)
                matches(address.zipCode, "^\\d{5}$")
            }

            // Nested map validation
            nested(profile.preferences) { prefs ->
                mapContainsKey(prefs, "notifications")
                mapContainsValue(prefs, "en")
            }
        }

        assertTrue(results.all { it.isSuccess })
    }

    @Test
    fun `test conditional validations`() {
        val user = User(
            name = "John Doe",
            email = "john@example.com",
            phone = "+1234567890",
            age = 25,
            password = "Password123!",
            birthDate = LocalDate.now().minusYears(25),
            registrationDate = LocalDateTime.now(),
            tags = listOf("user"),
            roles = setOf("user"),
            settings = mapOf("theme" to "dark"),
            scores = arrayOf(1, 2, 3),
            isActive = true,
            notes = sequenceOf("note1")
        )

        val results = user.validate {
            // Conditional validations based on user state
            whenever({ user -> user.isActive == true }) {
                required(user.email)
                isEmail(user.email)
            }

            whenever({ user -> user.age != null && user.age >= 18 }) {
                required(user.phone)
                isPhone(user.phone)
            }

            // Optional validations
            optional {
                password(user.password)
            }

            // Custom validation
            custom({ (user.name?.length ?: 0) > 2 }, "Name must be longer than 2 characters")
        }

        assertTrue(results.all { it.isSuccess })
    }
} 