package net.nekonekoserver.net.inventorylimiter.configure;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nekoneko on 2017/11/10.
 */
@Data
public class YamlConfig extends Utf8YamlConfiguration {

    private final File ymlFile;
    private final List<Integer> filteredSlots = new ArrayList<>();

    public YamlConfig(@NonNull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.ymlFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public boolean load() {
        filteredSlots.clear();
        try {
            load(ymlFile);

            List<String> input = getStringList("filtered-slots");
            if (input != null) {
                for (String s : input) {
                    try {
                        filteredSlots.add(Integer.valueOf(s));
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            return true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return false;
    }

//    public void save() {
//        try {
//            set("filtered-slots", filteredSlots);
//            save(ymlFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

