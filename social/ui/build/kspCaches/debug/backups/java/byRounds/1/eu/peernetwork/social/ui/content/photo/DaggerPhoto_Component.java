package eu.peernetwork.social.ui.content.photo;

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
public final class DaggerPhoto_Component {
  private DaggerPhoto_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Photo photo;

    private Builder() {
    }

    public Builder photo(Photo photo) {
      this.photo = Preconditions.checkNotNull(photo);
      return this;
    }

    public Photo.Component build() {
      Preconditions.checkBuilderRequirement(photo, Photo.class);
      return new ComponentImpl(photo);
    }
  }

  private static final class ComponentImpl implements Photo.Component {
    private final ComponentImpl componentImpl = this;

    private ComponentImpl(Photo photoParam) {


    }
  }
}
