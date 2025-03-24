package eu.peernetwork.social.ui.post;

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
public final class DaggerPost_Component {
  private DaggerPost_Component() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Post post;

    private Builder() {
    }

    public Builder post(Post post) {
      this.post = Preconditions.checkNotNull(post);
      return this;
    }

    public Post.Component build() {
      Preconditions.checkBuilderRequirement(post, Post.class);
      return new ComponentImpl(post);
    }
  }

  private static final class ComponentImpl implements Post.Component {
    private final ComponentImpl componentImpl = this;

    private ComponentImpl(Post postParam) {


    }
  }
}
