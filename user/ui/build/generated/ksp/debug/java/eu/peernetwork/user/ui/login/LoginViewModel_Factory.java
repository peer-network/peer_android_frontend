package eu.peernetwork.user.ui.login;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.usecase.LoginUsecase;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<LoginUsecase> loginUsecaseProvider;

  public LoginViewModel_Factory(Provider<LoginUsecase> loginUsecaseProvider) {
    this.loginUsecaseProvider = loginUsecaseProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(loginUsecaseProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<LoginUsecase> loginUsecaseProvider) {
    return new LoginViewModel_Factory(loginUsecaseProvider);
  }

  public static LoginViewModel newInstance(LoginUsecase loginUsecase) {
    return new LoginViewModel(loginUsecase);
  }
}
