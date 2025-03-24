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
public final class RetrievableDatasourceDelegate_Factory implements Factory<RetrievableDatasourceDelegate> {
  private final Provider<SharedPreferences> preferenceProvider;

  public RetrievableDatasourceDelegate_Factory(Provider<SharedPreferences> preferenceProvider) {
    this.preferenceProvider = preferenceProvider;
  }

  @Override
  public RetrievableDatasourceDelegate get() {
    return newInstance(preferenceProvider.get());
  }

  public static RetrievableDatasourceDelegate_Factory create(
      Provider<SharedPreferences> preferenceProvider) {
    return new RetrievableDatasourceDelegate_Factory(preferenceProvider);
  }

  public static RetrievableDatasourceDelegate newInstance(SharedPreferences preference) {
    return new RetrievableDatasourceDelegate(preference);
  }
}
