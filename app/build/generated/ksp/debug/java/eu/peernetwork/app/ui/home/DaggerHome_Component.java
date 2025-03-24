package eu.peernetwork.app.ui.home;

import androidx.lifecycle.ViewModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import eu.peernetwork.core.ui.factory.UiBuilderFactory_Factory;
import eu.peernetwork.persistence.domain.publishable.PublishableInteger;
import eu.peernetwork.persistence.domain.publishable.PublishableInteger_Factory;
import eu.peernetwork.persistence.domain.repository.PreferenceRepository;
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger;
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger_Factory;
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
public final class DaggerHome_Component {
  private DaggerHome_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Home home;

    private Builder() {
    }

    public Builder home(Home home) {
      this.home = Preconditions.checkNotNull(home);
      return this;
    }

    public Home.Component build() {
      Preconditions.checkBuilderRequirement(home, Home.class);
      return new ComponentImpl(home);
    }
  }

  private static final class ComponentImpl implements Home.Component {
    private final Home home;

    private final ComponentImpl componentImpl = this;

    private Provider<Home.Component> componentProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideFeedBuilderProvider;

    private Provider mapOfClassOfAndProviderOfUiComponentBuilderProvider;

    private Provider<UiBuilderFactory> uiBuilderFactoryProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponentProvider.Factory> provideBuilderFactoryProvider;

    private Provider<PreferenceRepository> preferenceRepositoryProvider;

    private Provider<RetrievableInteger> retrievableIntegerProvider;

    private Provider<PublishableInteger> publishableIntegerProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<ViewModel> viewModelProvider;

    private Provider mapOfClassOfAndProviderOfViewModelProvider;

    private Provider<androidx.lifecycle.ViewModelProvider.Factory> provideViewModelFactoryProvider;

    private ComponentImpl(Home homeParam) {
      this.home = homeParam;
      initialize(homeParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Home homeParam) {
      this.componentProvider = InstanceFactory.create((Home.Component) componentImpl);
      this.provideFeedBuilderProvider = DoubleCheck.provider(HomeModule_ProvideFeedBuilderFactory.create(componentProvider));
      this.mapOfClassOfAndProviderOfUiComponentBuilderProvider = MapProviderFactory.<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, eu.peernetwork.core.ui.component.UiComponent.Builder>builder(1).put(eu.peernetwork.social.ui.feed.Feed.Builder.class, provideFeedBuilderProvider).build();
      this.uiBuilderFactoryProvider = UiBuilderFactory_Factory.create(mapOfClassOfAndProviderOfUiComponentBuilderProvider);
      this.provideBuilderFactoryProvider = DoubleCheck.provider(HomeModule_ProvideBuilderFactoryFactory.create(uiBuilderFactoryProvider));
      this.preferenceRepositoryProvider = new PreferenceRepositoryProvider(homeParam);
      this.retrievableIntegerProvider = RetrievableInteger_Factory.create(preferenceRepositoryProvider);
      this.publishableIntegerProvider = PublishableInteger_Factory.create(preferenceRepositoryProvider);
      this.homeViewModelProvider = DoubleCheck.provider(HomeViewModel_Factory.create(retrievableIntegerProvider, publishableIntegerProvider));
      this.viewModelProvider = DoubleCheck.provider(HomeModule_ViewModelFactory.create(homeViewModelProvider));
      this.mapOfClassOfAndProviderOfViewModelProvider = MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(1).put(HomeViewModel.class, viewModelProvider).build();
      this.provideViewModelFactoryProvider = DoubleCheck.provider(HomeModule_ProvideViewModelFactoryFactory.create(mapOfClassOfAndProviderOfViewModelProvider));
    }

    @Override
    public PreferenceRepository preferenceRepository() {
      return Preconditions.checkNotNullFromComponent(home.preferenceRepository());
    }

    @Override
    public eu.peernetwork.core.ui.component.UiComponentProvider.Factory factory() {
      return provideBuilderFactoryProvider.get();
    }

    @Override
    public androidx.lifecycle.ViewModelProvider.Factory viewModelFactory() {
      return provideViewModelFactoryProvider.get();
    }

    private static final class PreferenceRepositoryProvider implements Provider<PreferenceRepository> {
      private final Home home;

      PreferenceRepositoryProvider(Home home) {
        this.home = home;
      }

      @Override
      public PreferenceRepository get() {
        return Preconditions.checkNotNullFromComponent(home.preferenceRepository());
      }
    }
  }
}
