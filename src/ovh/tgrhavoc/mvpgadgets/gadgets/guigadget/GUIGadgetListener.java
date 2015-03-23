package ovh.tgrhavoc.mvpgadgets.gadgets.guigadget;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

public class GUIGadgetListener implements Listener {
		
	MVPGadgets mainPlugin;
	Gadget guiGadget;

	public GUIGadgetListener(MVPGadgets mainPlugin, Gadget guiGadget) {
		this.mainPlugin = mainPlugin; 
		this.guiGadget = guiGadget;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if (event.getInventory().getName().equals( guiGadget.getInvTextFromConfig() )){
			event.setCancelled(true);
			
			if ( event.getRawSlot() > ((9 * ((mainPlugin.getGadgets().size()/9)+1))) )
				return;
			
			if (event.getCurrentItem().hasItemMeta()){
				for (Gadget g: mainPlugin.getGadgets()){
					if (g.getItemStack().getItemMeta().getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName())){
						event.getWhoClicked().getInventory().setItem(5, g.getItemStack());
						event.getWhoClicked().closeInventory();
						//TODO: Use messages.yml for message
						if (event.getWhoClicked() instanceof Player)
							((Player)event.getWhoClicked()).sendMessage(
									formatMessage(mainPlugin.getMessages().getString("Messages.SELECTED"), g));
					}
				}
			}
			
		}
	}
	
	private String formatMessage(String unformatted, Gadget gadget){
		return ChatColor.translateAlternateColorCodes('&', unformatted.replace("{GADGET}", gadget.getNameFromConfig()));
	}
	
	public Inventory getInv(Player player){
		Inventory inv = Bukkit.createInventory(null, (9 * ((mainPlugin.getGadgets().size()/9)+1)),
				guiGadget.getInvTextFromConfig() );
		
		int slot = 0;

		for (Gadget g: mainPlugin.getGadgets()){
			if (!g.isGUI){
				inv.setItem(slot, g.getGUIItem(player));
				slot++;
			}
		}
		
		return inv;
	}
}
