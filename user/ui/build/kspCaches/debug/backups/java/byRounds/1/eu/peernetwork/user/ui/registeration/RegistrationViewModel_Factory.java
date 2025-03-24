package eu.peernetwork.user.ui.registeration;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.domain.usecase.ActivationUsecase;
import eu.peernetwork.user.domain.usecase.RegistrationUsecase;
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
public final class RegistrationViewModel_Factory implements Factory<RegistrationViewModel> {
  private final Provider<RegistrationUsecase> registrationUsecaseProvider;

  private final Provider<ActivationUsecase> activationUsecaseProvider;

  public RegistrationViewModel_Factory(Provider<RegistrationUsecase> registrationUsecaseProvider,
      Provider<ActivationUsecase> activationUsecaseProvider) {
    this.registrationUsecaseProvider = registrationUsecaseProvider;
    this.activationUsecaseProvider = activationUsecaseProvider;
  }

  @Override
  public RegistrationViewModel get() {
    return newInstance(registrationUsecaseProvider.get(), activationUsecaseProvider.get());
  }

  public static RegistrationViewModel_Factory create(
      Provider<RegistrationUsecase> registrationUsecaseProvider,
      Provider<ActivationUsecase> activationUsecaseProvider) {
    return new RegistrationViewModel_Factory(registrationUsecaseProvider, activationUsecaseProvider);
  }

  public static RegistrationViewModel newInstance(RegistrationUsecase registrationUsecase,
      ActivationUsecase activationUsecase) {
    return new RegistrationViewModel(registrationUsecase, activationUsecase);
  }
}
