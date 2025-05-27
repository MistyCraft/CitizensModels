package top.mrxiaom.citizensmodels;
        
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import top.mrxiaom.citizensmodels.api.IModelEngine;
import top.mrxiaom.citizensmodels.meg.v3.ModelEngineV3;
import top.mrxiaom.citizensmodels.meg.v4.ModelEngineV4;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;

public class CitizensModels extends BukkitPlugin {
    public static CitizensModels getInstance() {
        return (CitizensModels) BukkitPlugin.getInstance();
    }

    public CitizensModels() {
        super(options()
                .bungee(false)
                .adventure(false)
                .database(false)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.citizensmodels.libs")
                .disableDefaultConfig(true)
        );
        this.scheduler = new FoliaLibScheduler(this);
    }
    private IModelEngine modelEngine;
    public IModelEngine getModelEngine() {
        return modelEngine;
    }

    @Override
    @SuppressWarnings({"deprecation"})
    public void onEnable() {
        if (initModelEngine()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        super.onEnable();
    }

    private boolean initModelEngine() {
        String megVersion = getModelEngineVersion();
        if (megVersion.startsWith("4.")) {
            modelEngine = new ModelEngineV4(getScheduler()::runTask);
            return false;
        }
        if (megVersion.startsWith("3.")) {
            modelEngine = new ModelEngineV3(getScheduler()::runTask);
            return false;
        }
        warn("当前 ModelEngine 版本 (" + megVersion + ") 不受支持!");
        return true;
    }

    private String getModelEngineVersion() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ModelEngine");
        if (plugin == null) return "unknown";
        String ver = plugin.getDescription().getVersion();
        if (ver.startsWith("R")) {
            return ver.substring(1);
        }
        return ver;
    }

    @Override
    protected void afterEnable() {
        getLogger().info("CitizensModels 加载完毕");
    }

    @Override
    protected void afterDisable() {
        if (modelEngine != null) {
            modelEngine.onDisable();
            modelEngine = null;
        }
    }
}
