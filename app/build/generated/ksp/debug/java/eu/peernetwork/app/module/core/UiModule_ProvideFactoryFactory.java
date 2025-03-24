package eu.peernetwork.app.module.core;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponentProvider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class UiModule_ProvideFactoryFactory implements Factory<UiComponentProvider.Factory> {
  private final Provider<UiBuilderFactory> factoryProvider;

  public UiModule_ProvideFactoryFactory(Provider<UiBuilderFactory> factoryProvider) {
    this.factoryProvider = factoryProvider;
  }

  @Override
  public UiComponentProvider.Factory get() {
    return provideFactory(factoryProvider.get());
  }

  public static UiModule_ProvideFactoryFactory create(Provider<UiBuilderFactory> factoryProvider) {
    return new UiModule_ProvideFactoryFactory(factoryProvider);
  }

  public static UiComponentProvider.Factory provideFactory(UiBuilderFactory factory) {
    return Preconditions.checkNotNullFromProvides(UiModule.INSTANCE.provideFactory(factory));
  }
}
