package ovh.tgrhavoc.mvpgadgets.events;

import org.bukkit.Bukkit;
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
		e.getPlayer().getInventory().setItem(4, (new GUIGadget(ref)).getItemStack());
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (e.getPlayer().getItemInHand().hasItemMeta()){
				if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("(Gadget)")){
					for (Gadget g: ref.getGadgets()){
						if (g.getItemStack().getItemMeta().getDisplayName().equals(e.getPlayer().getItemInHand().getItemMeta().getDisplayName())){
							GadgetEvent e1 = new GadgetEvent(g, e.getPlayer());
							Bukkit.getPluginManager().callEvent(e1);
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

}
