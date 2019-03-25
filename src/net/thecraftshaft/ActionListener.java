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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class ActionListener implements Listener {
	// If Ratio of Diamond to Stone is higher than 0.8, player is probably cheating
	// Array of map
	public static Map<String, Map<String, Integer>> playerData;
	private FileConfiguration config;
	private Map<String, Integer> alertTimeLog;

	public ActionListener() {
		playerData = new HashMap<String, Map<String, Integer>>();
		this.config = CraftShaftXray.config;
		alertTimeLog = new HashMap<String, Integer>();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Initialize Player Mining Data
		Map<String, Integer> miningData = new HashMap<String, Integer>();
		miningData.put("stone", 0);
		miningData.put("diamond", 0);
		playerData.put(event.getPlayer().getName(), miningData);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		playerData.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		// Determine concern of plugin, ignore people with permission to bypass
		if(event.getPlayer().hasPermission("craftshaft.xray.bypass") || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (event.getBlock().getType().equals(Material.DIAMOND_ORE)
				|| event.getBlock().getType().equals(Material.STONE)) {

			Map<String, Integer> miningData = playerData.get(event.getPlayer().getName());
			int diamondData = (int) miningData.get("diamond");
			int stoneData = (int) miningData.get("stone");
			// If block mined is diamond
			if (event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
				diamondData++;
			} // If block mined is stone
			else if (event.getBlock().getType().equals(Material.STONE)) {
				stoneData++;
			}
			miningData.put("diamond", diamondData);
			miningData.put("stone", stoneData);
			playerData.put(event.getPlayer().getName(), miningData);

			double ratio = (double) diamondData / (double) stoneData;
			if (ratio > config.getDouble("ratio-threshold")) {
				if (alertTimeLog.containsKey(event.getPlayer().getName())) {
					int timeNow = (int) (System.currentTimeMillis() / 1000);
					if((timeNow - alertTimeLog.get(event.getPlayer().getName())) >= 30) {
						alertTimeLog.put(event.getPlayer().getName(), timeNow);
						String message = ChatColor.translateAlternateColorCodes('&',
								String.format(
										"%s&e%s &6 is most likely using X-Ray. &r(Diamond to Stone Ratio: &c%.2f&r)",
										CraftShaftXray.prefix, event.getPlayer().getName(), ratio));
						Bukkit.broadcast(message, "craftshaft.xray.alert");
						Bukkit.getLogger().warning(event.getPlayer().getName() + " has an xray ratio of " + ratio);
					}
				}else {
					int timeNow = (int) (System.currentTimeMillis() / 1000);
					alertTimeLog.put(event.getPlayer().getName(), timeNow);
					String message = ChatColor.translateAlternateColorCodes('&',
							String.format(
									"%s&e%s &6 is most likely using X-Ray. &r(Diamond to Stone Ratio: &c%.2f&r)",
									CraftShaftXray.prefix, event.getPlayer().getName(), ratio));
					Bukkit.broadcast(message, "craftshaft.xray.alert");
					Bukkit.getLogger().warning(event.getPlayer().getName() + " has an xray ratio of " + ratio);
				}
				
			}
		}

	}
}
