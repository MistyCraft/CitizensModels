package top.mrxiaom.citizensmodels;

import top.mrxiaom.pluginbase.func.language.IHolderAccessor;
import top.mrxiaom.pluginbase.func.language.LanguageEnumAutoHolder;

import java.util.List;

import static top.mrxiaom.pluginbase.func.language.LanguageEnumAutoHolder.wrap;

public enum Messages implements IHolderAccessor {
    npc__not_selected("&e你应该先选择一个NPC &7(/npc sel)"),
    npc__blueprint_not_found("&e找不到指定的模型"),
    npc__model_set("&a已设置 NPC 模型为&e %model%&a! 推荐进行以下操作",
            "&f-&b /npc hidename",
            "&f-&b /npc hologram add 头顶标签显示"),
    npc__model_reset("&a已重置 NPC 模型"),
    npc__not_found("&e找不到指定的NPC &b%npc_id%"),
    npc__model_not_found("&e指定的NPC &b%npc_name%&r&7 (%npc_id%) &e没有在世界上生成，或者没有设置模型"),
    npc__animation_not_found("&e找不到NPC &b%npc_name%&r&7 (%npc_id%) &e的模型动画&b %animation%"),
    commands__reload("&a配置文件已重载"),
    commands__help("&b&lCitizensModels&r",
            "&f/npcm set <蓝图> &e设置已选中的NPC的模型",
            "&f/npcm reset &e重置已选中的NPC的模型",
            "&f/npcm ani <npcId> <动画名> &e播放NPC动画",
            "&f/npcm reload &e重载插件配置文件"),

    ;
    Messages(String defaultValue) {
        holder = wrap(this, defaultValue);
    }
    Messages(String... defaultValue) {
        holder = wrap(this, defaultValue);
    }
    Messages(List<String> defaultValue) {
        holder = wrap(this, defaultValue);
    }
    private final LanguageEnumAutoHolder<Messages> holder;
    public LanguageEnumAutoHolder<Messages> holder() {
        return holder;
    }
}
