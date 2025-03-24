package eu.peernetwork.social.ui.feed;

import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.peernetwork.core.ui.factory.UiBuilderFactory;
import eu.peernetwork.core.ui.factory.UiBuilderFactory_Factory;
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
public final class DaggerFeed_Component {
  private DaggerFeed_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Feed feed;

    private Builder() {
    }

    public Builder feed(Feed feed) {
      this.feed = Preconditions.checkNotNull(feed);
      return this;
    }

    public Feed.Component build() {
      Preconditions.checkBuilderRequirement(feed, Feed.class);
      return new ComponentImpl(feed);
    }
  }

  private static final class ComponentImpl implements Feed.Component {
    private final ComponentImpl componentImpl = this;

    private Provider<Feed.Component> componentProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideMusicBuilderProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> providePhotoBuilderProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponent.Builder> provideVideoBuilderProvider;

    private Provider mapOfClassOfAndProviderOfUiComponentBuilderProvider;

    private Provider<UiBuilderFactory> uiBuilderFactoryProvider;

    private Provider<eu.peernetwork.core.ui.component.UiComponentProvider.Factory> provideBuilderFactoryProvider;

    private ComponentImpl(Feed feedParam) {

      initialize(feedParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final Feed feedParam) {
      this.componentProvider = InstanceFactory.create((Feed.Component) componentImpl);
      this.provideMusicBuilderProvider = DoubleCheck.provider(FeedModule_ProvideMusicBuilderFactory.create(componentProvider));
      this.providePhotoBuilderProvider = DoubleCheck.provider(FeedModule_ProvidePhotoBuilderFactory.create(componentProvider));
      this.provideVideoBuilderProvider = DoubleCheck.provider(FeedModule_ProvideVideoBuilderFactory.create(componentProvider));
      this.mapOfClassOfAndProviderOfUiComponentBuilderProvider = MapProviderFactory.<Class<? extends eu.peernetwork.core.ui.component.UiComponent.Builder>, eu.peernetwork.core.ui.component.UiComponent.Builder>builder(3).put(eu.peernetwork.social.ui.content.music.Music.Builder.class, provideMusicBuilderProvider).put(eu.peernetwork.social.ui.content.photo.Photo.Builder.class, providePhotoBuilderProvider).put(eu.peernetwork.social.ui.content.video.Video.Builder.class, provideVideoBuilderProvider).build();
      this.uiBuilderFactoryProvider = UiBuilderFactory_Factory.create(mapOfClassOfAndProviderOfUiComponentBuilderProvider);
      this.provideBuilderFactoryProvider = DoubleCheck.provider(FeedModule_ProvideBuilderFactoryFactory.create(uiBuilderFactoryProvider));
    }

    @Override
    public eu.peernetwork.core.ui.component.UiComponentProvider.Factory factory() {
      return provideBuilderFactoryProvider.get();
    }
  }
}
