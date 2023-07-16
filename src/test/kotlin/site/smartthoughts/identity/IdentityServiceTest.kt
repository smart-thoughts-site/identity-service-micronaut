package site.smartthoughts.identity
import io.kotest.core.spec.style.AnnotationSpec
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import jakarta.inject.Inject

@MicronautTest
class IdentityServiceTest : AnnotationSpec() {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }
}
