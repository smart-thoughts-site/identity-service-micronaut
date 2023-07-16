package site.smartthoughts.identity.infrastructure.authentication

import jakarta.inject.Singleton
import site.smartthoughts.identity.application.PasswordMatcher
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Singleton
internal class BCryptPasswordMatcher : PasswordMatcher {

    companion object {
        private const val iterations = 1000
        private val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
    }

    override fun matches(passwordHash: String, saltHash: String, providedPassword: String): Boolean {
        println("passwordHash: $passwordHash, providedPassword: $providedPassword, saltHash: $saltHash")

        val saltHashAsBytes = Base64.getDecoder().decode(saltHash)

        val spec = PBEKeySpec(providedPassword.toCharArray(), saltHashAsBytes, iterations, 512)
        val providedPwHash = secretKeyFactory.generateSecret(spec).encoded
        val base64PwHash = Base64.getEncoder().encodeToString(providedPwHash)

        println("provided hash: $base64PwHash, user pw hash: $passwordHash")

        return passwordHash.contentEquals(base64PwHash)
    }
}