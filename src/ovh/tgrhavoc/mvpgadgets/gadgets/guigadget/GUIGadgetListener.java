package ovh.tgrhavoc.mvpgadgets.gadgets.guigadget;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.utils.VaultUtil;

public class GUIGadgetListener implements Listener {
		
	MVPGadgets mainPlugin;
	Gadget guiGadget;
	
	private static MVPGadgets staticMainPlugin;
	private static Gadget staticGuiGadget;

	public GUIGadgetListener(MVPGadgets mainPlugin, Gadget guiGadget) {
		this.mainPlugin = mainPlugin; 
		this.guiGadget = guiGadget;
		
		staticMainPlugin = mainPlugin;
		staticGuiGadget = guiGadget;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if (event.getInventory().getName().equals( guiGadget.getInvTextFromConfig() )){
			event.setCancelled(true);
			
			if ( event.getRawSlot() > (9 * ((mainPlugin.getGadgets().size()/9)+1)) )
				return;
			
			try {
			if (event.getCurrentItem() == null)
				return;
			} catch (NullPointerException ex) { return; }
			
			if (event.getCurrentItem().hasItemMeta()){
				if (event.getWhoClicked() instanceof Player){
					Player player = (Player) event.getWhoClicked();
					
					for (Gadget g : mainPlugin.getGadgets() ){
						if (g.getItemStack().getItemMeta().getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName())){
							
							if(mainPlugin.hookedVault() && !player.hasPermission("mvpgadgets." + g.getGadgetName())){
								
								if (!VaultUtil.transaction(player, mainPlugin.getGadgetPrice(g))){
									player.sendMessage(formatMessage(mainPlugin.getMessageFromConfig("Messages.UNABLE_BUY"), g));
									return;
								}else{
									mainPlugin.getPermission().playerAdd(player, "mvpgadgets." +g.getGadgetName().toLowerCase());
									player.sendMessage(formatMessage(mainPlugin.getMessageFromConfig("Messages.BOUGHT"), g));
								}
									
							}
								
							player.sendMessage(formatMessage(mainPlugin.getMessages().getString("Messages.SELECTED"), g));
								
							int slot;
							if (mainPlugin.getConfig().getInt("gadgetSlot") <= 1)
								slot = 0;
							else if (mainPlugin.getConfig().getInt("gadgetSlot") >= 9)
								slot = 8;
							else slot = mainPlugin.getConfig().getInt("gadgetSlot") - 1;
								
							player.getInventory().setItem(slot, g.getItemStack());
							
							// According to Bukkit JavaDocs, it's unsafe to close the inventory in an InventoryClickEvent until the next tick.
							// https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/InventoryClickEvent.html
							final Player pl = (Player)event.getWhoClicked();
							new BukkitRunnable() {
								@Override
								public void run() {
									pl.closeInventory();
								}
							}.runTaskLater(mainPlugin, 1);
							
							break;
							
						}
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
	
	public static Inventory getInv(Player player){
		Inventory inv = Bukkit.createInventory(null, (9 * ((staticMainPlugin.getGadgets().size()/9)+1)),
				staticGuiGadget.getInvTextFromConfig() );
		
		int slot = 0;

		for (Gadget g: staticMainPlugin.getGadgets()){
			if (!g.isGUI){
				inv.setItem(slot, g.getGUIItem(player));
				slot++;
			}
		}
		
		return inv;
	}
}
