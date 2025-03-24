package eu.peernetwork.user.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.data.api.AccountApi;
import eu.peernetwork.user.data.provider.SettingsProvider;
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
public final class AccountRepositoryDelegate_Factory implements Factory<AccountRepositoryDelegate> {
  private final Provider<AccountApi> apiProvider;

  private final Provider<SettingsProvider> providerProvider;

  public AccountRepositoryDelegate_Factory(Provider<AccountApi> apiProvider,
      Provider<SettingsProvider> providerProvider) {
    this.apiProvider = apiProvider;
    this.providerProvider = providerProvider;
  }

  @Override
  public AccountRepositoryDelegate get() {
    return newInstance(apiProvider.get(), providerProvider.get());
  }

  public static AccountRepositoryDelegate_Factory create(Provider<AccountApi> apiProvider,
      Provider<SettingsProvider> providerProvider) {
    return new AccountRepositoryDelegate_Factory(apiProvider, providerProvider);
  }

  public static AccountRepositoryDelegate newInstance(AccountApi api, SettingsProvider provider) {
    return new AccountRepositoryDelegate(api, provider);
  }
}
