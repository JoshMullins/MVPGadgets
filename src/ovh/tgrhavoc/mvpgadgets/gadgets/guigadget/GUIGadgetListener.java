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
import ovh.tgrhavoc.utils.VaultUtil;

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
						

						if (event.getWhoClicked() instanceof Player){
							if(mainPlugin.hookedVault() && !event.getWhoClicked().hasPermission("mvpgadgets." + g.getGadgetName())){
								if (!VaultUtil.transaction((Player)event.getWhoClicked(), mainPlugin.getGadgetPrice(g))){
									((Player)event.getWhoClicked()).sendMessage(
											formatMessage(mainPlugin.getMessageFromConfig("Messages.UNABLE_BUY"), g));
									return;
								}else{
									mainPlugin.getPermission().playerAdd((Player)event.getWhoClicked(), "mvpgadgets." +g.getGadgetName());
									((Player)event.getWhoClicked()).sendMessage(
											formatMessage(mainPlugin.getMessageFromConfig("Messages.BOUGHT"), g));
								}
							}
							
							((Player)event.getWhoClicked()).sendMessage(
									formatMessage(mainPlugin.getMessages().getString("Messages.SELECTED"), g));
							event.getWhoClicked().getInventory().setItem(5, g.getItemStack());
						}else{
							((Player)event.getWhoClicked()).sendMessage(
									mainPlugin.getMessageFromConfig("Messages.MobCannon.NO_PERMISSION")); //CBA making a new node for this message, just going to use the already made one :P
						}
						event.getWhoClicked().closeInventory();
					}
				}
			}
			
		}
	}
	
	private String formatMessage(String unformatted, Gadget gadget){
		return ChatColor.translateAlternateColorCodes('&',
				unformatted.replace("{GADGET}", gadget.getNameFromConfig())
							.replace("{COST}", mainPlugin.getGadgetPrice(gadget.getGadgetName())+"") );
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
