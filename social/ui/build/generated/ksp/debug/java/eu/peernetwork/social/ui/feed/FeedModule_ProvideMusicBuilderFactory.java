package eu.peernetwork.social.ui.feed;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.peernetwork.core.ui.component.UiComponent;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("eu.peernetwork.social.ui.feed.Feed.Scope")
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
public final class FeedModule_ProvideMusicBuilderFactory implements Factory<UiComponent.Builder> {
  private final Provider<Feed.Component> componentProvider;

  public FeedModule_ProvideMusicBuilderFactory(Provider<Feed.Component> componentProvider) {
    this.componentProvider = componentProvider;
  }

  @Override
  public UiComponent.Builder get() {
    return provideMusicBuilder(componentProvider.get());
  }

  public static FeedModule_ProvideMusicBuilderFactory create(
      Provider<Feed.Component> componentProvider) {
    return new FeedModule_ProvideMusicBuilderFactory(componentProvider);
  }

  public static UiComponent.Builder provideMusicBuilder(Feed.Component component) {
    return Preconditions.checkNotNullFromProvides(FeedModule.INSTANCE.provideMusicBuilder(component));
  }
}
