package eu.peernetwork.user.ui.registeration;

import androidx.lifecycle.ViewModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class RegistrationModule_ProvideViewModelFactory implements Factory<ViewModel> {
  private final Provider<RegistrationViewModel> viewModelProvider;

  public RegistrationModule_ProvideViewModelFactory(
      Provider<RegistrationViewModel> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  @Override
  public ViewModel get() {
    return provideViewModel(viewModelProvider.get());
  }

  public static RegistrationModule_ProvideViewModelFactory create(
      Provider<RegistrationViewModel> viewModelProvider) {
    return new RegistrationModule_ProvideViewModelFactory(viewModelProvider);
  }

  public static ViewModel provideViewModel(RegistrationViewModel viewModel) {
    return Preconditions.checkNotNullFromProvides(RegistrationModule.INSTANCE.provideViewModel(viewModel));
  }
}
