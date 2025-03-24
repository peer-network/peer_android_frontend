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
public final class SearchApiDelegate_Factory implements Factory<SearchApiDelegate> {
  private final Provider<ApolloClient> clientProvider;

  public SearchApiDelegate_Factory(Provider<ApolloClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public SearchApiDelegate get() {
    return newInstance(clientProvider.get());
  }

  public static SearchApiDelegate_Factory create(Provider<ApolloClient> clientProvider) {
    return new SearchApiDelegate_Factory(clientProvider);
  }

  public static SearchApiDelegate newInstance(ApolloClient client) {
    return new SearchApiDelegate(client);
  }
}
