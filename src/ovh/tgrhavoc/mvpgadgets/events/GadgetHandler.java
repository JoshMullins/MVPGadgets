package ovh.tgrhavoc.mvpgadgets.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
	public void playerJoinEvent(PlayerJoinEvent e){
		//Used to give the player the GUI when they spawn
		for (String str : ref.getConfig().getStringList("guiGadgetWorlds"))
			if (e.getPlayer().getWorld().getName().equals(str)) {
				int slot;
				if (ref.getConfig().getInt("guiGadgetSlot") < 1)
					slot = 0;
				else if (ref.getConfig().getInt("guiGadgetSlot") > 9)
					slot = 8;
				else slot = ref.getConfig().getInt("guiGadgetSlot") - 1;
				
				GUIGadget guiGadget = new GUIGadget(ref);
				e.getPlayer().getInventory().setItem(slot, guiGadget.getItemStack());
				for (Gadget gadget : ref.getGadgets()){
					try {
						gadget.getClass().getConstructor(MVPGadgets.class, UUID.class).newInstance(
								gadget.getPlugin(), e.getPlayer().getUniqueId());
					} catch (Exception ex){ ex.printStackTrace(); }
				}
					
				break;
			}
		
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (player.getItemInHand().hasItemMeta()){
				if (player.getItemInHand().getItemMeta().getDisplayName().contains("(Gadget)")){
					if (ref.getConfig().getStringList("guiGadgetWorlds").contains(player.getWorld().getName())) {
						
						try {
							
							for (Gadget g : Gadget.getOwnerGadgets(player.getUniqueId())){
								if (g.getItemStack().getItemMeta().getDisplayName().equals(
										player.getItemInHand().getItemMeta().getDisplayName())){
									GadgetEvent e1 = new GadgetEvent(g, player);
									Bukkit.getPluginManager().callEvent(e1);
									e.setCancelled(true);
								}
							}
							
						} catch (NullPointerException npe) {
							// player probably didn't log into the server via a hub world listed in the config, so gadgets didn't register
							// either that, or the server reloaded with players online
							for (Gadget gadget : ref.getGadgets()){
								try {
									gadget.getClass().getConstructor(MVPGadgets.class, UUID.class).newInstance(
											gadget.getPlugin(), player.getUniqueId());
								} catch (Exception ex){ ex.printStackTrace(); }
							}
							
							// Hopefully fixed the problem, now retry the GadgetEvent firing
							for (Gadget g : Gadget.getOwnerGadgets(player.getUniqueId())){
								if (g.getItemStack().getItemMeta().getDisplayName().equals(
										player.getItemInHand().getItemMeta().getDisplayName())){
									GadgetEvent e1 = new GadgetEvent(g, player);
									Bukkit.getPluginManager().callEvent(e1);
									e.setCancelled(true);
								}
							}
							
						}
						
					} else if (!ref.getMessageFromConfig("Messages.WRONG_WORLD").isEmpty())
							player.sendMessage(ref.getMessageFromConfig("Messages.WRONG_WORLD"));
				}
			}
		}
	}

}
