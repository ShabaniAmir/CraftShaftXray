package net.thecraftshaft;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CmdCSXTop implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check permissions
		if(!sender.hasPermission("craftshaft.xray.top")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4 You do not have permission to run this command"));
			return true;
		}

		Map<String, Double> ratios = renderRatios(ActionListener.playerData);
		Map<String, Double> sortedRatios = sortByValue(ratios);
		
		// TODO: go through the list and call CmdCSXRatio.displayRatioOfPlayer(sender, playerName) for each of the top 5 highest ratios;
		if(sortedRatios.size() < 5) {
			for(String playerName: sortedRatios.keySet()) {
				CraftShaftXray.displayRatioOfPlayer(sender, playerName, sortedRatios.get(playerName));
			}
		} else {
			int counter = 0;
			for(String playerName: sortedRatios.keySet()) {
				CraftShaftXray.displayRatioOfPlayer(sender, playerName, sortedRatios.get(playerName));
				counter++;
				if(counter >= 5) {
					return true;
				}
			}
		}
		
		return true;
	}
	
	// Create map of all players and their ratios
	private Map<String, Double> renderRatios(Map<String, Map<String, Integer>> playerData){
		Map<String, Double> renderedRatios = new HashMap<String, Double>();
		
		// Iterate through Players in playerData
		for(String playerName: playerData.keySet()) {
			Map<String, Integer> miningData = playerData.get(playerName);
			double ratio = (double) miningData.get("diamond") / (double) miningData.get("stone");
			renderedRatios.put(playerName, ratio);
		}
		return renderedRatios;
	}

	// Sort Ratios
    public static Map<String, Double> sortByValue(Map<String, Double> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Double> > list = 
               new LinkedList<Map.Entry<String, Double> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
            public int compare(Map.Entry<String, Double> o1,  
                               Map.Entry<String, Double> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>(); 
        for (Map.Entry<String, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
	

}
