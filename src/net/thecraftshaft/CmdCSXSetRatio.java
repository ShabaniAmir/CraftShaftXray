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
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4 You do not have permission to run this command"));
			return true;
		}
		
		// Set ratio
		if(args.length > 0) {
			double newRatio;
			try {
				newRatio = Double.parseDouble(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe value you entered is not a ratio"));
				return false;
			}
			CraftShaftXray.config.set("ratio-threshold", newRatio);
			this.plugin.saveConfig();
			
		}
		sender.sendMessage("Current ratio threshold: " + CraftShaftXray.config.getDouble("ratio-threshold"));
		
		return true;
	}
	
}
