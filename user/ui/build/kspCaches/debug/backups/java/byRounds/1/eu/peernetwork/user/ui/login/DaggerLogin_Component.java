package eu.peernetwork.user.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.user.domain.repository.AuthenticationRepository;
import eu.peernetwork.user.domain.repository.TokenRepository;
import eu.peernetwork.user.domain.usecase.LoginUsecase;
import eu.peernetwork.user.domain.usecase.LoginUsecase_Factory;
import javax.annotation.processing.Generated;

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
public final class DaggerLogin_Component {
  private DaggerLogin_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Login login;

    private Builder() {
    }

    public Builder login(Login login) {
      this.login = Preconditions.checkNotNull(login);
      return this;
    }

    public Login.Component build() {
      Preconditions.checkBuilderRequirement(login, Login.class);
      return new ComponentImpl(login);
    }
  }

  private static final class ComponentImpl implements Login.Component {
    private final Login login;

    private final ComponentImpl componentImpl = this;

    private Provider<AuthenticationRepository> authenticationRepositoryProvider;

    private Provider<LoginUsecase> loginUsecaseProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<ViewModel> provideViewModelProvider;

    private Provider mapOfClassOfAndProviderOfViewModelProvider;

    private Provider<ViewModelProvider.Factory> provideViewModelFactoryProvider;

    private ComponentImpl(Login loginParam) {
      this.login = loginParam;
      initialize(loginParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Login loginParam) {
      this.authenticationRepositoryProvider = new AuthenticationRepositoryProvider(loginParam);
      this.loginUsecaseProvider = LoginUsecase_Factory.create(authenticationRepositoryProvider);
      this.loginViewModelProvider = DoubleCheck.provider(LoginViewModel_Factory.create(loginUsecaseProvider));
      this.provideViewModelProvider = DoubleCheck.provider(LoginModule_ProvideViewModelFactory.create(loginViewModelProvider));
      this.mapOfClassOfAndProviderOfViewModelProvider = MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(1).put(LoginViewModel.class, provideViewModelProvider).build();
      this.provideViewModelFactoryProvider = DoubleCheck.provider(LoginModule_ProvideViewModelFactoryFactory.create(mapOfClassOfAndProviderOfViewModelProvider));
    }

    @Override
    public AuthenticationRepository authenticationRepository() {
      return Preconditions.checkNotNullFromComponent(login.authenticationRepository());
    }

    @Override
    public TokenRepository tokenRepository() {
      return Preconditions.checkNotNullFromComponent(login.tokenRepository());
    }

    @Override
    public ViewModelProvider.Factory viewModelFactory() {
      return provideViewModelFactoryProvider.get();
    }

    private static final class AuthenticationRepositoryProvider implements Provider<AuthenticationRepository> {
      private final Login login;

      AuthenticationRepositoryProvider(Login login) {
        this.login = login;
      }

      @Override
      public AuthenticationRepository get() {
        return Preconditions.checkNotNullFromComponent(login.authenticationRepository());
      }
    }
  }
}
