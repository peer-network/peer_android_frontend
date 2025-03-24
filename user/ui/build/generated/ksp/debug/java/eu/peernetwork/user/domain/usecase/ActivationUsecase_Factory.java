package eu.peernetwork.user.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.repository.AccountRepository;
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
public final class ActivationUsecase_Factory implements Factory<ActivationUsecase> {
  private final Provider<AccountRepository> repositoryProvider;

  public ActivationUsecase_Factory(Provider<AccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ActivationUsecase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ActivationUsecase_Factory create(Provider<AccountRepository> repositoryProvider) {
    return new ActivationUsecase_Factory(repositoryProvider);
  }

  public static ActivationUsecase newInstance(AccountRepository repository) {
    return new ActivationUsecase(repository);
  }
}
