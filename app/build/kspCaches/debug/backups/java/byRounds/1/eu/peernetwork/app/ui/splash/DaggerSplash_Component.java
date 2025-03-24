package eu.peernetwork.app.ui.splash;

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
public final class DaggerSplash_Component {
  private DaggerSplash_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Splash splash;

    private Builder() {
    }

    public Builder splash(Splash splash) {
      this.splash = Preconditions.checkNotNull(splash);
      return this;
    }

    public Splash.Component build() {
      Preconditions.checkBuilderRequirement(splash, Splash.class);
      return new ComponentImpl(splash);
    }
  }

  private static final class ComponentImpl implements Splash.Component {
    private final ComponentImpl componentImpl = this;

    private ComponentImpl(Splash splashParam) {


    }
  }
}
