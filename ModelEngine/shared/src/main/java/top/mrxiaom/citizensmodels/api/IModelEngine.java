package top.mrxiaom.citizensmodels.api;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IModelEngine {
    String MODEL_ID_KEY = "model-id";
    void applyModel(NPC npc);
    void resetModel(NPC npc, boolean deSpawn);
    void onDisable();

    void markHurt(NPC npc);
    void markDeath(NPC npc);

    @NotNull List<String> getOrderedModelIds();
    @Nullable IActiveModel getActiveModel(@Nullable Entity entity, @Nullable String modelId);
}
