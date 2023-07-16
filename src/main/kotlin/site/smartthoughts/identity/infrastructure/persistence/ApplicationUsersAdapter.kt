package site.smartthoughts.identity.infrastructure.persistence

import jakarta.inject.Singleton
import site.smartthoughts.identity.application.ApplicationUser
import site.smartthoughts.identity.application.ApplicationUsers

@Singleton
internal class ApplicationUsersAdapter(private val userRepository: UserRepository) : ApplicationUsers {

    override fun findByName(username: String): ApplicationUser? {
        val result = userRepository.findByUsername(username) ?: return null
        return ApplicationUser(
            result.id!!,
            result.username,
            result.email,
            result.salt,
            result.password
        )
    }
}