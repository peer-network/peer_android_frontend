package eu.peernetwork.app.ui.main;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean;
import eu.peernetwork.persistence.domain.retrievable.RetrievableBoolean;
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.app.ui.main.Main.Scope")
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<TokenObserverUsecase> tokenObserverUsecaseProvider;

  private final Provider<RetrievableBoolean> retrievableBooleanProvider;

  private final Provider<PublishableBoolean> publishableBooleanProvider;

  public MainViewModel_Factory(Provider<TokenObserverUsecase> tokenObserverUsecaseProvider,
      Provider<RetrievableBoolean> retrievableBooleanProvider,
      Provider<PublishableBoolean> publishableBooleanProvider) {
    this.tokenObserverUsecaseProvider = tokenObserverUsecaseProvider;
    this.retrievableBooleanProvider = retrievableBooleanProvider;
    this.publishableBooleanProvider = publishableBooleanProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(tokenObserverUsecaseProvider.get(), retrievableBooleanProvider.get(), publishableBooleanProvider.get());
  }

  public static MainViewModel_Factory create(
      Provider<TokenObserverUsecase> tokenObserverUsecaseProvider,
      Provider<RetrievableBoolean> retrievableBooleanProvider,
      Provider<PublishableBoolean> publishableBooleanProvider) {
    return new MainViewModel_Factory(tokenObserverUsecaseProvider, retrievableBooleanProvider, publishableBooleanProvider);
  }

  public static MainViewModel newInstance(TokenObserverUsecase tokenObserverUsecase,
      RetrievableBoolean retrievableBoolean, PublishableBoolean publishableBoolean) {
    return new MainViewModel(tokenObserverUsecase, retrievableBoolean, publishableBoolean);
  }
}
