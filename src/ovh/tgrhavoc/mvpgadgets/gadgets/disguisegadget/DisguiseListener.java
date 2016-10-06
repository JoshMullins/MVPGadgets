package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.AbstractDisguise;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.DisguiseFactory;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.EntityDisguises;

/**
 * Event listener for the {@link DisguiseGadget DisguiseGadget}
 * 
 * @author Jordan Dalton
 *
 */
public class DisguiseListener implements Listener{
	
	MVPGadgets plugin; 
	DisguiseGadget disguiseGadget;
	
	Map<Player, AbstractDisguise> disguised = new HashMap<Player, AbstractDisguise>();
	
	public DisguiseListener(MVPGadgets plugin, DisguiseGadget gadget) {
		this.plugin = plugin;
		this.disguiseGadget = gadget;
	}
	
	@EventHandler
	public void disguiseChooseEvent(InventoryClickEvent event){
		Inventory check = disguiseGadget.getInv();
		if (event.getInventory().getName().equals( check.getName() )){
			event.setCancelled(true);
			if ( event.getRawSlot() > ((9 * (((disguiseGadget.getDisguiseList().size()+1)/9)+1))) )
				return;
			
			try {
			if (event.getCurrentItem() == null)
				return;
			} catch (NullPointerException ex){ return; }
			
			if (event.getCurrentItem().hasItemMeta()){
				for (EntityDisguises d: disguiseGadget.getDisguiseList())
					if (d.getName(plugin).equals(event.getCurrentItem().getItemMeta().getDisplayName())
							|| event.getCurrentItem().getItemMeta().getDisplayName().equals("Remove Disguise")){
						disguise((Player)event.getWhoClicked(), event.getCurrentItem().getItemMeta().getDisplayName());
					}else
						continue;
			}
			
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		for(Entry<Player, AbstractDisguise> set: disguised.entrySet()){
			try {
				set.getValue().sendDisguise(e.getPlayer());//Make sure new players see disguises.
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		if (disguised.containsKey(e.getPlayer())){
			try {
				disguised.get(e.getPlayer()).removeDisguise();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			disguised.remove(e.getPlayer());
		}
	}
	
	public EntityDisguises getEntityDisguiseFromString(String type){
		EntityDisguises ed = EntityDisguises.getByMessageName(plugin, type);
		return ed;
	}
	
	public void disguise(Player p, String type) {
		if (type.equals("Remove Disguise"))
			removeDisguise(p);
		else
			disguise(p, getEntityDisguiseFromString(type), p.getName());
	}

	public void disguise(Player p, EntityDisguises type, String displayName) {
		if (disguised.containsKey(p)){
			//Update their disguise
			try {
				disguised.get(p).changeDisguise(type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.UPDATED").replace("{ENTITY}", type.getName(plugin)));
		}else{
			//Add them with a disguise
			AbstractDisguise playerDisguise = DisguiseFactory.getDisguiseFor(p, type, MVPGadgets.getNmsVersion() );
			
			if (playerDisguise != null)
				disguised.put( p, playerDisguise );
			//else, they should have already received a player#message telling them what went wrong.
			
			p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.DISGUISED").replace("{ENTITY}", type.getName(plugin)));
		}
		
		// Bukkit.broadcastMessage(disguised.get(p).getDisguise().getClassName());
		
		if (disguised.containsKey(p)){
			disguised.get(p).updateDisguise(Bukkit.getOnlinePlayers());
		}
		
	}
	
	public void removeDisguise(Player p){
		if (!disguised.containsKey(p))
			return;
		AbstractDisguise md = disguised.get(p);
		try {
			md.removeDisguise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		disguised.remove(p);
		p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.REMOVED"));
	}
	
}
