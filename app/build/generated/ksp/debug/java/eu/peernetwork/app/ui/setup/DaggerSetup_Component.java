package eu.peernetwork.app.ui.setup;

import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import eu.peernetwork.core.ui.factory.UiBuilderFactory_Factory;
import eu.peernetwork.persistence.domain.repository.PreferenceRepository;
import eu.peernetwork.user.domain.repository.AccountRepository;
import eu.peernetwork.user.domain.repository.AuthenticationRepository;
import eu.peernetwork.user.domain.repository.SearchRepository;
import eu.peernetwork.user.domain.repository.TokenRepository;
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
public final class DaggerSetup_Component {
  private DaggerSetup_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Setup setup;

    private Builder() {
    }

    public Builder setup(Setup setup) {
      this.setup = Preconditions.checkNotNull(setup);
      return this;
    }

    public Setup.Component build() {
      Preconditions.checkBuilderRequirement(setup, Setup.class);
      return new ComponentImpl(setup);
    }
  }

  private static final class ComponentImpl implements Setup.Component {
    private final Setup setup;

    private final ComponentImpl componentImpl = this;

    private Provider<Setup.Component> componentProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideLoginBuilderProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideRegistrationBuilderProvider;

    private Provider mapOfClassOfAndProviderOfUiComponentBuilderProvider;

    private Provider<UiBuilderFactory> uiBuilderFactoryProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponentProvider.Factory> provideBuilderFactoryProvider;

    private ComponentImpl(Setup setupParam) {
      this.setup = setupParam;
      initialize(setupParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Setup setupParam) {
      this.componentProvider = InstanceFactory.create((Setup.Component) componentImpl);
      this.provideLoginBuilderProvider = DoubleCheck.provider(SetupModule_ProvideLoginBuilderFactory.create(componentProvider));
      this.provideRegistrationBuilderProvider = DoubleCheck.provider(SetupModule_ProvideRegistrationBuilderFactory.create(componentProvider));
      this.mapOfClassOfAndProviderOfUiComponentBuilderProvider = MapProviderFactory.<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, eu.peernetwork.core.ui.component.UiComponent.Builder>builder(2).put(eu.peernetwork.user.ui.login.Login.Builder.class, provideLoginBuilderProvider).put(eu.peernetwork.user.ui.registeration.Registration.Builder.class, provideRegistrationBuilderProvider).build();
      this.uiBuilderFactoryProvider = UiBuilderFactory_Factory.create(mapOfClassOfAndProviderOfUiComponentBuilderProvider);
      this.provideBuilderFactoryProvider = DoubleCheck.provider(SetupModule_ProvideBuilderFactoryFactory.create(uiBuilderFactoryProvider));
    }

    @Override
    public AccountRepository accountRepository() {
      return Preconditions.checkNotNullFromComponent(setup.accountRepository());
    }

    @Override
    public SearchRepository searchRepository() {
      return Preconditions.checkNotNullFromComponent(setup.searchRepository());
    }

    @Override
    public AuthenticationRepository authenticationRepository() {
      return Preconditions.checkNotNullFromComponent(setup.authenticationRepository());
    }

    @Override
    public TokenRepository tokenRepository() {
      return Preconditions.checkNotNullFromComponent(setup.tokenRepository());
    }

    @Override
    public PreferenceRepository preferenceRepository() {
      return Preconditions.checkNotNullFromComponent(setup.preferenceRepository());
    }

    @Override
    public eu.peernetwork.core.ui.component.UiComponentProvider.Factory factory() {
      return provideBuilderFactoryProvider.get();
    }
  }
}
