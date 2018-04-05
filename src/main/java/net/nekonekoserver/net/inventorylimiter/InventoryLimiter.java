package net.nekonekoserver.net.inventorylimiter;

import lombok.Getter;
import net.nekonekoserver.net.inventorylimiter.configure.YamlConfig;
import net.nekonekoserver.net.inventorylimiter.listener.InventoryListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryLimiter extends JavaPlugin {

    @Getter
    private YamlConfig yamlConfig;
    private InventoryListener listener;

    @Override
    public void onEnable() {
        yamlConfig = new YamlConfig(this);
        yamlConfig.load();
        listener = new InventoryListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/ilimit reload");
            sender.sendMessage("/ilimit give");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            yamlConfig.load();
            sender.sendMessage(String.format("[%s]: 設定ファイルの再読み込みに成功", getName()));
        } else if (args[0].equalsIgnoreCase("give")) {
            getServer().getOnlinePlayers().forEach(listener::reset);
            sender.sendMessage(String.format("[%s]: 制限を更新しました", getName()));
        }
        return true;
    }
}
