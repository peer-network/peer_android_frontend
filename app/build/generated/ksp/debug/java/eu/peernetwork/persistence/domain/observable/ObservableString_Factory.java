package eu.peernetwork.persistence.domain.observable;

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
public final class ObservableString_Factory implements Factory<ObservableString> {
  private final Provider<PreferenceRepository> repositoryProvider;

  public ObservableString_Factory(Provider<PreferenceRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ObservableString get() {
    return newInstance(repositoryProvider.get());
  }

  public static ObservableString_Factory create(Provider<PreferenceRepository> repositoryProvider) {
    return new ObservableString_Factory(repositoryProvider);
  }

  public static ObservableString newInstance(PreferenceRepository repository) {
    return new ObservableString(repository);
  }
}
