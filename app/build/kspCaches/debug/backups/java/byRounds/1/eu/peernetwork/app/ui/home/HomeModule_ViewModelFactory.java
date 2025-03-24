package eu.peernetwork.app.ui.home;

import androidx.lifecycle.ViewModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.app.ui.home.Home.Scope")
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
public final class HomeModule_ViewModelFactory implements Factory<ViewModel> {
  private final Provider<HomeViewModel> viewModelProvider;

  public HomeModule_ViewModelFactory(Provider<HomeViewModel> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  @Override
  public ViewModel get() {
    return viewModel(viewModelProvider.get());
  }

  public static HomeModule_ViewModelFactory create(Provider<HomeViewModel> viewModelProvider) {
    return new HomeModule_ViewModelFactory(viewModelProvider);
  }

  public static ViewModel viewModel(HomeViewModel viewModel) {
    return Preconditions.checkNotNullFromProvides(HomeModule.INSTANCE.viewModel(viewModel));
  }
}
