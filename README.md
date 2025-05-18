# Tartib DSL

Tartib DSL is a powerful Kotlin library that provides a fluent and type-safe way to define and manage business rules and validations. It offers a domain-specific language (DSL) for creating validation rules, conditions, and business logic in a declarative way.

## Features

- **Expressive DSL**: Write validation rules in a natural, readable way
- **Type-safe**: Leverage Kotlin's type system for compile-time safety
- **Composable Rules**: Combine rules using logical operators (AND, OR, NOT)
- **Extensible**: Easy to add custom validation rules
- **Nested Validations**: Validate complex object hierarchies
- **Conditional Rules**: Apply rules based on conditions
- **Detailed Results**: Get comprehensive validation results with error messages

## Installation

Add the following to your `build.gradle.kts`:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/IslombekTurakulov/tartib-dsl")
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("ru.iuturakulov:tartib-dsl:0.1.0")
}
```

To use the library, you need to:
1. Create a GitHub account if you don't have one
2. Create a Personal Access Token (PAT) with `read:packages` scope
3. Set the following environment variables:
   - `GITHUB_USERNAME`: your GitHub username
   - `GITHUB_TOKEN`: your Personal Access Token

## Quick Start

```kotlin
data class User(
    val username: String,
    val email: String,
    val age: Int
)

// Define validation rules
val isValid = user.isValid {
    validate {
        notBlank { it.username }
        isEmail { it.email }
        range(18, 100) { it.age }
    }
}

// Get validation results
val results = user.validate {
    validate {
        notBlank { it.username }
        isEmail { it.email }
        range(18, 100) { it.age }
    }
}
```

## Core Concepts

### Basic Validations

```kotlin
validate {
    // Required field validation
    required { it.field }
    
    // String validations
    notBlank { it.stringField }
    
    // Collection validations
    notEmpty { it.collection }
    size(1, 10) { it.collection }
    unique { it.collection }
}
```

### Format Validations

```kotlin
validate {
    // Email validation
    isEmail { it.email }
    
    // Phone number validation
    isPhone { it.phone }
    
    // URL validation
    url { it.website }
    
    // Custom pattern matching
    matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") { it.email }
}
```

### Numeric Validations

```kotlin
validate {
    // Range validation
    range(0, 100) { it.score }
    
    // Minimum value
    min(0.0) { it.price }
    
    // Maximum value
    max(1000.0) { it.amount }
    
    // Additional numeric validations
    positive { it.value }
    negative { it.value }
    even { it.value }
    odd { it.value }
    divisibleBy(2) { it.value }
}
```

### Date Validations

```kotlin
validate {
    // Date before
    dateBefore(LocalDate.now()) { it.deliveryDate }
    
    // Date after
    dateAfter(LocalDate.now().plusDays(30)) { it.expiryDate }
    
    // Date range
    dateBetween(
        start = LocalDate.now(),
        end = LocalDate.now().plusDays(30)
    ) { it.validityPeriod }
    
    // DateTime validations
    dateTimeBefore(LocalDateTime.now()) { it.createdAt }
    dateTimeAfter(LocalDateTime.now()) { it.updatedAt }
}
```

### Collection Validations

```kotlin
validate {
    // List validations
    listNotEmpty { it.items }
    listSize(5) { it.items }
    listMinSize(1) { it.items }
    listMaxSize(10) { it.items }
    
    // Set validations
    setNotEmpty { it.uniqueItems }
    setSize(3) { it.uniqueItems }
    
    // Array validations
    arrayNotEmpty { it.data }
    arraySize(5) { it.data }
    
    // Map validations
    mapNotEmpty { it.properties }
    mapSize(3) { it.properties }
    
    // Sequence validations
    sequenceNotEmpty { it.sequence }
    sequenceSize(5) { it.sequence }
}
```

### Composite Rules

```kotlin
validate {
    // AND rule
    and {
        rule {
            name("Username length")
            condition { it.username.length >= 3 }
            message("Username too short")
        }
        rule {
            name("Email format")
            condition { it.email.contains("@") }
            message("Invalid email format")
        }
    }
    
    // OR rule
    or {
        rule {
            name("Phone validation")
            condition { it.phone != null }
            message("Phone is required")
        }
        rule {
            name("Email validation")
            condition { it.email.isNotBlank() }
            message("Email is required")
        }
    }
    
    // NOT rule
    not {
        rule {
            name("Admin username")
            condition { it.username.contains("admin") }
            message("Username cannot contain 'admin'")
        }
    }
}
```

### Nested Validations

```kotlin
data class Order(
    val items: List<OrderItem>,
    val address: Address
)

validate {
    // Validate nested object
    nested({ it.address }) {
        notBlank { it.street }
        notBlank { it.city }
        matches("^\\d{5}(-\\d{4})?$") { it.zipCode }
    }
    
    // Validate collection elements
    forEach({ it.items }) {
        required { it.productId }
        min(1) { it.quantity }
    }
}
```

### Conditional Validations

```kotlin
validate {
    // Apply rules only when condition is met
    whenever({ it.totalAmount > 100 }) {
        min(2) { it.items.size }
        required { it.deliveryAddress }
    }
}
```

### Custom Validations

```kotlin
validate {
    // Custom validation rule
    custom(
        property = { it.field },
        validator = { value -> 
            value is String && value.length >= 3 && !value.contains(" ")
        },
        message = "Custom validation failed"
    )
}
```

## Advanced Usage

### Creating Reusable Validation Rules

```kotlin
val userValidationRules = validate<User> {
    validate {
        notBlank { it.username }
        isEmail { it.email }
        range(18, 100) { it.age }
    }
}

// Reuse rules
val isValid = userValidationRules.withContext(user).isValid()
```

### Combining Multiple Validation Sets

```kotlin
val basicRules = validate<User> {
    validate {
        notBlank { it.username }
        isEmail { it.email }
    }
}

val advancedRules = validate<User> {
    validate {
        range(18, 100) { it.age }
        isPhone { it.phone }
    }
}

// Combine rules
val allRules = validate<User> {
    validate {
        rules(basicRules.rules)
        rules(advancedRules.rules)
    }
}
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 