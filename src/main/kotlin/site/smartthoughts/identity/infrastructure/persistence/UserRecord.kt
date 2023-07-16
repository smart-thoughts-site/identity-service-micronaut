package site.smartthoughts.identity.infrastructure.persistence

import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.*

@Serdeable
@Entity
@Table(name = "users")
class UserRecord (
    @Id
    var id: UUID? = null,
    @NotNull
    var username: String,
    var email: String,
    @NotNull
    @Column(name="salt", columnDefinition = "bpchar(88)", nullable = false, length = 88)
    var salt: ByteArray,
    @NotNull
    @Column(name="password", columnDefinition = "bpchar(88)", nullable = false, length = 88)
    var password: String
)