package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
/**
 * Disguise gadget.
 * Allows players to disguise themselves as any mob.
 * 
 * @author Jordan Dalton
 *
 */
public class DisguiseGadget extends Gadget {
	/**
	 * List of disguises the player can use.
	 */
	List<EntityDisguise> disguiseList = new ArrayList<EntityDisguise>();
	
	public DisguiseGadget(MVPGadgets plugin, UUID owningPlayer) {
		super(plugin, "disguiseGadget", new ItemStack(Material.SKULL_ITEM), owningPlayer);
		
		for (String s: plugin.getConfig().getStringList("Disguises_List")){
			disguiseList.add(EntityDisguise.valueOf(s));
		}
	}
	
	public DisguiseGadget(MVPGadgets plugin) {
		super(plugin, "disguiseGadget", new ItemStack(Material.SKULL_ITEM));
		
		for (String s: plugin.getConfig().getStringList("Disguises_List")){
			disguiseList.add(EntityDisguise.valueOf(s));
		}
	}
	
	@Override
	public void execute(Player player) {
		player.openInventory(getInv());
	}
	
	/**
	 * Get the inventory that this gadget uses to allow the player to select their disguise.
	 * @return Inventory the player uses to select their disguise
	 */
	public Inventory getInv(){
		Inventory inv = Bukkit.createInventory(null, (9 * (((disguiseList.size()+1)/9)+1)),
				this.getMessageFromMessages("inventory_name"));
		int slot = 0;
		for (EntityDisguise disguise: disguiseList){
			ItemStack i = new ItemStack(Material.SKULL_ITEM);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(disguise.getName(getPlugin()));
			i.setItemMeta(m);
			inv.setItem(slot, i);
			slot ++;
		}
		//Allow player to undisguise
		ItemStack i = new ItemStack(Material.SKULL_ITEM);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName("Remove Disguise");
		i.setItemMeta(m);
		inv.setItem(slot, i);
		
		return inv;
	}
	
	@Override
	public void registerEvents(MVPGadgets plugin, PluginManager pm) {
		pm.registerEvents(new DisguiseListener(plugin, this), plugin);
	}
	
	/**
	 * Get the list if disguises the player can use.
	 * @return List<EntityDisguise> the player can use.
	 */
	public List<EntityDisguise> getDisguiseList() {
		return disguiseList;
	}
}