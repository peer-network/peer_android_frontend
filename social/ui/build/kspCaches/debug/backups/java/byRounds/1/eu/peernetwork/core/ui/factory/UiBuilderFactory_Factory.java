package eu.peernetwork.core.ui.factory;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponent;
import java.util.Map;
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
public final class UiBuilderFactory_Factory implements Factory<UiBuilderFactory> {
  private final Provider<Map<Class<? extends UiComponent.Builder>, Provider<UiComponent.Builder>>> factoryProvider;

  public UiBuilderFactory_Factory(
      Provider<Map<Class<? extends UiComponent.Builder>, Provider<UiComponent.Builder>>> factoryProvider) {
    this.factoryProvider = factoryProvider;
  }

  @Override
  public UiBuilderFactory get() {
    return newInstance(factoryProvider.get());
  }

  public static UiBuilderFactory_Factory create(
      Provider<Map<Class<? extends UiComponent.Builder>, Provider<UiComponent.Builder>>> factoryProvider) {
    return new UiBuilderFactory_Factory(factoryProvider);
  }

  public static UiBuilderFactory newInstance(
      Map<Class<? extends UiComponent.Builder>, Provider<UiComponent.Builder>> factory) {
    return new UiBuilderFactory(factory);
  }
}
