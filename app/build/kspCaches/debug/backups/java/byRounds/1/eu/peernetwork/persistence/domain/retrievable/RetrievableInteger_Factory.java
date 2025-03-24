package eu.peernetwork.persistence.domain.retrievable;

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
public final class RetrievableInteger_Factory implements Factory<RetrievableInteger> {
  private final Provider<PreferenceRepository> repositoryProvider;

  public RetrievableInteger_Factory(Provider<PreferenceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RetrievableInteger get() {
    return newInstance(repositoryProvider.get());
  }

  public static RetrievableInteger_Factory create(
      Provider<PreferenceRepository> repositoryProvider) {
    return new RetrievableInteger_Factory(repositoryProvider);
  }

  public static RetrievableInteger newInstance(PreferenceRepository repository) {
    return new RetrievableInteger(repository);
  }
}
