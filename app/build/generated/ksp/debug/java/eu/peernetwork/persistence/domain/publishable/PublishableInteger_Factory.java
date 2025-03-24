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
public final class PublishableInteger_Factory implements Factory<PublishableInteger> {
  private final Provider<PreferenceRepository> repositoryProvider;

  public PublishableInteger_Factory(Provider<PreferenceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PublishableInteger get() {
    return newInstance(repositoryProvider.get());
  }

  public static PublishableInteger_Factory create(
      Provider<PreferenceRepository> repositoryProvider) {
    return new PublishableInteger_Factory(repositoryProvider);
  }

  public static PublishableInteger newInstance(PreferenceRepository repository) {
    return new PublishableInteger(repository);
  }
}
