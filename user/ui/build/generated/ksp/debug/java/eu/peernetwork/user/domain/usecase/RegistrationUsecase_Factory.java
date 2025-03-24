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
public final class RegistrationUsecase_Factory implements Factory<RegistrationUsecase> {
  private final Provider<AccountRepository> repositoryProvider;

  public RegistrationUsecase_Factory(Provider<AccountRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RegistrationUsecase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RegistrationUsecase_Factory create(Provider<AccountRepository> repositoryProvider) {
    return new RegistrationUsecase_Factory(repositoryProvider);
  }

  public static RegistrationUsecase newInstance(AccountRepository repository) {
    return new RegistrationUsecase(repository);
  }
}
