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

public class DisguiseListener implements Listener{
	
	MVPGadgets plugin; 
	DisguiseGadget disguiseGadget;
	
	Map<Player, MyDisguise> disguised = new HashMap<Player, MyDisguise>();
	
	public DisguiseListener(MVPGadgets plugin, DisguiseGadget gadget) {
		this.plugin = plugin;
		this.disguiseGadget = gadget;
	}
	
	@EventHandler
	public void disguiseChoseEvent(InventoryClickEvent event){
		Inventory check = disguiseGadget.getInv();
		if (event.getInventory().getName().equals( check.getName() )){
			event.setCancelled(true);
			
			
			if ( event.getRawSlot() > ((9 * (((disguiseGadget.getDisguiseList().size()+1)/9)+1))) )
				return;
			
			if (event.getCurrentItem() == null) //Fix for a NPE (when user clicks outside inv)
				return;
			
			System.out.println("Didn't click null item");
			
			if (event.getCurrentItem().hasItemMeta()){
				for (EntityDisguise d: disguiseGadget.getDisguiseList())
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
		for(Entry<Player, MyDisguise> set: disguised.entrySet()){
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
	
	public EntityDisguise getEntityDisguiseFromString(String type){
		EntityDisguise ed = EntityDisguise.getByName(plugin, type);
		return ed;
	}
	
	public void disguise(Player p, String type) {
		if (type.equals("Remove Disguise"))
			removeDisguise(p);
		else
			disguise(p, getEntityDisguiseFromString(type), p.getName());
	}

	public void disguise(Player p, EntityDisguise type, String displayName) {
		if (disguised.containsKey(p)){
			//Update their disguise
			try {
				disguised.get(p).changePlayerDisguise(type, Bukkit.getOnlinePlayers());
			} catch (Exception e) {
				e.printStackTrace();
			}
			p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.DISGUISED").replace("{ENTITY}", type.getName(plugin)));
		}else{
			//Add them with a disguise
			disguised.put(p, new MyDisguise(p, type, displayName));			
			p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.UPDATED").replace("{ENTITY}", type.getName(plugin)));
		}
		
		disguised.get(p).updateDisguise(Bukkit.getOnlinePlayers());
		
	}
	
	public void removeDisguise(Player p){
		if (!disguised.containsKey(p))
			return;
		MyDisguise md = disguised.get(p);
		try {
			md.removeDisguise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		disguised.remove(p);
		p.sendMessage(plugin.getMessageFromConfig("Messages.Disguises.REMOVED"));
	}
	
}
