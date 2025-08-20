package top.mrxiaom.citizensmodels.meg.v4;

import com.google.common.collect.Lists;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModelUpdaters;
import com.ticxo.modelengine.api.model.ModeledEntity;
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

public class ModelEngineV4 implements IModelEngine {
    private final Consumer<Runnable> runTask;
    public ModelEngineV4(Consumer<Runnable> runTask) {
        this.runTask = runTask;
    }

    @Override
    public void applyModel(NPC npc) {
        String modelId = npc.data().get(MODEL_ID_KEY, null);
        if (modelId == null) return;
        ActiveModel model = ModelEngineAPI.createActiveModel(modelId);

        Entity entity = npc.getEntity();
        ModeledEntity old = ModelEngineAPI.getModeledEntity(entity);
        if (old != null) destroy(old);
        ModeledEntity modeled = ModelEngineAPI.getOrCreateModeledEntity(entity);
        modeled.setBaseEntityVisible(false);
        modeled.addModel(model, false);
        ModelUpdaters updaters = ModelEngineAPI.getAPI().getModelUpdaters();
        updaters.registerModeledEntity(modeled.getBase(), modeled);
    }

    @Override
    public void resetModel(NPC npc, boolean deSpawn) {
        Entity entity = npc.getEntity();
        if (entity == null) return;
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity);
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
        modeled.restore();
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
                ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity);
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
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(npc.getEntity());
        if (modeled != null) {
            modeled.markHurt();
        }
    }

    @Override
    public void markDeath(NPC npc) {
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(npc.getEntity());
        if (modeled != null) {
            modeled.markRemoved();
        }
    }

    @Override
    public @NotNull List<String> getOrderedModelIds() {
        return Lists.newArrayList(ModelEngineAPI.getAPI().getModelRegistry().getOrderedId());
    }

    @Override
    public @Nullable IActiveModel getActiveModel(@Nullable Entity entity, @Nullable String modelId) {
        ModeledEntity modeled = entity == null ? null : ModelEngineAPI.getModeledEntity(entity);
        ActiveModel model = modeled == null || modelId == null ? null : modeled.getModel(modelId).orElse(null);
        return model == null ? null : new ActiveModelV4(model);
    }
}
