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
public final class BiographySettingsApi_Factory implements Factory<BiographySettingsApi> {
  private final Provider<ApolloClient> clientProvider;

  public BiographySettingsApi_Factory(Provider<ApolloClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public BiographySettingsApi get() {
    return newInstance(clientProvider.get());
  }

  public static BiographySettingsApi_Factory create(Provider<ApolloClient> clientProvider) {
    return new BiographySettingsApi_Factory(clientProvider);
  }

  public static BiographySettingsApi newInstance(ApolloClient client) {
    return new BiographySettingsApi(client);
  }
}
