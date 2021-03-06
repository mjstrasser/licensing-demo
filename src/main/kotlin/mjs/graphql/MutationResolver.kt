package mjs.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import mjs.commands.AddDocumentCommand
import mjs.commands.CreateApplicationCommand
import mjs.commands.SetApplicantCommand
import mjs.projection.ApplicationView
import mjs.shared.NumberGenerator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class MutationResolver(
    @Autowired val commandGateway: CommandGateway,
    @Autowired val queryResolver: QueryResolver,
    @Autowired val numberGenerator: NumberGenerator
) : GraphQLMutationResolver {

    fun createApplication(applicationId: UUID, type: String, createTimestamp: Instant): ApplicationView {
        val number = numberGenerator.nextNumber()
        commandGateway.send<Void>(CreateApplicationCommand(applicationId, number, type, createTimestamp))
        return ApplicationView(applicationId, number, type, createTimestamp)
    }

    fun addDocument(applicationId: UUID, id: UUID, type: String, contents: String): ApplicationView? {
        commandGateway.send<Void>(AddDocumentCommand(applicationId, id, type, contents))
        return queryResolver.getApplicationById(applicationId)
    }

    fun setApplicant(applicationId: UUID, id: UUID, firstName: String, lastName: String, email: String): ApplicationView? {
        commandGateway.send<Void>(SetApplicantCommand(applicationId, id, firstName, lastName, email))
        return queryResolver.getApplicationById(applicationId)
    }
}
