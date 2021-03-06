package mjs.projection

import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "applications")
class ApplicationView(
    @Id
    val id: UUID,
    val number: Int,
    val type: String,
    val createTimestamp: Instant
) {

    @javax.persistence.Transient
    lateinit var documents: List<DocumentView>

    @javax.persistence.Transient
    var applicant: ApplicantView? = null
}
