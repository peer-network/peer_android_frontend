package eu.peernetwork.app.ui.home;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.persistence.domain.publishable.PublishableInteger;
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.app.ui.home.Home.Scope")
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<RetrievableInteger> retrievableIntegerProvider;

  private final Provider<PublishableInteger> publishableIntegerProvider;

  public HomeViewModel_Factory(Provider<RetrievableInteger> retrievableIntegerProvider,
      Provider<PublishableInteger> publishableIntegerProvider) {
    this.retrievableIntegerProvider = retrievableIntegerProvider;
    this.publishableIntegerProvider = publishableIntegerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(retrievableIntegerProvider.get(), publishableIntegerProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<RetrievableInteger> retrievableIntegerProvider,
      Provider<PublishableInteger> publishableIntegerProvider) {
    return new HomeViewModel_Factory(retrievableIntegerProvider, publishableIntegerProvider);
  }

  public static HomeViewModel newInstance(RetrievableInteger retrievableInteger,
      PublishableInteger publishableInteger) {
    return new HomeViewModel(retrievableInteger, publishableInteger);
  }
}
