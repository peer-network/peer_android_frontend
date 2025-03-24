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
public final class AccountApiDelegate_Factory implements Factory<AccountApiDelegate> {
  private final Provider<ApolloClient> clientProvider;

  public AccountApiDelegate_Factory(Provider<ApolloClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public AccountApiDelegate get() {
    return newInstance(clientProvider.get());
  }

  public static AccountApiDelegate_Factory create(Provider<ApolloClient> clientProvider) {
    return new AccountApiDelegate_Factory(clientProvider);
  }

  public static AccountApiDelegate newInstance(ApolloClient client) {
    return new AccountApiDelegate(client);
  }
}
