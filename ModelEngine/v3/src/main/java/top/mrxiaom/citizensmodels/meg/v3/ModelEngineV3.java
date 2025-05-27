package top.mrxiaom.citizensmodels.meg.v3;

import com.google.common.collect.Lists;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.state.ModelState;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.mananger.ModelTicker;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.citizensmodels.api.IActiveModel;
import top.mrxiaom.citizensmodels.api.IModelEngine;

import java.util.List;
import java.util.function.Consumer;

public class ModelEngineV3 implements IModelEngine {
    private final Consumer<Runnable> runTask;
    public ModelEngineV3(Consumer<Runnable> runTask) {
        this.runTask = runTask;
    }

    @Override
    public void applyModel(NPC npc) {
        String modelId = npc.data().get(MODEL_ID_KEY, null);
        if (modelId == null) return;
        ActiveModel model = ModelEngineAPI.createActiveModel(modelId);

        Entity entity = npc.getEntity();
        ModeledEntity old = ModelEngineAPI.getModeledEntity(entity.getUniqueId());
        if (old != null) destroy(old);
        ModeledEntity modeled = ModelEngineAPI.getOrCreateModeledEntity(entity);
        modeled.setBaseEntityVisible(false);
        modeled.addModel(model, false);

        ModelTicker modelTicker = ModelEngineAPI.getModelTicker();
        modelTicker.registerModeledEntity(modeled.getBase(), modeled);
    }

    @Override
    public void resetModel(NPC npc, boolean deSpawn) {
        Entity entity = npc.getEntity();
        if (entity == null) return;
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity.getUniqueId());
        if (modeled != null) {
            Location loc = entity.getLocation();
            destroy(modeled);
            if (!deSpawn) runTask.accept(() -> {
                npc.despawn();
                npc.spawn(loc);
            });
        }
    }

    private void destroy(ModeledEntity modeled) {
        modeled.destroy();
        List<String> keys = Lists.newArrayList(modeled.getModels().keySet());
        for (String key : keys) {
            modeled.removeModel(key);
        }
    }

    @Override
    public void onDisable() {
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
            Entity entity = npc.getEntity();
            if (entity != null) {
                ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity.getUniqueId());
                if (modeled != null) {
                    Location loc = entity.getLocation();
                    destroy(modeled);
                    npc.despawn();
                    npc.spawn(loc);
                }
            }
        }
    }

    @Override
    public void markHurt(NPC npc) {
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(npc.getEntity().getUniqueId());
        if (modeled != null) {
            modeled.hurt();
        }
    }

    @Override
    public void markDeath(NPC npc) {
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(npc.getEntity().getUniqueId());
        if (modeled != null) {
            modeled.setState(ModelState.DEATH);
        }
    }

    @Override
    public @NotNull List<String> getOrderedModelIds() {
        return Lists.newArrayList(ModelEngineAPI.api.getModelRegistry().getAllBlueprintId());
    }

    @Override
    public @Nullable IActiveModel getActiveModel(@Nullable Entity entity, @Nullable String modelId) {
        ModeledEntity modeled = entity == null ? null : ModelEngineAPI.getModeledEntity(entity.getUniqueId());
        ActiveModel model = modeled == null || modelId == null ? null : modeled.getModel(modelId);
        return model == null ? null : new ActiveModelV3(model);
    }
}
