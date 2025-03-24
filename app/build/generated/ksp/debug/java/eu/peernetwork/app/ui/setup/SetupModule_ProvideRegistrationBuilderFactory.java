package eu.peernetwork.app.ui.setup;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponent;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.app.ui.setup.Setup.Scope")
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
public final class SetupModule_ProvideRegistrationBuilderFactory implements Factory<UiComponent.Builder> {
  private final Provider<Setup.Component> componentProvider;

  public SetupModule_ProvideRegistrationBuilderFactory(
      Provider<Setup.Component> componentProvider) {
    this.componentProvider = componentProvider;
  }

  @Override
  public UiComponent.Builder get() {
    return provideRegistrationBuilder(componentProvider.get());
  }

  public static SetupModule_ProvideRegistrationBuilderFactory create(
      Provider<Setup.Component> componentProvider) {
    return new SetupModule_ProvideRegistrationBuilderFactory(componentProvider);
  }

  public static UiComponent.Builder provideRegistrationBuilder(Setup.Component component) {
    return Preconditions.checkNotNullFromProvides(SetupModule.INSTANCE.provideRegistrationBuilder(component));
  }
}
