package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

public class DisguiseGadget extends Gadget {
	//This gadget will also have a GUI selection 
	
	List<String> disguiseList = new ArrayList<String>();
	
	public DisguiseGadget(MVPGadgets plugin) {
		super(plugin, "disguisegadget", new ItemStack(Material.SKULL_ITEM));
		
		disguiseList = plugin.getConfig().getStringList("Disguises_List");
	}
	
	@Override
	public void execute(Player player) {
		player.openInventory(getInv());
	}
	
	public Inventory getInv(){
		Inventory inv = Bukkit.createInventory(null, (9 * (((disguiseList.size()+1)/9)+1)),
				this.getMessageFromMessages("inventory_name"));
		int slot = 0;
		for (String disguise: disguiseList){
			ItemStack i = new ItemStack(Material.SKULL_ITEM);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(disguise);
			i.setItemMeta(m);
			inv.setItem(slot, i);
			slot ++;
		}
		//Allow player to unisguise
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

	public List<String> getDisguiseList() {
		return disguiseList;
	}
}
