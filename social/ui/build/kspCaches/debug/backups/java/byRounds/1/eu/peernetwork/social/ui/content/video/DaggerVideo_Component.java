package eu.peernetwork.social.ui.content.video;

import dagger.internal.DaggerGenerated;
import dagger.internal.Preconditions;
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
public final class DaggerVideo_Component {
  private DaggerVideo_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Video video;

    private Builder() {
    }

    public Builder video(Video video) {
      this.video = Preconditions.checkNotNull(video);
      return this;
    }

    public Video.Component build() {
      Preconditions.checkBuilderRequirement(video, Video.class);
      return new ComponentImpl(video);
    }
  }

  private static final class ComponentImpl implements Video.Component {
    private final ComponentImpl componentImpl = this;

    private ComponentImpl(Video videoParam) {


    }
  }
}
