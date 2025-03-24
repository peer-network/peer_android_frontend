package eu.peernetwork.user.ui.login;

import androidx.lifecycle.ViewModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.user.ui.login.Login.Scope")
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
public final class LoginModule_ProvideViewModelFactory implements Factory<ViewModel> {
  private final Provider<LoginViewModel> viewModelProvider;

  public LoginModule_ProvideViewModelFactory(Provider<LoginViewModel> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  @Override
  public ViewModel get() {
    return provideViewModel(viewModelProvider.get());
  }

  public static LoginModule_ProvideViewModelFactory create(
      Provider<LoginViewModel> viewModelProvider) {
    return new LoginModule_ProvideViewModelFactory(viewModelProvider);
  }

  public static ViewModel provideViewModel(LoginViewModel viewModel) {
    return Preconditions.checkNotNullFromProvides(LoginModule.INSTANCE.provideViewModel(viewModel));
  }
}
