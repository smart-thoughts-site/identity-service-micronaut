package site.smartthoughts.identity.application

import java.util.*

class ApplicationUser(
    val id: UUID,
    val username: String,
    val email: String,
    val saltHash: String,
    val passwordHash: String)