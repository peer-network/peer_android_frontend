package eu.peernetwork.core.remote.provider

import com.apollographql.apollo3.ApolloClient

interface NetworkProvider {
    fun client(): ApolloClient
}
