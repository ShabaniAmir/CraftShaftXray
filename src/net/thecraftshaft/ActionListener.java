package net.thecraftshaft;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class ActionListener implements Listener {
	// If Ratio of Diamond to Stone is higher than 0.7, player is probably cheating
	
	public static Map<String, Map<String, Integer>> playerData;	// Keeps track of what player has mined in here
	private FileConfiguration config;	//	Used for easier access to the config file
	private Map<String, Integer> alertTimeLog;	// Used so server isn't spammed with every block that is broken

	public ActionListener() {
		// Initializing variables
		playerData = new HashMap<String, Map<String, Integer>>();
		this.config = CraftShaftXray.config;
		alertTimeLog = new HashMap<String, Integer>();
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		// When player leaves, delete their data to save space
		if(playerData.containsKey(event.getPlayer().getName())) playerData.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		// Is player in a world that is ignored in config?
		if(config.contains("ignore-worlds") && config.getStringList("ignore-worlds").contains(event.getPlayer().getWorld().getName())) return;
		// Determine concern of plugin, ignore people with permission to bypass or people in creative
		if(event.getPlayer().hasPermission("craftshaft.xray.bypass") || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		
		// Check if the block that was broken is stone or diamond
		if (event.getBlock().getType().equals(Material.DIAMOND_ORE)
				|| event.getBlock().getType().equals(Material.STONE)) {
			
			// Check if player is already in playerData, if not, add them
			if(!playerData.containsKey(event.getPlayer().getName())) {
				Map<String, Integer> miningData = new HashMap<String, Integer>();
				miningData.put("stone", 0);
				miningData.put("diamond", 0);
				playerData.put(event.getPlayer().getName(), miningData);
			}
			
			// Get the data saved so far for the player
			Map<String, Integer> miningData = playerData.get(event.getPlayer().getName());
			int diamondData = (int) miningData.get("diamond");
			int stoneData = (int) miningData.get("stone");
			
			// Update the data If block mined is diamond
			if (event.getBlock().getType().equals(Material.DIAMOND_ORE)) diamondData++;
			// If block mined is stone
			else if (event.getBlock().getType().equals(Material.STONE))	stoneData++;
			
			// Put the updated data back in the array
			miningData.put("diamond", diamondData);
			miningData.put("stone", stoneData);
			playerData.put(event.getPlayer().getName(), miningData);

			// Calculate the ratio and take necessary actions
			calculateRatio(diamondData, stoneData, event.getPlayer().getName());
		}

	}
	
	private void calculateRatio(int diamondData, int stoneData, String name) {
		double ratio = (double) diamondData / (double) stoneData;
		if (ratio > config.getDouble("ratio-threshold")) {
			if (alertTimeLog.containsKey(name)) {
				int timeNow = (int) (System.currentTimeMillis() / 1000);
				if((timeNow - alertTimeLog.get(name)) >= CraftShaftXray.config.getInt("minimum-alert-delay")) {
					alertTimeLog.put(name, timeNow);
					sendAlert(name, ratio);

				}
			}else {
				int timeNow = (int) (System.currentTimeMillis() / 1000);
				alertTimeLog.put(name, timeNow);
				sendAlert(name, ratio);

			}
			
		}
	}
	
	private void sendAlert(String name, double ratio) {
		// Get messages from config
		String configMessage = config.getConfigurationSection("messages").getString("alert");
		
		// Parse placeholders
		configMessage = configMessage.replaceAll("\\{prefix\\}", CraftShaftXray.prefix);
		configMessage = configMessage.replaceAll("\\{name\\}", name);
		configMessage = configMessage.replaceAll("\\{ratio\\}", String.format("%.2f", ratio));
		
		// Add color to the message
		String message = ChatColor.translateAlternateColorCodes('&', configMessage);
		
		// Broadcast message to anyone with the permission
		Bukkit.broadcast(message, "craftshaft.xray.alert");
		
		// This section is for logging to console, checking if its enabled and if color should be added
		if(config.getBoolean("log-to-console")) {
			if(!config.getBoolean("console-colors")) message = ChatColor.stripColor(message);
			Bukkit.getConsoleSender().sendMessage(message);
		}
		
	}
}
