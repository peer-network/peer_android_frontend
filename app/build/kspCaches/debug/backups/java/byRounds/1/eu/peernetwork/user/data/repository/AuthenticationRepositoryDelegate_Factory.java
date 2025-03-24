package eu.peernetwork.user.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.data.api.AuthenticationApi;
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
public final class AuthenticationRepositoryDelegate_Factory implements Factory<AuthenticationRepositoryDelegate> {
  private final Provider<AuthenticationApi> apiProvider;

  public AuthenticationRepositoryDelegate_Factory(Provider<AuthenticationApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public AuthenticationRepositoryDelegate get() {
    return newInstance(apiProvider.get());
  }

  public static AuthenticationRepositoryDelegate_Factory create(
      Provider<AuthenticationApi> apiProvider) {
    return new AuthenticationRepositoryDelegate_Factory(apiProvider);
  }

  public static AuthenticationRepositoryDelegate newInstance(AuthenticationApi api) {
    return new AuthenticationRepositoryDelegate(api);
  }
}
