package eu.peernetwork.user.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.repository.TokenRepository;
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
public final class TokenObserverUsecase_Factory implements Factory<TokenObserverUsecase> {
  private final Provider<TokenRepository> repositoryProvider;

  public TokenObserverUsecase_Factory(Provider<TokenRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TokenObserverUsecase get() {
    return newInstance(repositoryProvider.get());
  }

  public static TokenObserverUsecase_Factory create(Provider<TokenRepository> repositoryProvider) {
    return new TokenObserverUsecase_Factory(repositoryProvider);
  }

  public static TokenObserverUsecase newInstance(TokenRepository repository) {
    return new TokenObserverUsecase(repository);
  }
}
