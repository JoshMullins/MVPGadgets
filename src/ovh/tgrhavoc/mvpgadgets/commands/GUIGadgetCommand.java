package ovh.tgrhavoc.mvpgadgets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadgetListener;

public class GUIGadgetCommand implements CommandExecutor {
	
	MVPGadgets plugin;
	
	public GUIGadgetCommand(MVPGadgets plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gadget")) {
			if (!sender.hasPermission("mvpgadgets.gadget")) {
				sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getMessageFromConfig("Messages.CONSOLE"));
				return true;
			}
			else {
				Player player = (Player) sender;
				
				// don't want to open a gadget GUI if the player isn't in a hub world
				if (	plugin.getConfig().getStringList("guiGadgetWorlds").size() == 0 ||
						plugin.getConfig().getStringList("guiGadgetWorlds").contains(player.getWorld().getName())) {
					player.openInventory(GUIGadgetListener.getInv(player));
					
					int slot;
					if (plugin.getConfig().getInt("gadgetSlot") <= 1)
						slot = 0;
					else if (plugin.getConfig().getInt("gadgetSlot") >= 9)
						slot = 8;
					else slot = plugin.getConfig().getInt("gadgetSlot") - 1;
					
					GUIGadget guiGadget = new GUIGadget(plugin);
					player.getInventory().setItem(slot, guiGadget.getItemStack());
					
				} else if (!plugin.getMessageFromConfig("Messages.WRONG_WORLD").isEmpty())
					player.sendMessage(plugin.getMessageFromConfig("Messages.WRONG_WORLD"));
				
			}
		}
		
		return true;
	}

}
