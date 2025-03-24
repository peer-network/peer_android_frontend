package eu.peernetwork.persistence.domain.publishable;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.persistence.domain.repository.PreferenceRepository;
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
public final class PublishableBoolean_Factory implements Factory<PublishableBoolean> {
  private final Provider<PreferenceRepository> repositoryProvider;

  public PublishableBoolean_Factory(Provider<PreferenceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PublishableBoolean get() {
    return newInstance(repositoryProvider.get());
  }

  public static PublishableBoolean_Factory create(
      Provider<PreferenceRepository> repositoryProvider) {
    return new PublishableBoolean_Factory(repositoryProvider);
  }

  public static PublishableBoolean newInstance(PreferenceRepository repository) {
    return new PublishableBoolean(repository);
  }
}
