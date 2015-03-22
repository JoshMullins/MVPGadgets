package ovh.tgrhavoc.mvpgadgets.gadgets;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

public abstract class Gadget {
	
	static final String MESSAGE_PATH = "Gadgets.{gadget_name}";
	static final String NAME_PATH = MESSAGE_PATH +".name";
	
	protected ItemStack gadgetItem;
	private MVPGadgets plugin;
	
	/**
	 * Main constructor for the gadgets.
	 * @param plugin
	 * 				Main plugin reference
	 * @param name 
	 * 				Name of the gaget in the messages.yml file (Doesn't have to represent gadget but, it helps if it does.
	 * @param itemStack
	 * 				ItemStack that reprensents this gadget
	 */
	public Gadget(MVPGadgets plugin, String name, ItemStack itemStack){
		if (plugin == null)
			throw new IllegalArgumentException("plugin cannot be null!");
		
		if (name == null || name.equals(""))
			throw new IllegalArgumentException("name cannot be null or empty for the gadgets!");
		
		if (itemStack == null)
			throw new IllegalArgumentException("itemStack cannot be null!");
		
		this.gadgetName = name;
		
		this.plugin = plugin;
		setItemStack(plugin.getMessages().getString(getNamePath()), itemStack);
	}
	
	public boolean isGUI = false;
	private String gadgetName; //Name of gadget for use in messages.yml file
	
	public abstract void execute(Player player);
	
	/**
	 * Get the ItemStack that represents this gadget
	 * @return {@link ItemStack} that represents this gadget (Can be set by using setItemStack(String, ItemStack) method)
	 */
	public ItemStack getItemStack(){
		return gadgetItem;
	}
	/**
	 * Get the name of the gadget as defined in the config file
	 * 
	 * @return Name for this gadget with the correct colours applied.
	 */
	public String getNameFromConfig(){
		return ChatColor.translateAlternateColorCodes('&', 
				plugin.getMessages().getString(getNamePath()));
	}
	
	
	public String getInvTextFromConfig(){
		return ChatColor.translateAlternateColorCodes('&', 
				plugin.getMessages().getString(getInvText()));
	}
	
	private String getNamePath(){
		return NAME_PATH.replace("{gadget_name}", gadgetName);
	}
	
	private String getInvText(){
		if(isGUI){
			return MESSAGE_PATH.replace("{gadget_name}", gadgetName) + ".inventory_name";
		}
		return "NOT A GUI";
	}
	
	/**
	 * Helpful method for eaily setting the item stack for a gadget.
	 * @param name Name you want to give the gadget (Runs through {@link ChatColor.translateAlternateColorCodes})
	 * @param gadgetItem {@link ItemStack} "icon" for the gadget (Displayed in GUI and in player's inventory.
	 */
	public void setItemStack(String name, ItemStack gadgetItem){
		ItemMeta m = gadgetItem.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name + " (Gadget)"));
		gadgetItem.setItemMeta(m);
		this.gadgetItem = gadgetItem;
	}
	
	/**
	 * Get the main plugin reference
	 * @return {@link MVPGadgets} reference.
	 */
	public MVPGadgets getPlugin(){
		return plugin;
	}
	
}
