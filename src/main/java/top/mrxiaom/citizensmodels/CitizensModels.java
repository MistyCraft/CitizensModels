package top.mrxiaom.citizensmodels;
        
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

    @Override
    protected void afterEnable() {
        getLogger().info("CitizensModels 加载完毕");
    }
}
