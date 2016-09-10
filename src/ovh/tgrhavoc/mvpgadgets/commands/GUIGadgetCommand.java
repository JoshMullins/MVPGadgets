package ovh.tgrhavoc.mvpgadgets.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
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
				try {
					// don't want to open a gadget GUI if the player isn't in a hub world
					if (plugin.getConfig().getStringList("guiGadgetWorlds").contains(player.getWorld().getName())) {
						player.openInventory(GUIGadgetListener.getInv(player));
						int slot;
						if (plugin.getConfig().getInt("guiGadgetSlot") < 1)
							slot = 0;
						else if (plugin.getConfig().getInt("guiGadgetSlot") > 9)
							slot = 8;
						else slot = plugin.getConfig().getInt("guiGadgetSlot") - 1;
						
						GUIGadget guiGadget = new GUIGadget(plugin);
						player.getInventory().setItem(slot, guiGadget.getItemStack());
					} else if (!plugin.getMessageFromConfig("Messages.WRONG_WORLD").isEmpty())
						player.sendMessage(plugin.getMessageFromConfig("Messages.WRONG_WORLD"));
				} catch (NullPointerException npe) {
					// player probably didn't log into the server via a hub world listed in the config, so gadgets didn't register
					// either that, or the server reloaded with players online
					for (Gadget gadget : plugin.getGadgets()){
						try {
							gadget.getClass().getConstructor(MVPGadgets.class, UUID.class).newInstance(
									gadget.getPlugin(), player.getUniqueId());
						} catch (Exception ex){ ex.printStackTrace(); }
					}
					
					// yay, copy-pasted code because a tired programmer is a lazy one
					// but hey, a lazy programmer is a great one am i rite
					if (plugin.getConfig().getStringList("guiGadgetWorlds").contains(player.getWorld().getName())) {
						player.openInventory(GUIGadgetListener.getInv(player));
						int slot;
						if (plugin.getConfig().getInt("guiGadgetSlot") < 1)
							slot = 0;
						else if (plugin.getConfig().getInt("guiGadgetSlot") > 9)
							slot = 8;
						else slot = plugin.getConfig().getInt("guiGadgetSlot") - 1;
						
						GUIGadget guiGadget = new GUIGadget(plugin);
						player.getInventory().setItem(slot, guiGadget.getItemStack());
					} else if (!plugin.getMessageFromConfig("Messages.WRONG_WORLD").isEmpty())
						player.sendMessage(plugin.getMessageFromConfig("Messages.WRONG_WORLD"));
					
				}
			}
		}
		
		return true;
	}

}
