package eu.peernetwork.user.remote.provider;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.data.api.SettingsApi;
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
public final class SettingsProviderDelegate_Factory implements Factory<SettingsProviderDelegate> {
  private final Provider<Map<String, SettingsApi<?>>> settingsProvider;

  public SettingsProviderDelegate_Factory(Provider<Map<String, SettingsApi<?>>> settingsProvider) {
    this.settingsProvider = settingsProvider;
  }

  @Override
  public SettingsProviderDelegate get() {
    return newInstance(settingsProvider.get());
  }

  public static SettingsProviderDelegate_Factory create(
      Provider<Map<String, SettingsApi<?>>> settingsProvider) {
    return new SettingsProviderDelegate_Factory(settingsProvider);
  }

  public static SettingsProviderDelegate newInstance(Map<String, SettingsApi<?>> settings) {
    return new SettingsProviderDelegate(settings);
  }
}
