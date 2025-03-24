package eu.peernetwork.app.module.core;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.app.Peer;
import eu.peernetwork.core.ui.component.UiComponent;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class UiModule_ProvideMainBuilderFactory implements Factory<UiComponent.Builder> {
  private final Provider<Peer.Component> componentProvider;

  public UiModule_ProvideMainBuilderFactory(Provider<Peer.Component> componentProvider) {
    this.componentProvider = componentProvider;
  }

  @Override
  public UiComponent.Builder get() {
    return provideMainBuilder(componentProvider.get());
  }

  public static UiModule_ProvideMainBuilderFactory create(
      Provider<Peer.Component> componentProvider) {
    return new UiModule_ProvideMainBuilderFactory(componentProvider);
  }

  public static UiComponent.Builder provideMainBuilder(Peer.Component component) {
    return Preconditions.checkNotNullFromProvides(UiModule.INSTANCE.provideMainBuilder(component));
  }
}
