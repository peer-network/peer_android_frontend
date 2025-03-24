package eu.peernetwork.user.remote.api;

import com.apollographql.apollo3.ApolloClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class UsernameSettingsApi_Factory implements Factory<UsernameSettingsApi> {
  private final Provider<ApolloClient> clientProvider;

  public UsernameSettingsApi_Factory(Provider<ApolloClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public UsernameSettingsApi get() {
    return newInstance(clientProvider.get());
  }

  public static UsernameSettingsApi_Factory create(Provider<ApolloClient> clientProvider) {
    return new UsernameSettingsApi_Factory(clientProvider);
  }

  public static UsernameSettingsApi newInstance(ApolloClient client) {
    return new UsernameSettingsApi(client);
  }
}
