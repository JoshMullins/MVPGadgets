package ovh.tgrhavoc.mvpgadgets.gadgets.guigadget;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

public class GUIGadget extends Gadget {
	
	GUIGadgetListener listener;
	
	public GUIGadget(MVPGadgets plugin) {
		super(plugin, "guiGadget", new ItemStack(Material.CHEST));
		isGUI = true;
	}
	
	public void setListener(GUIGadgetListener listener){
		this.listener = listener;
	}
	
	@Override
	public void execute(Player player) {
		player.openInventory(GUIGadgetListener.getInv(player));
	}

	@Override
	public void registerEvents(MVPGadgets plugin, PluginManager pm) {
		
		GUIGadgetListener l = new GUIGadgetListener(plugin, this);
		pm.registerEvents(l, plugin);
		
		this.listener = l;
	}

}
