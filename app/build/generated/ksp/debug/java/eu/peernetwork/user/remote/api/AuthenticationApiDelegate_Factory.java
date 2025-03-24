package eu.peernetwork.user.remote.api;

import com.apollographql.apollo3.ApolloClient;
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
public final class AuthenticationApiDelegate_Factory implements Factory<AuthenticationApiDelegate> {
  private final Provider<ApolloClient> clientProvider;

  private final Provider<AuthenticationApi.Listener> listenerProvider;

  public AuthenticationApiDelegate_Factory(Provider<ApolloClient> clientProvider,
      Provider<AuthenticationApi.Listener> listenerProvider) {
    this.clientProvider = clientProvider;
    this.listenerProvider = listenerProvider;
  }

  @Override
  public AuthenticationApiDelegate get() {
    return newInstance(clientProvider.get(), listenerProvider.get());
  }

  public static AuthenticationApiDelegate_Factory create(Provider<ApolloClient> clientProvider,
      Provider<AuthenticationApi.Listener> listenerProvider) {
    return new AuthenticationApiDelegate_Factory(clientProvider, listenerProvider);
  }

  public static AuthenticationApiDelegate newInstance(ApolloClient client,
      AuthenticationApi.Listener listener) {
    return new AuthenticationApiDelegate(client, listener);
  }
}
