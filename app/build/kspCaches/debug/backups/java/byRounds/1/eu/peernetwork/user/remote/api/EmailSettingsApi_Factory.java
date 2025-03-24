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
public final class EmailSettingsApi_Factory implements Factory<EmailSettingsApi> {
  private final Provider<ApolloClient> clientProvider;

  public EmailSettingsApi_Factory(Provider<ApolloClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public EmailSettingsApi get() {
    return newInstance(clientProvider.get());
  }

  public static EmailSettingsApi_Factory create(Provider<ApolloClient> clientProvider) {
    return new EmailSettingsApi_Factory(clientProvider);
  }

  public static EmailSettingsApi newInstance(ApolloClient client) {
    return new EmailSettingsApi(client);
  }
}
