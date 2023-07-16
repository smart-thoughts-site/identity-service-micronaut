package site.smartthoughts.identity

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith
import io.kotest.matchers.types.beInstanceOf
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension.getMock
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import site.smartthoughts.identity.application.ApplicationUser
import site.smartthoughts.identity.application.ApplicationUsers
import java.util.*

@MicronautTest
class AuthenticationProcessTest(
    private val applicationUsers: ApplicationUsers,
    @Client("/") val httpClient: HttpClient
) : AnnotationSpec() {

    @Test
    fun uponSuccessfulAuthenticationUserGetsOkResponse() {
        val mock = getMock(applicationUsers)

        every {
            mock.findByName("sherlock")
        } returns ApplicationUserBuilder.create("sherlock", "super-secret")

        val creds = UsernamePasswordCredentials("sherlock", "super-secret")
        val request = HttpRequest.POST("/login", creds)
        val authResponse = httpClient.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)

        authResponse.status shouldBe HttpStatus.OK
        val bearerAccessRefreshToken = authResponse.body()
        bearerAccessRefreshToken.username shouldBe "sherlock"

        withClue("accessToken should be present") {
            bearerAccessRefreshToken.accessToken shouldNotBe null
        }

        JWTParser.parse(bearerAccessRefreshToken.accessToken) should beInstanceOf<SignedJWT>()

        val accessToken = bearerAccessRefreshToken.accessToken
        val requestWithAuthorization = HttpRequest.GET<Any>("/")
            .accept(MediaType.TEXT_PLAIN)
            .bearerAuth(accessToken)
        val response = httpClient.toBlocking().exchange(requestWithAuthorization, String::class.java)

        response.status shouldBe HttpStatus.OK
        response.body() shouldBe "sherlock"

        verify { mock.findByName( any() ) }
    }

    @Test
    fun userNotFoundUnsuccessfulAuthentication() {
        val mock = getMock(applicationUsers)
        every {
            mock.findByName("sherlock")
        } returns null

        val creds = UsernamePasswordCredentials("sherlock", "super-secret")
        val request = HttpRequest.POST("/login", creds)

        val exception = shouldThrow<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
        }
        exception.status shouldBe HttpStatus.UNAUTHORIZED
        exception.message shouldBe "User Not Found"
    }

    @Test
    fun credentialsDoNotMatchUnsuccessfulAuthentication() {
        val mock = getMock(applicationUsers)

        every {
            mock.findByName("sherlock")
        } returns ApplicationUserBuilder.create("sherlock", "super-secret")

        val creds = UsernamePasswordCredentials("sherlock", "wrong-secret")
        val request = HttpRequest.POST("/login", creds)

        val exception = shouldThrow<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
        }
        exception.status shouldBe HttpStatus.UNAUTHORIZED
        exception.message shouldBe "Credentials Do Not Match"
    }

    @MockBean(ApplicationUsers::class)
    fun applicationUsers(): ApplicationUsers = mockk<ApplicationUsers>(relaxed = true)
}