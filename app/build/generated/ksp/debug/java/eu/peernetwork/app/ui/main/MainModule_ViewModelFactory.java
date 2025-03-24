package eu.peernetwork.app.ui.main;

import androidx.lifecycle.ViewModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.app.ui.main.Main.Scope")
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
public final class MainModule_ViewModelFactory implements Factory<ViewModel> {
  private final Provider<MainViewModel> viewModelProvider;

  public MainModule_ViewModelFactory(Provider<MainViewModel> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  @Override
  public ViewModel get() {
    return viewModel(viewModelProvider.get());
  }

  public static MainModule_ViewModelFactory create(Provider<MainViewModel> viewModelProvider) {
    return new MainModule_ViewModelFactory(viewModelProvider);
  }

  public static ViewModel viewModel(MainViewModel viewModel) {
    return Preconditions.checkNotNullFromProvides(MainModule.INSTANCE.viewModel(viewModel));
  }
}
