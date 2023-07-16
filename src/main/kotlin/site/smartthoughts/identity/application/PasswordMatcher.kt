package site.smartthoughts.identity.application

fun interface PasswordMatcher {
    fun matches(passwordHash: String, saltHash: ByteArray, providedPassword: String): Boolean
}