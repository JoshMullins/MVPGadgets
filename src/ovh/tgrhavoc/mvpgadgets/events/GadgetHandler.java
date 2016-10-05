package ovh.tgrhavoc.mvpgadgets.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
/**
 * Handles the main events for this plugin.
 * Events handled by this class:
 * 		GadgetEvent - Executes the gadget (Calls {@link ovh.tgrhavoc.mvpgadgets.gadgets.Gadget#execute(org.bukkit.entity.Player) Gadget#execute})
 * 		PlayerJoinEvent - Adds the gadget selector to the player's inventory when they join
 * 		PlayerInteractEvent - Calls the GadgetEvent event
 * 		
 * @author Jordan Dalton
 *
 */
public class GadgetHandler implements Listener{
	
	MVPGadgets ref;
	
	public GadgetHandler(MVPGadgets main){
		ref = main;
	}
	
	@EventHandler
	public void gadgetEvent (final GadgetEvent e){
		e.getGadget().execute(e.getPlayer());
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		ItemStack dropped = e.getItemDrop().getItemStack();
		if(dropped.hasItemMeta()){
			if(dropped.getItemMeta().getDisplayName().contains("(Gadget)")){
				if (!(ref.getConfig().getBoolean("allowDropping"))){
					e.setCancelled(true);
					e.getPlayer().sendMessage(ref.getMessageFromConfig("Messages.NO_THROW"));
				}
			}
		}
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e){
		//Used to give the player the GUI when they spawn
		int slot;
		// We want to check equality as well, someone might think 1 = the first slot. and 9 = the last slot.
		if (ref.getConfig().getInt("guiGadgetSlot") <= 1)
			slot = 0;
		else if (ref.getConfig().getInt("guiGadgetSlot") >= 9)
			slot = 8;
		else slot = ref.getConfig().getInt("guiGadgetSlot") - 1;
		
		GUIGadget guiGadget = new GUIGadget(ref);
		
		if (ref.getConfig().getStringList("guiGadgetWorlds").size() == 0){ // If they have an empty list
			e.getPlayer().getInventory().setItem(slot, guiGadget.getItemStack() );
			return;
		}
		
		boolean foundWorld = false;
		for (String str : ref.getConfig().getStringList("guiGadgetWorlds")){
			if ( ! (e.getPlayer().getWorld().getName().equals(str)) ){
				continue;
			}
			
			e.getPlayer().getInventory().setItem(slot, guiGadget.getItemStack() );
			foundWorld = true;
		}
		
		if (!foundWorld)
			e.getPlayer().sendMessage(ref.getMessageFromConfig("Messages.WRONG_WORLD"));
		
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (player.getItemInHand().hasItemMeta()){
				if (player.getItemInHand().getItemMeta().getDisplayName().contains("(Gadget)")){
					// If the server doesn't want a hub world.. Or the player is in the specified world.
					if (	ref.getConfig().getStringList("guiGadgetWorlds").size() == 0 ||
							ref.getConfig().getStringList("guiGadgetWorlds").contains(player.getWorld().getName())) {
						
						for (Gadget g : ref.getGadgets() ){
							if (g.getItemStack().getItemMeta().getDisplayName().equals(
									player.getItemInHand().getItemMeta().getDisplayName())){
								GadgetEvent e1 = new GadgetEvent(g, player);
								Bukkit.getPluginManager().callEvent(e1);
								e.setCancelled(true);
							}
						}
						
					} else if (!ref.getMessageFromConfig("Messages.WRONG_WORLD").isEmpty())
							player.sendMessage(ref.getMessageFromConfig("Messages.WRONG_WORLD"));
				}
			}
		}
	}

}
