package top.mrxiaom.citizensmodels.meg.v4;

import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.model.ActiveModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.citizensmodels.api.IActiveModel;
import top.mrxiaom.citizensmodels.api.IAnimation;

import java.util.List;

public class ActiveModelV4 implements IActiveModel {
    private final ActiveModel activeModel;
    public ActiveModelV4(ActiveModel activeModel) {
        this.activeModel = activeModel;
    }

    @Override
    public @NotNull List<String> getAnimationKeys() {
        return List.of();
    }

    @Override
    public @Nullable IAnimation getAnimation(String id) {
        AnimationHandler handler = activeModel.getAnimationHandler();
        IAnimationProperty animation = handler.getAnimation(id);
        return animation == null ? null : new AnimationV4(handler, animation);
    }
}
