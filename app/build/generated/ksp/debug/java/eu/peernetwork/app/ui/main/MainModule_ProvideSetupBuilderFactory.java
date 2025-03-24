package eu.peernetwork.app.ui.main;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponent;
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
public final class MainModule_ProvideSetupBuilderFactory implements Factory<UiComponent.Builder> {
  private final Provider<Main.Component> componentProvider;

  public MainModule_ProvideSetupBuilderFactory(Provider<Main.Component> componentProvider) {
    this.componentProvider = componentProvider;
  }

  @Override
  public UiComponent.Builder get() {
    return provideSetupBuilder(componentProvider.get());
  }

  public static MainModule_ProvideSetupBuilderFactory create(
      Provider<Main.Component> componentProvider) {
    return new MainModule_ProvideSetupBuilderFactory(componentProvider);
  }

  public static UiComponent.Builder provideSetupBuilder(Main.Component component) {
    return Preconditions.checkNotNullFromProvides(MainModule.INSTANCE.provideSetupBuilder(component));
  }
}
