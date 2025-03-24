package eu.peernetwork.user.ui.registeration;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.user.ui.registeration.Registration.Scope")
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
public final class RegistrationModule_ProvideViewModelFactoryFactory implements Factory<ViewModelProvider.Factory> {
  private final Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> classToViewModelProvider;

  public RegistrationModule_ProvideViewModelFactoryFactory(
      Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> classToViewModelProvider) {
    this.classToViewModelProvider = classToViewModelProvider;
  }

  @Override
  public ViewModelProvider.Factory get() {
    return provideViewModelFactory(classToViewModelProvider.get());
  }

  public static RegistrationModule_ProvideViewModelFactoryFactory create(
      Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> classToViewModelProvider) {
    return new RegistrationModule_ProvideViewModelFactoryFactory(classToViewModelProvider);
  }

  public static ViewModelProvider.Factory provideViewModelFactory(
      Map<Class<? extends ViewModel>, Provider<ViewModel>> classToViewModel) {
    return Preconditions.checkNotNullFromProvides(RegistrationModule.INSTANCE.provideViewModelFactory(classToViewModel));
  }
}
