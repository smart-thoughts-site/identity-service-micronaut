package site.smartthoughts.identity;

import site.smartthoughts.identity.application.ApplicationUser
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object ApplicationUserBuilder {

    private const val iterations = 1000
    private val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
    private val random = SecureRandom()

    fun create(username: String, password: String): ApplicationUser {
        var saltHash = random.generateSeed(512);

        val spec = PBEKeySpec(password.toCharArray(), saltHash, iterations, 512)
        val passwordHash = secretKeyFactory.generateSecret(spec).encoded
        val base64PwHash = Base64.getEncoder().encodeToString(passwordHash)

        return ApplicationUser(
            UUID.randomUUID(),
            username,
            "email",
            saltHash,
            base64PwHash
        )
    }
}
