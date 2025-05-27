package top.mrxiaom.citizensmodels.func;

import com.google.common.collect.Lists;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModelUpdaters;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.mrxiaom.citizensmodels.CitizensModels;
import top.mrxiaom.pluginbase.func.AutoRegister;

import java.util.List;

@AutoRegister
public class NPCListener extends AbstractModule implements Listener {
    public NPCListener(CitizensModels plugin) {
        super(plugin);
        registerEvents();
        plugin.getScheduler().runTaskLater(() -> {
            for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
                if (npc.isSpawned()) {
                    applyModel(npc);
                }
            }
        }, 5L);
    }

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent e) {
        applyModel(e.getNPC());
    }

    @EventHandler
    public void onNPCDeSpawn(NPCDespawnEvent e){
        resetModel(e.getNPC());
    }

    @EventHandler
    public void onNPCHurt(NPCDamageEvent e) {
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(e.getNPC().getEntity());
        if (modeled != null) {
            modeled.markHurt();
        }
    }

    @EventHandler
    public void onNPCDead(NPCDeathEvent e) {
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(e.getNPC().getEntity());
        if (modeled != null) {
            modeled.markRemoved();
        }
    }

    public void setNPCModel(NPC npc, String modelId) {
        if (modelId == null) {
            npc.data().remove("model-id");
            resetModel(npc);
        } else {
            npc.data().setPersistent("model-id", modelId);
            applyModel(npc);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean hasInvisibleTrait(NPC npc) {
        for (Trait trait : npc.getTraits()) {
            if (trait.getName().equals("invisible")) {
                return true;
            }
        }
        return false;
    }

    public void applyModel(NPC npc) {
        String modelId = npc.data().get("model-id", null);
        if (modelId == null) return;
        info("正在更新 " + npc.getFullName() + " 的模型为 " + modelId);
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

    public void resetModel(NPC npc) {
        Entity entity = npc.getEntity();
        if (entity == null) return;
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity);
        if (modeled != null) {
            info("正在移除 " + npc.getFullName() + " 的模型");
            destroy(modeled);
            npc.despawn();
            npc.spawn(npc.getStoredLocation());
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
            resetModel(npc);
        }
    }

    public static NPCListener inst() {
        return instanceOf(NPCListener.class);
    }
}
