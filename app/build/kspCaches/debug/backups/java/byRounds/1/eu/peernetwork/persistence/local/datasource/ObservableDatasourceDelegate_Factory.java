package eu.peernetwork.persistence.local.datasource;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class ObservableDatasourceDelegate_Factory implements Factory<ObservableDatasourceDelegate> {
  private final Provider<SharedPreferences> preferenceProvider;

  public ObservableDatasourceDelegate_Factory(Provider<SharedPreferences> preferenceProvider) {
    this.preferenceProvider = preferenceProvider;
  }

  @Override
  public ObservableDatasourceDelegate get() {
    return newInstance(preferenceProvider.get());
  }

  public static ObservableDatasourceDelegate_Factory create(
      Provider<SharedPreferences> preferenceProvider) {
    return new ObservableDatasourceDelegate_Factory(preferenceProvider);
  }

  public static ObservableDatasourceDelegate newInstance(SharedPreferences preference) {
    return new ObservableDatasourceDelegate(preference);
  }
}
