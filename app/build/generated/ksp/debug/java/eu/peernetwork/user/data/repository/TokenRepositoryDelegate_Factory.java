package eu.peernetwork.user.data.repository;

import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.persistence.domain.observable.ObservableString;
import eu.peernetwork.persistence.domain.publishable.PublishableString;
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
public final class TokenRepositoryDelegate_Factory implements Factory<TokenRepositoryDelegate> {
  private final Provider<Gson> gsonProvider;

  private final Provider<PublishableString> publisherProvider;

  private final Provider<ObservableString> observableProvider;

  public TokenRepositoryDelegate_Factory(Provider<Gson> gsonProvider,
      Provider<PublishableString> publisherProvider,
      Provider<ObservableString> observableProvider) {
    this.gsonProvider = gsonProvider;
    this.publisherProvider = publisherProvider;
    this.observableProvider = observableProvider;
  }

  @Override
  public TokenRepositoryDelegate get() {
    return newInstance(gsonProvider.get(), publisherProvider.get(), observableProvider.get());
  }

  public static TokenRepositoryDelegate_Factory create(Provider<Gson> gsonProvider,
      Provider<PublishableString> publisherProvider,
      Provider<ObservableString> observableProvider) {
    return new TokenRepositoryDelegate_Factory(gsonProvider, publisherProvider, observableProvider);
  }

  public static TokenRepositoryDelegate newInstance(Gson gson, PublishableString publisher,
      ObservableString observable) {
    return new TokenRepositoryDelegate(gson, publisher, observable);
  }
}
