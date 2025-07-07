package eu.peernetwork.messaging.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.messaging.data.api.ParticipantsApi
import eu.peernetwork.messaging.domain.model.Participant
import messaging.eu.peernetwork.messaging.remote.AddChatParticipantsMutation
import messaging.eu.peernetwork.messaging.remote.GetParticipantsQuery
import messaging.eu.peernetwork.messaging.remote.RemoveChatParticipantsMutation
import javax.inject.Inject

class ParticipantsApiDelegate @Inject constructor(
    private val client: RequestClient
): ParticipantsApi {
    override suspend fun get(chatId: String): List<Participant> {
        val query = GetParticipantsQuery(
            chatid = chatId,
            offset = Optional.Absent,
            limit = Optional.Absent
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().getChat
        response.assertOrThrow(data.status, data.ResponseCode)

        return data.affectedRows?.flatMap { it.chatparticipants }?.mapNotNull { participant ->
            participant?.let {
                Participant(
                    userId = it.userid,
                    username = it.username,
                    slug = it.slug.toString(),
                    image = it.img
                )
            }
        } ?: emptyList()
    }

    override suspend fun add(chatId: String, recipients: List<String>): List<Participant> {
        val mutation = AddChatParticipantsMutation(
            chatid = chatId,
            recipients = recipients
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().addChatParticipants
        response.assertOrThrow(data.status, data.ResponseCode)

        return get(chatId)
    }

    override suspend fun remove(chatId: String, recipients: List<String>) {
        val mutation = RemoveChatParticipantsMutation(
            recipients = recipients,
            chatid = chatId
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().removeChatParticipants
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
