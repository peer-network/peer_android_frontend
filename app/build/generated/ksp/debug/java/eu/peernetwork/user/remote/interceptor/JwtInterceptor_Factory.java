package eu.peernetwork.user.remote.interceptor;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.usecase.TokenUsecase;
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
public final class JwtInterceptor_Factory implements Factory<JwtInterceptor> {
  private final Provider<TokenUsecase> tokenUsecaseProvider;

  public JwtInterceptor_Factory(Provider<TokenUsecase> tokenUsecaseProvider) {
    this.tokenUsecaseProvider = tokenUsecaseProvider;
  }

  @Override
  public JwtInterceptor get() {
    return newInstance(tokenUsecaseProvider.get());
  }

  public static JwtInterceptor_Factory create(Provider<TokenUsecase> tokenUsecaseProvider) {
    return new JwtInterceptor_Factory(tokenUsecaseProvider);
  }

  public static JwtInterceptor newInstance(TokenUsecase tokenUsecase) {
    return new JwtInterceptor(tokenUsecase);
  }
}
