package site.smartthoughts.identity.infrastructure.persistence

import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository

import java.util.*

@Repository
interface UserRepository : GenericRepository<UserRecord, UUID> {

    @Executable
    fun findByUsername(username: String): UserRecord?
}