package eu.peernetwork.user.ui.registeration;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.user.domain.repository.AccountRepository;
import eu.peernetwork.user.domain.repository.SearchRepository;
import eu.peernetwork.user.domain.usecase.ActivationUsecase;
import eu.peernetwork.user.domain.usecase.ActivationUsecase_Factory;
import eu.peernetwork.user.domain.usecase.RegistrationUsecase;
import eu.peernetwork.user.domain.usecase.RegistrationUsecase_Factory;
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
public final class DaggerRegistration_Component {
  private DaggerRegistration_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Registration registration;

    private Builder() {
    }

    public Builder registration(Registration registration) {
      this.registration = Preconditions.checkNotNull(registration);
      return this;
    }

    public Registration.Component build() {
      Preconditions.checkBuilderRequirement(registration, Registration.class);
      return new ComponentImpl(registration);
    }
  }

  private static final class ComponentImpl implements Registration.Component {
    private final Registration registration;

    private final ComponentImpl componentImpl = this;

    private Provider<AccountRepository> accountRepositoryProvider;

    private Provider<RegistrationUsecase> registrationUsecaseProvider;

    private Provider<ActivationUsecase> activationUsecaseProvider;

    private Provider<RegistrationViewModel> registrationViewModelProvider;

    private Provider<ViewModel> provideViewModelProvider;

    private Provider mapOfClassOfAndProviderOfViewModelProvider;

    private Provider<ViewModelProvider.Factory> provideViewModelFactoryProvider;

    private ComponentImpl(Registration registrationParam) {
      this.registration = registrationParam;
      initialize(registrationParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Registration registrationParam) {
      this.accountRepositoryProvider = new AccountRepositoryProvider(registrationParam);
      this.registrationUsecaseProvider = RegistrationUsecase_Factory.create(accountRepositoryProvider);
      this.activationUsecaseProvider = ActivationUsecase_Factory.create(accountRepositoryProvider);
      this.registrationViewModelProvider = DoubleCheck.provider(RegistrationViewModel_Factory.create(registrationUsecaseProvider, activationUsecaseProvider));
      this.provideViewModelProvider = DoubleCheck.provider(RegistrationModule_ProvideViewModelFactory.create(registrationViewModelProvider));
      this.mapOfClassOfAndProviderOfViewModelProvider = MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(1).put(RegistrationViewModel.class, provideViewModelProvider).build();
      this.provideViewModelFactoryProvider = DoubleCheck.provider(RegistrationModule_ProvideViewModelFactoryFactory.create(mapOfClassOfAndProviderOfViewModelProvider));
    }

    @Override
    public AccountRepository accountRepository() {
      return Preconditions.checkNotNullFromComponent(registration.accountRepository());
    }

    @Override
    public SearchRepository searchRepository() {
      return Preconditions.checkNotNullFromComponent(registration.searchRepository());
    }

    @Override
    public ViewModelProvider.Factory viewModelFactory() {
      return provideViewModelFactoryProvider.get();
    }

    private static final class AccountRepositoryProvider implements Provider<AccountRepository> {
      private final Registration registration;

      AccountRepositoryProvider(Registration registration) {
        this.registration = registration;
      }

      @Override
      public AccountRepository get() {
        return Preconditions.checkNotNullFromComponent(registration.accountRepository());
      }
    }
  }
}
