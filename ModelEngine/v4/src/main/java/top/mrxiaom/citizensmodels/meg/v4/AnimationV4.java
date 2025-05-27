package top.mrxiaom.citizensmodels.meg.v4;

import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import top.mrxiaom.citizensmodels.api.IAnimation;

public class AnimationV4 implements IAnimation {
    private final AnimationHandler handler;
    private final IAnimationProperty animation;

    public AnimationV4(AnimationHandler handler, IAnimationProperty animation) {
        this.handler = handler;
        this.animation = animation;
    }

    @Override
    public void play(boolean force) {
        handler.playAnimation(animation, force);
    }
}
