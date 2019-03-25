package net.thecraftshaft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class CmdCSXSetRatio implements CommandExecutor{
	
	public Plugin plugin;
	
	public CmdCSXSetRatio(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("craftshaft.xray.setratio")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getConfigurationSection("errors").getString("permission")));
			return true;
		}
		
		// Set ratio
		if(args.length > 0) {
			double newRatio;
			try {
				newRatio = Double.parseDouble(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getConfigurationSection("errors").getString("not-ratio")));
				return false;
			}
			CraftShaftXray.config.set("ratio-threshold", newRatio);
			this.plugin.saveConfig();
			
		}
		String message = plugin.getConfig().getConfigurationSection("messages").getString("get-ratio");
		message = message.replaceAll("\\{prefix\\}", CraftShaftXray.prefix);
		message = message.replaceAll("\\{ratio\\}", String.valueOf(CraftShaftXray.config.getDouble("ratio-threshold")));
		sender.sendMessage(message);
		
		return true;
	}
	
}
