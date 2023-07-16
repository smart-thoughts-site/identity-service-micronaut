package site.smartthoughts.identity.application

fun interface PasswordMatcher {
    fun matches(passwordHash: String, saltHash: String, providedPassword: String): Boolean
}