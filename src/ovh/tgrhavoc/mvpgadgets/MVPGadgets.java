package ovh.tgrhavoc.mvpgadgets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ovh.tgrhavoc.mvpgadgets.events.GadgetHandler;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadgetListener;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseListener;

public class MVPGadgets extends JavaPlugin {
	
	List<Gadget> availableGadgets = new ArrayList<Gadget>();

	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new GadgetHandler(this), this);
		
		registerGadgets();
		
		registerGadetEvents();
		
		getCommand("launchmob").setExecutor(new MobCannon(this, null));
	}
	
	private void registerGadgets() {
		addGadget(new GUIGadget(new ItemStack(Material.CHEST)));
		addGadget(new HorseGadget());
	}

	private void registerGadetEvents() {
		Bukkit.getPluginManager().registerEvents(new GUIGadgetListener(this), this);
		Bukkit.getPluginManager().registerEvents(new HorseListener(this), this);
	}

	public void addGadget(Gadget g){
		if (availableGadgets.contains(g))
			return;
		availableGadgets.add(g);
	}
	
	public List<Gadget> getGadgets(){
		return availableGadgets;
	}

}
