package eu.peernetwork.app.ui.main;

import androidx.lifecycle.ViewModelProvider;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<ViewModelProvider.Factory> factoryProvider;

  public MainActivity_MembersInjector(Provider<ViewModelProvider.Factory> factoryProvider) {
    this.factoryProvider = factoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<ViewModelProvider.Factory> factoryProvider) {
    return new MainActivity_MembersInjector(factoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectFactory(instance, factoryProvider.get());
  }

  @InjectedFieldSignature("eu.peernetwork.app.ui.main.MainActivity.factory")
  public static void injectFactory(MainActivity instance, ViewModelProvider.Factory factory) {
    instance.factory = factory;
  }
}
