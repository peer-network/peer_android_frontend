package eu.peernetwork.user.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.repository.AuthenticationRepository;
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
public final class LoginUsecase_Factory implements Factory<LoginUsecase> {
  private final Provider<AuthenticationRepository> repositoryProvider;

  public LoginUsecase_Factory(Provider<AuthenticationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LoginUsecase get() {
    return newInstance(repositoryProvider.get());
  }

  public static LoginUsecase_Factory create(Provider<AuthenticationRepository> repositoryProvider) {
    return new LoginUsecase_Factory(repositoryProvider);
  }

  public static LoginUsecase newInstance(AuthenticationRepository repository) {
    return new LoginUsecase(repository);
  }
}
