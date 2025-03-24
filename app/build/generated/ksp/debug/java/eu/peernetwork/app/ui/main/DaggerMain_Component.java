package eu.peernetwork.app.ui.main;

import androidx.lifecycle.ViewModel;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import eu.peernetwork.core.ui.factory.UiBuilderFactory_Factory;
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean;
import eu.peernetwork.persistence.domain.publishable.PublishableBoolean_Factory;
import eu.peernetwork.persistence.domain.repository.PreferenceRepository;
import eu.peernetwork.persistence.domain.retrievable.RetrievableBoolean;
import eu.peernetwork.persistence.domain.retrievable.RetrievableBoolean_Factory;
import eu.peernetwork.user.domain.repository.AccountRepository;
import eu.peernetwork.user.domain.repository.AuthenticationRepository;
import eu.peernetwork.user.domain.repository.SearchRepository;
import eu.peernetwork.user.domain.repository.TokenRepository;
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase;
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase_Factory;
import javax.annotation.processing.Generated;

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
public final class DaggerMain_Component {
  private DaggerMain_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Main main;

    private Builder() {
    }

    public Builder main(Main main) {
      this.main = Preconditions.checkNotNull(main);
      return this;
    }

    public Main.Component build() {
      Preconditions.checkBuilderRequirement(main, Main.class);
      return new ComponentImpl(main);
    }
  }

  private static final class ComponentImpl implements Main.Component {
    private final Main main;

    private final ComponentImpl componentImpl = this;

    private Provider<TokenRepository> tokenRepositoryProvider;

    private Provider<TokenObserverUsecase> tokenObserverUsecaseProvider;

    private Provider<PreferenceRepository> preferenceRepositoryProvider;

    private Provider<RetrievableBoolean> retrievableBooleanProvider;

    private Provider<PublishableBoolean> publishableBooleanProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<ViewModel> viewModelProvider;

    private Provider mapOfClassOfAndProviderOfViewModelProvider;

    private Provider<androidx.lifecycle.ViewModelProvider.Factory> provideViewModelFactoryProvider;

    private Provider<Main.Component> componentProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideHomeBuilderProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideSetupBuilderProvider;

    private Provider mapOfClassOfAndProviderOfUiComponentBuilderProvider;

    private Provider<UiBuilderFactory> uiBuilderFactoryProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponentProvider.Factory> provideBuilderFactoryProvider;

    private ComponentImpl(Main mainParam) {
      this.main = mainParam;
      initialize(mainParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Main mainParam) {
      this.tokenRepositoryProvider = new TokenRepositoryProvider(mainParam);
      this.tokenObserverUsecaseProvider = TokenObserverUsecase_Factory.create(tokenRepositoryProvider);
      this.preferenceRepositoryProvider = new PreferenceRepositoryProvider(mainParam);
      this.retrievableBooleanProvider = RetrievableBoolean_Factory.create(preferenceRepositoryProvider);
      this.publishableBooleanProvider = PublishableBoolean_Factory.create(preferenceRepositoryProvider);
      this.mainViewModelProvider = DoubleCheck.provider(MainViewModel_Factory.create(tokenObserverUsecaseProvider, retrievableBooleanProvider, publishableBooleanProvider));
      this.viewModelProvider = DoubleCheck.provider(MainModule_ViewModelFactory.create(mainViewModelProvider));
      this.mapOfClassOfAndProviderOfViewModelProvider = MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(1).put(MainViewModel.class, viewModelProvider).build();
      this.provideViewModelFactoryProvider = DoubleCheck.provider(MainModule_ProvideViewModelFactoryFactory.create(mapOfClassOfAndProviderOfViewModelProvider));
      this.componentProvider = InstanceFactory.create((Main.Component) componentImpl);
      this.provideHomeBuilderProvider = DoubleCheck.provider(MainModule_ProvideHomeBuilderFactory.create(componentProvider));
      this.provideSetupBuilderProvider = DoubleCheck.provider(MainModule_ProvideSetupBuilderFactory.create(componentProvider));
      this.mapOfClassOfAndProviderOfUiComponentBuilderProvider = MapProviderFactory.<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, eu.peernetwork.core.ui.component.UiComponent.Builder>builder(2).put(eu.peernetwork.app.ui.home.Home.Builder.class, provideHomeBuilderProvider).put(eu.peernetwork.app.ui.setup.Setup.Builder.class, provideSetupBuilderProvider).build();
      this.uiBuilderFactoryProvider = UiBuilderFactory_Factory.create(mapOfClassOfAndProviderOfUiComponentBuilderProvider);
      this.provideBuilderFactoryProvider = DoubleCheck.provider(MainModule_ProvideBuilderFactoryFactory.create(uiBuilderFactoryProvider));
    }

    @Override
    public AccountRepository accountRepository() {
      return Preconditions.checkNotNullFromComponent(main.accountRepository());
    }

    @Override
    public SearchRepository searchRepository() {
      return Preconditions.checkNotNullFromComponent(main.searchRepository());
    }

    @Override
    public AuthenticationRepository authenticationRepository() {
      return Preconditions.checkNotNullFromComponent(main.authenticationRepository());
    }

    @Override
    public TokenRepository tokenRepository() {
      return Preconditions.checkNotNullFromComponent(main.tokenRepository());
    }

    @Override
    public PreferenceRepository preferenceRepository() {
      return Preconditions.checkNotNullFromComponent(main.preferenceRepository());
    }

    @Override
    public void inject(MainActivity p0) {
      injectMainActivity(p0);
    }

    @Override
    public eu.peernetwork.core.ui.component.UiComponentProvider.Factory factory() {
      return provideBuilderFactoryProvider.get();
    }

    @Override
    public androidx.lifecycle.ViewModelProvider.Factory viewModelFactory() {
      return provideViewModelFactoryProvider.get();
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity(MainActivity instance) {
      MainActivity_MembersInjector.injectFactory(instance, provideViewModelFactoryProvider.get());
      return instance;
    }

    private static final class TokenRepositoryProvider implements Provider<TokenRepository> {
      private final Main main;

      TokenRepositoryProvider(Main main) {
        this.main = main;
      }

      @Override
      public TokenRepository get() {
        return Preconditions.checkNotNullFromComponent(main.tokenRepository());
      }
    }

    private static final class PreferenceRepositoryProvider implements Provider<PreferenceRepository> {
      private final Main main;

      PreferenceRepositoryProvider(Main main) {
        this.main = main;
      }

      @Override
      public PreferenceRepository get() {
        return Preconditions.checkNotNullFromComponent(main.preferenceRepository());
      }
    }
  }
}
