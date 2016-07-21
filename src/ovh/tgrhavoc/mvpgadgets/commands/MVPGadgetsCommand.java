package ovh.tgrhavoc.mvpgadgets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

public class MVPGadgetsCommand implements CommandExecutor {
	
	private MVPGadgets plugin;
	
	public MVPGadgetsCommand(MVPGadgets plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mvpgadgets")) {
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("mvpgadgets.reload")) {
					sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
					return true;
				}
				plugin.reloadConfig();
				plugin.initConfigs();
				sender.sendMessage(plugin.getMessageFromConfig("Messages.RELOADED"));
			}
		}
		
		return true;
	}
	
}
