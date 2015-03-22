package ovh.tgrhavoc.mvpgadgets.gadgets.guigadget;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

public class GUIGadgetListener implements Listener {
	
	static Inventory guiInv;
	
	MVPGadgets mainPlugin;
	Gadget guiGadget;

	public GUIGadgetListener(MVPGadgets mainPlugin, Gadget guiGadget) {
		this.mainPlugin = mainPlugin; 
		this.guiGadget = guiGadget;
		
		int slot = 0;
		//Bukkit.broadcastMessage( ((16/9) +1) +"");
		guiInv = Bukkit.createInventory(null, (9 * ((mainPlugin.getGadgets().size()/9)+1)),
				guiGadget.getInvTextFromConfig() );
		for (Gadget g: mainPlugin.getGadgets()){
			//System.out.println("Gadget looped: " + g.getNamePath());
			if (!g.isGUI){
				addItemToInv(g.getItemStack(), slot);
				slot++;
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if (event.getInventory().getName().equals(guiInv.getName())){
			event.setCancelled(true);
			
			if ( event.getRawSlot() > (guiInv.getSize()))
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
	
	public static Inventory getInv(){
		return guiInv;
	}
	
	public Inventory getGUIInv(){
		return guiInv;
	}
	
	public void addItemToInv(ItemStack i, int slot){
		guiInv.setItem(slot, i);
	}

}
