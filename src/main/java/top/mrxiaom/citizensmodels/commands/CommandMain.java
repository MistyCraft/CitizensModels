package top.mrxiaom.citizensmodels.commands;
        
import com.google.common.collect.Lists;
import com.ticxo.modelengine.api.ModelEngineAPI;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.NPCSelector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.citizensmodels.func.NPCListener;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.citizensmodels.CitizensModels;
import top.mrxiaom.citizensmodels.func.AbstractModule;

import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    public CommandMain(CitizensModels plugin) {
        super(plugin);
        registerCommand("citizensmodels", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length == 2 && "set".equalsIgnoreCase(args[0])) {
                NPCSelector selector = ((Citizens) CitizensAPI.getPlugin()).getNPCSelector();
                NPC npc = selector.getSelected(sender);
                if (npc == null) {
                    return t(sender, "&e你应该先选择一个NPC &7(/npc sel)");
                }
                String modelId = args[1];
                if (!ModelEngineAPI.getAPI().getModelRegistry().getOrderedId().contains(modelId)) {
                    return t(sender, "&e找不到指定的模型");
                }
                NPCListener.inst().setNPCModel(npc, modelId);
                return t(sender, "&a已设置 NPC 模型为&e " + modelId);
            }
            if (args.length == 1 && "reset".equalsIgnoreCase(args[0])) {
                NPCSelector selector = ((Citizens) CitizensAPI.getPlugin()).getNPCSelector();
                NPC npc = selector.getSelected(sender);
                if (npc == null) {
                    return t(sender, "&e你应该先选择一个NPC &7(/npc sel)");
                }
                NPCListener.inst().setNPCModel(npc, null);
                return t(sender, "&a已重置 NPC 模型");
            }
            if (args.length == 1 && "reload".equalsIgnoreCase(args[0])) {
                plugin.reloadConfig();
                return t(sender, "&a配置文件已重载");
            }
            return t(sender, "&b&lCitizensModels&r",
                    "&f/npcm set <蓝图> &e设置已选中的NPC的模型",
                    "&f/npcm reset &e重置已选中的NPC的模型",
                    "&f/npcm reload &e重载插件配置文件");
        }
        return true;
    }

    private static final List<String> emptyList = Collections.emptyList();
    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "set", "reset", "reload");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        if (args.length == 2) {
            if (sender.isOp()) {
                if ("set".equalsIgnoreCase(args[0])) {
                    return startsWith(ModelEngineAPI.getAPI().getModelRegistry().getOrderedId(), args[1]);
                }
            }
        }
        return emptyList;
    }

    public List<String> startsWith(Collection<String> list, String s) {
        return startsWith(null, list, s);
    }
    public List<String> startsWith(String[] addition, Collection<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        if (addition != null) stringList.addAll(0, Lists.newArrayList(addition));
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}
