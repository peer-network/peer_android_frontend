package eu.peernetwork.persistence.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.persistence.data.datasource.ObservableDatasource;
import eu.peernetwork.persistence.data.datasource.PublishableDatasource;
import eu.peernetwork.persistence.data.datasource.RetrievableDatasource;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class PreferenceRepositoryDelegate_Factory implements Factory<PreferenceRepositoryDelegate> {
  private final Provider<ObservableDatasource> observableProvider;

  private final Provider<PublishableDatasource> publishableProvider;

  private final Provider<RetrievableDatasource> retrievableProvider;

  public PreferenceRepositoryDelegate_Factory(Provider<ObservableDatasource> observableProvider,
      Provider<PublishableDatasource> publishableProvider,
      Provider<RetrievableDatasource> retrievableProvider) {
    this.observableProvider = observableProvider;
    this.publishableProvider = publishableProvider;
    this.retrievableProvider = retrievableProvider;
  }

  @Override
  public PreferenceRepositoryDelegate get() {
    return newInstance(observableProvider.get(), publishableProvider.get(), retrievableProvider.get());
  }

  public static PreferenceRepositoryDelegate_Factory create(
      Provider<ObservableDatasource> observableProvider,
      Provider<PublishableDatasource> publishableProvider,
      Provider<RetrievableDatasource> retrievableProvider) {
    return new PreferenceRepositoryDelegate_Factory(observableProvider, publishableProvider, retrievableProvider);
  }

  public static PreferenceRepositoryDelegate newInstance(ObservableDatasource observable,
      PublishableDatasource publishable, RetrievableDatasource retrievable) {
    return new PreferenceRepositoryDelegate(observable, publishable, retrievable);
  }
}
