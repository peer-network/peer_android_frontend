package eu.peernetwork.app.ui.home;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponent;
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
public final class HomeModule_ProvideFeedBuilderFactory implements Factory<UiComponent.Builder> {
  private final Provider<Home.Component> componentProvider;

  public HomeModule_ProvideFeedBuilderFactory(Provider<Home.Component> componentProvider) {
    this.componentProvider = componentProvider;
  }

  @Override
  public UiComponent.Builder get() {
    return provideFeedBuilder(componentProvider.get());
  }

  public static HomeModule_ProvideFeedBuilderFactory create(
      Provider<Home.Component> componentProvider) {
    return new HomeModule_ProvideFeedBuilderFactory(componentProvider);
  }

  public static UiComponent.Builder provideFeedBuilder(Home.Component component) {
    return Preconditions.checkNotNullFromProvides(HomeModule.INSTANCE.provideFeedBuilder(component));
  }
}
