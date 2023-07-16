package site.smartthoughts.identity.infrastructure.resources

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
class HomeResource {

    @Produces(TEXT_PLAIN)
    @Get
    fun index(principal: Principal): String = principal.name
}