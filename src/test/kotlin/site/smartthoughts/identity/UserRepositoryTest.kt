package site.smartthoughts.identity

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.context.BeanContext
import io.micronaut.data.annotation.Query
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import site.smartthoughts.identity.infrastructure.persistence.UserRepository

@MicronautTest
class UserRepositoryTest(private val beanContext: BeanContext) : AnnotationSpec() {

    @Test
    fun testAnnotationMetadata() {
        val query = beanContext.getBeanDefinition(UserRepository::class.java)
            .getRequiredMethod<Any>("findByUsername", String::class.java)
            .annotationMetadata
            .stringValue(Query::class.java)
            .orElse(null)

        query shouldBe "SELECT userRecord_ FROM site.smartthoughts.identity.infrastructure.persistence.UserRecord AS userRecord_ WHERE (userRecord_.username = :p1)"
    }
}