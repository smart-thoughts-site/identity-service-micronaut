package site.smartthoughts.identity.infrastructure.authentication

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import site.smartthoughts.identity.application.ApplicationUsers
import site.smartthoughts.identity.application.PasswordMatcher

@Singleton
class AuthenticationProviderUserPassword(
    private val applicationUsers: ApplicationUsers,
    private val passwordMatcher: PasswordMatcher) : AuthenticationProvider<HttpRequest<*>> {

    override fun authenticate(httpRequest: HttpRequest<*>?,
                              authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            performAuthentication(authenticationRequest, emitter)
        }, FluxSink.OverflowStrategy.ERROR)
    }

    private fun performAuthentication(
        authenticationRequest: AuthenticationRequest<*, *>,
        emitter: FluxSink<AuthenticationResponse>
    ) {
        val username = authenticationRequest.identity
        val providedPassword = authenticationRequest.secret

        if (username is String && providedPassword is String) {
            val user = applicationUsers.findByName(username)

            val response = if (user == null) {
                AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND)
            } else if (!passwordMatcher.matches(user.passwordHash, user.saltHash, providedPassword)) {
                AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
            } else {
                AuthenticationResponse.success(username)
            }
            emitter.next(response)
            emitter.complete()
            return
        }
        emitter.error(AuthenticationResponse.exception())
    }
}