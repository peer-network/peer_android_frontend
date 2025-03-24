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
public final class TokenUsecase_Factory implements Factory<TokenUsecase> {
  private final Provider<TokenRepository> repositoryProvider;

  public TokenUsecase_Factory(Provider<TokenRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TokenUsecase get() {
    return newInstance(repositoryProvider.get());
  }

  public static TokenUsecase_Factory create(Provider<TokenRepository> repositoryProvider) {
    return new TokenUsecase_Factory(repositoryProvider);
  }

  public static TokenUsecase newInstance(TokenRepository repository) {
    return new TokenUsecase(repository);
  }
}
