package site.smartthoughts.identity

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class LoginIncludesRefreshTokenTest(@Client("/") val client: HttpClient) {
    /*
    @Test
    fun uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() {
        val creds = UsernamePasswordCredentials("sherlock", "password")
        val request = HttpRequest.POST("/login", creds)
        val response = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)

        assertEquals("sherlock", response.username)
        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)

        assertTrue(JWTParser.parse(response.accessToken) is SignedJWT)
    }
     */
}