package eu.peernetwork.app.module.core;

import com.apollographql.apollo3.ApolloClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.user.remote.interceptor.JwtInterceptor;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class NetworkModule_ProvideApolloClientFactory implements Factory<ApolloClient> {
  private final Provider<JwtInterceptor> jwtInterceptorProvider;

  public NetworkModule_ProvideApolloClientFactory(Provider<JwtInterceptor> jwtInterceptorProvider) {
    this.jwtInterceptorProvider = jwtInterceptorProvider;
  }

  @Override
  public ApolloClient get() {
    return provideApolloClient(jwtInterceptorProvider.get());
  }

  public static NetworkModule_ProvideApolloClientFactory create(
      Provider<JwtInterceptor> jwtInterceptorProvider) {
    return new NetworkModule_ProvideApolloClientFactory(jwtInterceptorProvider);
  }

  public static ApolloClient provideApolloClient(JwtInterceptor jwtInterceptor) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideApolloClient(jwtInterceptor));
  }
}
