package eu.peernetwork.user.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.data.api.SearchApi;
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
public final class SearchRepositoryDelegate_Factory implements Factory<SearchRepositoryDelegate> {
  private final Provider<SearchApi> apiProvider;

  public SearchRepositoryDelegate_Factory(Provider<SearchApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public SearchRepositoryDelegate get() {
    return newInstance(apiProvider.get());
  }

  public static SearchRepositoryDelegate_Factory create(Provider<SearchApi> apiProvider) {
    return new SearchRepositoryDelegate_Factory(apiProvider);
  }

  public static SearchRepositoryDelegate newInstance(SearchApi api) {
    return new SearchRepositoryDelegate(api);
  }
}
