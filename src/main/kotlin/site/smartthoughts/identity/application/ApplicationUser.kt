package site.smartthoughts.identity.application

class ApplicationUser(
    val username: String,
    val email: String,
    val saltHash: String,
    val passwordHash: String)