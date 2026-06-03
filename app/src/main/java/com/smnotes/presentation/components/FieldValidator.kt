package com.smnotes.presentation.components

object FieldValidator {

    fun required(value: String): String? =
        if (value.isBlank()) "This field is required" else null

    fun email(value: String): String? {
        if (value.isBlank()) return null
        val pattern = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
        return if (!pattern.matches(value)) "Enter a valid email address" else null
    }

    // Login only: just checks minimum length, no complexity (backend validates the rest)
    fun passwordMinLength(value: String): String? {
        if (value.isBlank()) return null
        return if (value.length < 9) "Password must be at least 9 characters" else null
    }

    // Rules mirror AuthViewModel.isPasswordValid (backend enforced)
    fun password(value: String): String? {
        if (value.isBlank()) return null
        if (value.length < 9) return "Password must be at least 9 characters"
        if (!value.any { it.isUpperCase() }) return "Must contain at least 1 uppercase letter"
        if (!value.any { it.isDigit() }) return "Must contain at least 1 digit"
        return null
    }

    fun name(value: String): String? {
        if (value.isBlank()) return null
        return if (value.trim().length < 2) "Name must be at least 2 characters" else null
    }

    fun compose(vararg validators: (String) -> String?): (String) -> String? = { value ->
        validators.firstNotNullOfOrNull { it(value) }
    }
}
