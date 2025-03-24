package eu.peernetwork.social.ui.content.music;

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
public final class DaggerMusic_Component {
  private DaggerMusic_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Music music;

    private Builder() {
    }

    public Builder music(Music music) {
      this.music = Preconditions.checkNotNull(music);
      return this;
    }

    public Music.Component build() {
      Preconditions.checkBuilderRequirement(music, Music.class);
      return new ComponentImpl(music);
    }
  }

  private static final class ComponentImpl implements Music.Component {
    private final ComponentImpl componentImpl = this;

    private ComponentImpl(Music musicParam) {


    }
  }
}
