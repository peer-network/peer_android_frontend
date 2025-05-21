package eu.peernetwork.core.remote.api

import com.apollographql.apollo3.ApolloClient

interface RequestClient {
    operator fun invoke(): ApolloClient
}
