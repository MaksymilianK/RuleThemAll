package pl.konradmaksymilian;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.konradmaksymilian.persistence.PersistenceManager;

import java.util.logging.Level;

public class RuleThemAll extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Starting RuleThemAll Paper Permission Plugin...");
        FileConfiguration config = setUpConfig();
        setUpPersistence(config);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Stopping RuleThemAll Paper Permission Plugin...");
    }

    private FileConfiguration setUpConfig() {
        FileConfiguration config = getConfig();
        config.addDefault("db.user", "root");
        config.addDefault("db.password", "admin");
        config.addDefault("db.host", "localhost");
        config.addDefault("db.port", 5432);
        config.addDefault("db.name", "arrival");
        config.addDefault("server-id", 1);
        config.options().copyDefaults(true);
        saveConfig();
        return config;
    }

    private void setUpPersistence(FileConfiguration config) {
        PersistenceManager.builder()
                .user(config.getString("db.user"))
                .password(config.getString("db.password"))
                .host(config.getString("db.host"))
                .port(config.getInt("db.port"))
                .name(config.getString("db.name"))
                .server(config.getInt("server-id"))
                .build();
    }
}
