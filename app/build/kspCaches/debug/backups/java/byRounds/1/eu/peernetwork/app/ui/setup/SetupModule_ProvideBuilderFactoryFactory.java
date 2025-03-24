package eu.peernetwork.app.ui.setup;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponentProvider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
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
public final class SetupModule_ProvideBuilderFactoryFactory implements Factory<UiComponentProvider.Factory> {
  private final Provider<UiBuilderFactory> factoryProvider;

  public SetupModule_ProvideBuilderFactoryFactory(Provider<UiBuilderFactory> factoryProvider) {
    this.factoryProvider = factoryProvider;
  }

  @Override
  public UiComponentProvider.Factory get() {
    return provideBuilderFactory(factoryProvider.get());
  }

  public static SetupModule_ProvideBuilderFactoryFactory create(
      Provider<UiBuilderFactory> factoryProvider) {
    return new SetupModule_ProvideBuilderFactoryFactory(factoryProvider);
  }

  public static UiComponentProvider.Factory provideBuilderFactory(UiBuilderFactory factory) {
    return Preconditions.checkNotNullFromProvides(SetupModule.INSTANCE.provideBuilderFactory(factory));
  }
}
