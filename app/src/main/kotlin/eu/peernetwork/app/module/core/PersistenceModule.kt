package eu.peernetwork.app.module.core

import dagger.Binds
import dagger.Module
import eu.peernetwork.persistence.data.repository.PreferenceRepositoryDelegate
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import eu.peernetwork.persistence.local.datasource.ObservableDatasourceDelegate
import eu.peernetwork.persistence.local.datasource.PublishableDatasourceDelegate

@Module
internal interface PersistenceModule {
    @Binds
    fun bindPreferenceRepository(delegate: PreferenceRepositoryDelegate): PreferenceRepository

    @Binds
    fun bindPublishableDatasource(delegate: PublishableDatasourceDelegate): PublishableDatasource

    @Binds
    fun bindObservableDatasource(delegate: ObservableDatasourceDelegate): ObservableDatasource
}
