package top.mrxiaom.citizensmodels.func;
        
import top.mrxiaom.citizensmodels.CitizensModels;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder extends top.mrxiaom.pluginbase.func.AbstractPluginHolder<CitizensModels> {
    public AbstractPluginHolder(CitizensModels plugin) {
        super(plugin);
    }

    public AbstractPluginHolder(CitizensModels plugin, boolean register) {
        super(plugin, register);
    }
}
