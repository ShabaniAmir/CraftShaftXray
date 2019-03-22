package net.thecraftshaft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CmdCSXRatio implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Check permissions
		if(!sender.hasPermission("craftshaft.xray.ratio")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4 You do not have permission to run this command"));
			return true;
		}
		
		// Invalid arguments
		if (args.length < 1) return false;
		
		if (ActionListener.playerData.containsKey(args[0])) {
			double ratio = (double) ActionListener.playerData.get(args[0]).get("diamond")
					/ (double) ActionListener.playerData.get(args[0]).get("stone");
			CraftShaftXray.displayRatioOfPlayer(sender, args[0], ratio);
			return true;
		}else {
			sender.sendMessage("Player not found");
		}
		return true;
		
	}
	
	
}
