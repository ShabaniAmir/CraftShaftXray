package net.thecraftshaft;


import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CraftShaftXray extends JavaPlugin{
	public static String prefix;
	public static FileConfiguration config;
	@Override
	public void onEnable() {
		this.prefix = ChatColor.translateAlternateColorCodes('&', "&4&l[XRAY]&r ");
		this.config = this.getConfig();
		
		this.saveDefaultConfig();
		// Register Commands
		this.getCommand("csxratio").setExecutor(new CmdCSXRatio());
		this.getCommand("csxtop").setExecutor(new CmdCSXTop());
		this.getCommand("csxsetratio").setExecutor(new CmdCSXSetRatio(this));
		// Register Listener
		getServer().getPluginManager().registerEvents(new ActionListener(), this);
		
	}
	@Override
	public void onDisable() {
		
	}
	
	public static boolean displayRatioOfPlayer(CommandSender sender, String playerName, double ratio) {
		String strRatio;
		if (ratio > CraftShaftXray.config.getDouble("ratio-threshold")) {
			strRatio = ChatColor.translateAlternateColorCodes('&', String.format("&c%.2f&r", ratio));
		}else {
			strRatio = ChatColor.translateAlternateColorCodes('&', String.format("&r%.2f&r", ratio));
		}
		sender.sendMessage(CraftShaftXray.prefix + playerName + "'s ratio is " + strRatio);
		return true;
	}
	
	
}
