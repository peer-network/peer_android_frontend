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
public final class PublishableDatasourceDelegate_Factory implements Factory<PublishableDatasourceDelegate> {
  private final Provider<SharedPreferences> preferenceProvider;

  public PublishableDatasourceDelegate_Factory(Provider<SharedPreferences> preferenceProvider) {
    this.preferenceProvider = preferenceProvider;
  }

  @Override
  public PublishableDatasourceDelegate get() {
    return newInstance(preferenceProvider.get());
  }

  public static PublishableDatasourceDelegate_Factory create(
      Provider<SharedPreferences> preferenceProvider) {
    return new PublishableDatasourceDelegate_Factory(preferenceProvider);
  }

  public static PublishableDatasourceDelegate newInstance(SharedPreferences preference) {
    return new PublishableDatasourceDelegate(preference);
  }
}
