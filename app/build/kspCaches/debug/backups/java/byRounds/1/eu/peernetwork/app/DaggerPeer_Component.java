package eu.peernetwork.app;

import android.content.Context;
import android.content.SharedPreferences;
import com.apollographql.apollo3.ApolloClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.app.module.core.CoreModule_ProvideGsonFactory;
import eu.peernetwork.app.module.core.CoreModule_ProvideSharedPreferencesFactory;
import eu.peernetwork.app.module.core.NetworkModule_ProvideApolloClientFactory;
import eu.peernetwork.app.module.core.UiModule_ProvideFactoryFactory;
import eu.peernetwork.app.module.core.UiModule_ProvideMainBuilderFactory;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import eu.peernetwork.persistence.data.repository.PreferenceRepositoryDelegate;
import eu.peernetwork.persistence.domain.observable.ObservableString;
import eu.peernetwork.persistence.domain.publishable.PublishableString;
import eu.peernetwork.persistence.domain.repository.PreferenceRepository;
import eu.peernetwork.persistence.local.datasource.ObservableDatasourceDelegate;
import eu.peernetwork.persistence.local.datasource.PublishableDatasourceDelegate;
import eu.peernetwork.persistence.local.datasource.RetrievableDatasourceDelegate;
import eu.peernetwork.user.data.api.SettingsApi;
import eu.peernetwork.user.data.repository.AccountRepositoryDelegate;
import eu.peernetwork.user.data.repository.AuthenticationRepositoryDelegate;
import eu.peernetwork.user.data.repository.SearchRepositoryDelegate;
import eu.peernetwork.user.data.repository.TokenRepositoryDelegate;
import eu.peernetwork.user.domain.repository.AccountRepository;
import eu.peernetwork.user.domain.repository.AuthenticationRepository;
import eu.peernetwork.user.domain.repository.SearchRepository;
import eu.peernetwork.user.domain.repository.TokenRepository;
import eu.peernetwork.user.domain.usecase.TokenUsecase;
import eu.peernetwork.user.remote.api.AccountApiDelegate;
import eu.peernetwork.user.remote.api.AuthenticationApiDelegate;
import eu.peernetwork.user.remote.api.AvatarSettingsApi;
import eu.peernetwork.user.remote.api.BiographySettingsApi;
import eu.peernetwork.user.remote.api.EmailSettingsApi;
import eu.peernetwork.user.remote.api.SearchApiDelegate;
import eu.peernetwork.user.remote.api.UsernameSettingsApi;
import eu.peernetwork.user.remote.interceptor.JwtInterceptor;
import eu.peernetwork.user.remote.provider.SettingsProviderDelegate;
import java.util.Collections;
import java.util.Map;
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
public final class DaggerPeer_Component {
  private DaggerPeer_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Peer peer;

    private Builder() {
    }

    public Builder peer(Peer peer) {
      this.peer = Preconditions.checkNotNull(peer);
      return this;
    }

    public Peer.Component build() {
      Preconditions.checkBuilderRequirement(peer, Peer.class);
      return new ComponentImpl(peer);
    }
  }

  private static final class ComponentImpl implements Peer.Component {
    private final Peer peer;

    private final ComponentImpl componentImpl = this;

    private Provider<Peer.Component> componentProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideMainBuilderProvider;

    private ComponentImpl(Peer peerParam) {
      this.peer = peerParam;
      initialize(peerParam);

    }

    private Map<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, javax.inject.Provider<eu.peernetwork.core.ui.component.UiComponent.Builder>> mapOfClassOfAndProviderOfUiComponentBuilder(
        ) {
      return Collections.<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, javax.inject.Provider<eu.peernetwork.core.ui.component.UiComponent.Builder>>singletonMap(eu.peernetwork.app.ui.main.Main.Builder.class, provideMainBuilderProvider);
    }

    private UiBuilderFactory uiBuilderFactory() {
      return new UiBuilderFactory(mapOfClassOfAndProviderOfUiComponentBuilder());
    }

    private SharedPreferences sharedPreferences() {
      return CoreModule_ProvideSharedPreferencesFactory.provideSharedPreferences(Preconditions.checkNotNullFromComponent(peer.getApplicationContext()));
    }

    private ObservableDatasourceDelegate observableDatasourceDelegate() {
      return new ObservableDatasourceDelegate(sharedPreferences());
    }

    private PublishableDatasourceDelegate publishableDatasourceDelegate() {
      return new PublishableDatasourceDelegate(sharedPreferences());
    }

    private RetrievableDatasourceDelegate retrievableDatasourceDelegate() {
      return new RetrievableDatasourceDelegate(sharedPreferences());
    }

    private PreferenceRepositoryDelegate preferenceRepositoryDelegate() {
      return new PreferenceRepositoryDelegate(observableDatasourceDelegate(), publishableDatasourceDelegate(), retrievableDatasourceDelegate());
    }

    private PublishableString publishableString() {
      return new PublishableString(preferenceRepositoryDelegate());
    }

    private ObservableString observableString() {
      return new ObservableString(preferenceRepositoryDelegate());
    }

    private TokenRepositoryDelegate tokenRepositoryDelegate() {
      return new TokenRepositoryDelegate(CoreModule_ProvideGsonFactory.provideGson(), publishableString(), observableString());
    }

    private TokenUsecase tokenUsecase() {
      return new TokenUsecase(tokenRepositoryDelegate());
    }

    private JwtInterceptor jwtInterceptor() {
      return new JwtInterceptor(tokenUsecase());
    }

    private ApolloClient apolloClient() {
      return NetworkModule_ProvideApolloClientFactory.provideApolloClient(jwtInterceptor());
    }

    private AccountApiDelegate accountApiDelegate() {
      return new AccountApiDelegate(apolloClient());
    }

    private UsernameSettingsApi usernameSettingsApi() {
      return new UsernameSettingsApi(apolloClient());
    }

    private EmailSettingsApi emailSettingsApi() {
      return new EmailSettingsApi(apolloClient());
    }

    private BiographySettingsApi biographySettingsApi() {
      return new BiographySettingsApi(apolloClient());
    }

    private AvatarSettingsApi avatarSettingsApi() {
      return new AvatarSettingsApi(apolloClient());
    }

    private Map<String, SettingsApi<?>> mapOfStringAndSettingsApiOf() {
      return MapBuilder.<String, SettingsApi<?>>newMapBuilder(4).put("username", usernameSettingsApi()).put("email", emailSettingsApi()).put("bio", biographySettingsApi()).put("avatar", avatarSettingsApi()).build();
    }

    private SettingsProviderDelegate settingsProviderDelegate() {
      return new SettingsProviderDelegate(mapOfStringAndSettingsApiOf());
    }

    private AccountRepositoryDelegate accountRepositoryDelegate() {
      return new AccountRepositoryDelegate(accountApiDelegate(), settingsProviderDelegate());
    }

    private SearchApiDelegate searchApiDelegate() {
      return new SearchApiDelegate(apolloClient());
    }

    private SearchRepositoryDelegate searchRepositoryDelegate() {
      return new SearchRepositoryDelegate(searchApiDelegate());
    }

    private AuthenticationApiDelegate authenticationApiDelegate() {
      return new AuthenticationApiDelegate(apolloClient(), tokenRepositoryDelegate());
    }

    private AuthenticationRepositoryDelegate authenticationRepositoryDelegate() {
      return new AuthenticationRepositoryDelegate(authenticationApiDelegate());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final Peer peerParam) {
      this.componentProvider = InstanceFactory.create((Peer.Component) componentImpl);
      this.provideMainBuilderProvider = DoubleCheck.provider(UiModule_ProvideMainBuilderFactory.create(componentProvider));
    }

    @Override
    public Context getApplicationContext() {
      return Preconditions.checkNotNullFromComponent(peer.getApplicationContext());
    }

    @Override
    public void inject(PeerApplication p0) {
    }

    @Override
    public eu.peernetwork.core.ui.component.UiComponentProvider.Factory factory() {
      return UiModule_ProvideFactoryFactory.provideFactory(uiBuilderFactory());
    }

    @Override
    public AccountRepository accountRepository() {
      return accountRepositoryDelegate();
    }

    @Override
    public SearchRepository searchRepository() {
      return searchRepositoryDelegate();
    }

    @Override
    public AuthenticationRepository authenticationRepository() {
      return authenticationRepositoryDelegate();
    }

    @Override
    public TokenRepository tokenRepository() {
      return tokenRepositoryDelegate();
    }

    @Override
    public PreferenceRepository preferenceRepository() {
      return preferenceRepositoryDelegate();
    }
  }
}
