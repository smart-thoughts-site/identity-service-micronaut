package site.smartthoughts.identity.application

fun interface ApplicationUsers {
    fun findByName(username: String): ApplicationUser?
}