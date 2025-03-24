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
public final class RetrievableBoolean_Factory implements Factory<RetrievableBoolean> {
  private final Provider<PreferenceRepository> repositoryProvider;

  public RetrievableBoolean_Factory(Provider<PreferenceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RetrievableBoolean get() {
    return newInstance(repositoryProvider.get());
  }

  public static RetrievableBoolean_Factory create(
      Provider<PreferenceRepository> repositoryProvider) {
    return new RetrievableBoolean_Factory(repositoryProvider);
  }

  public static RetrievableBoolean newInstance(PreferenceRepository repository) {
    return new RetrievableBoolean(repository);
  }
}
