package pl.konradmaksymilian;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class RuleThemAll extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Starting RuleThemAll Paper Permission Plugin...");
        prepareConfig();
        setUpDBConnection();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Stopping RuleThemAll Paper Permission Plugin...");
    }

    private void prepareConfig() {

    }

    private void setUpDBConnection() {

    }
}
