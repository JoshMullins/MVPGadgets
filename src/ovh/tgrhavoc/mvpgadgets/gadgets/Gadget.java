package ovh.tgrhavoc.mvpgadgets.gadgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

/**
 * Class that all gadgets should extend.
 * 
 * @author Jordan Dalton
 *
 */
public abstract class Gadget {
	
	private static List<String> registeredEvents = new ArrayList<String>();

	//Messages.yml paths
	static final String MESSAGE_PATH = "Gadgets.{gadget_name}";
	static final String NAME_PATH = MESSAGE_PATH +".name";
	static final String DESCRIPTION_PATH = MESSAGE_PATH+".description";
	
	//config.yml paths
	static final String PRICE = "Gadget_Prices.{gadget_name}";
	
	protected ItemStack gadgetItem;
	private MVPGadgets plugin;
	
	/**
	 * Main constructor for the gadgets. Registers gadget events to the server. This constructor
	 * has safety checks to avoid redundant registration, but try not to abuse it anyway. :)
	 * @param plugin
	 * 				Main plugin reference
	 * @param name 
	 * 				Name of the gadget in the messages.yml file (Doesn't have to represent gadget, but it helps if it does).
	 * @param itemStack
	 * 				ItemStack that represents this gadget
	 * @param owningPlayer
	 * 				The player who is to use this Gadget. Each player owns multiple gadgets, each of which have
	 * their own event registration to the server; this design helps avoid multiple and redundant event registrations (which are bad).
	 */
	public Gadget(MVPGadgets plugin, String name, ItemStack itemStack, UUID owningPlayer){
		if (plugin == null)
			throw new IllegalArgumentException("plugin cannot be null!");
		
		if (name == null || name.equals(""))
			throw new IllegalArgumentException("name cannot be null or empty for the gadgets!");
		
		if (itemStack == null)
			throw new IllegalArgumentException("itemStack cannot be null!");
		
		if (owningPlayer == null)
			throw new IllegalArgumentException("owningPlayer cannot be null!");
					
		this.gadgetName = name;
		this.plugin = plugin;
		setItemStack(plugin.getMessages().getString(getNamePath()), itemStack);
		
		if (! (registeredEvents.contains(name)) ){
			registerEvents(plugin, plugin.getServer().getPluginManager());
			registeredEvents.add(name);
		}
		
	}
	
	/**
	 * Secondary constructor for gadgets. Does not register gadget events to the main plugin.
	 * Used mainly to get the instance of an unusable gadget when you don't need to register its events,
	 * i.e. to see what ItemStack it has without re-registering events to the server.
	 * <br><br>
	 * Preferred that you don't use the main constructor when you do not need to register events and/or set an owner.
	 * @param plugin Main plugin reference
	 * @param name Name of the gadget in the messages.yml file (Doesn't have to represent gadget, but it helps if it does).
	 * @param itemStack ItemStack that represents this gadget
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
		registerEvents(plugin, plugin.getServer().getPluginManager());
		
	}
	
	/**
	 * If the plugin is a GUI or not (SHOULD ONLY EVER BE USED FOR THE GADGET SELECTOR)
	 */
	public boolean isGUI = false;
	
	/**
	 * Do not change this.
	 */
	public String gadgetName; //Name of gadget for use in messages.yml file
	
	/**
	 * Called when the player right clicks the gadget.
	 * All code you want to execute should be in here.
	 * 
	 * @param player Who triggered the event.
	 */
	public abstract void execute(Player player);
	
	/**
	 * Called when the gadget is initialised.
	 * Please use this method to register all listeners for your gadget.
	 * @param plugin Main plugin instance
	 * @param pm PluginManager so you can register the events
	 */
	public abstract void registerEvents(MVPGadgets plugin, PluginManager pm);
	
	/**
	 * Get the ItemStack that represents this gadget (Non GUI, this is the item the player holds.)
	 * @return ItemStack that represents this gadget (Can be set by using setItemStack(String, ItemStack) method)
	 */
	public ItemStack getItemStack(){
		return gadgetItem;
	}
	
	/**
	 * Get the ItemStack for this gadget that should be used in the GadgetSelector.
	 * This returns the item with the price of the gadget and whether the player has permission to use it.
	 * 
	 * @param player Player you want to get the gadget for (Used to get the permission part)
	 * @return ItemStack of the gadget
	 */
	public ItemStack getGUIItem(Player player){
		ItemStack guiItem = gadgetItem.clone(); //Make sure we don't edit the original itemstack
		ItemMeta meta = guiItem.getItemMeta();
		
		//Description from message.yml with colour codes applied
		List<String> lore = new ArrayList<String>();
		for (String s: getDescriptionFromConfig()){
			lore.add( ChatColor.translateAlternateColorCodes('&', s) );
		}
		//Whether the user has permission
		String permissionMsg = getPlugin().getMessageFromConfig("Gadgets.PERMISSION");
		String pos = getPlugin().getMessageFromConfig("Gadgets.Positive");
		String neg = getPlugin().getMessageFromConfig("Gadgets.Negative");
		
		if (player.hasPermission("mvpgadgets." + gadgetName) ) //Eg. mvpgadgets.horseGadget
			lore.add(permissionMsg.replace("{HAS_PERMISSION}", pos));
		else
			lore.add(permissionMsg.replace("{HAS_PERMISSION}", neg));
		
		//If vault is hooked then display price
		if (plugin.hookedVault()){
			String priceMsg = getPlugin().getMessageFromConfig("Gadgets.PRICE");
			lore.add(priceMsg.replace("{COST}", getPlugin().getGadgetPrice(this)+""));
		}
		
		meta.setLore(lore);//Apply lore
		guiItem.setItemMeta(meta); //Apply meta
		
		return guiItem;
	}
	
	/**
	 * Get the gadget name defined in the Messages.yml file without colour codes.
	 * @return Name of the gadget defined in the messages.yml file
	 */
	public String getPlainName(){
		return ChatColor.stripColor(getNameFromConfig());
	}
	
	/**
	 * Get the name of the gadget as defined in the messages.yml file
	 * 
	 * @return Name for this gadget with the correct colours applied.
	 */
	public String getNameFromConfig(){
		return ChatColor.translateAlternateColorCodes('&', 
				plugin.getMessages().getString(getNamePath()));
	}
	
	/**
	 * Get a node in the messages.yml file that is under your gadget (e.g. Gadgets.<YourGadget>.Message)
	 * @param message node the message resides in (Appended to "Gadgets.<YourGadget>.")
	 * @return Colour coded message nodes
	 */
	public String getMessageFromMessages(String message){
		return ChatColor.translateAlternateColorCodes('&', 
				plugin.getMessageFromConfig(MESSAGE_PATH.replace("{gadget_name}", gadgetName) + "." + message));
	}
	
	/**
	 * Used for GUI gadgets. Gets their names as defined in the messages.yml file.
	 * @return Colour translated string at node Gadgets.YourGadget.inventory_name
	 */
	public String getInvTextFromConfig(){
		return ChatColor.translateAlternateColorCodes('&', 
				plugin.getMessages().getString(getInvText()));
	}
	/**
	 * Get the description of the gadget as defined in the messages.yml
	 * @return List<String> that contains the description of the gadget (added to lore)
	 */
	public List<String> getDescriptionFromConfig(){
		return plugin.getMessages().getStringList(getDescriptionPath());
	}
	
	private String getNamePath(){
		return NAME_PATH.replace("{gadget_name}", gadgetName);
	}
	private String getDescriptionPath(){
		return DESCRIPTION_PATH.replace("{gadget_name}", gadgetName);
	}
	/**
	 * Get the path for the gadget's price
	 * @return Price path (Gadget_Prices.<YourGadget>)
	 */
	public String getPricePath(){
		return PRICE.replace("{gadget_name}", gadgetName);
	}
	
	private String getInvText(){
		if(isGUI){
			return MESSAGE_PATH.replace("{gadget_name}", gadgetName) + ".inventory_name";
		}
		return "NOT A GUI";
	}
	
	/**
	 * Helpful method for eaily setting the item stack for a gadget.
	 * @param name Name you want to give the gadget (Runs through ChatColor.translateAlternateColorCodes)
	 * @param gadgetItem ItemStack "icon" for the gadget (Displayed in GUI and in player's inventory.
	 */
	public void setItemStack(String name, ItemStack gadgetItem){
		ItemMeta m = gadgetItem.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name + " (Gadget)"));
		gadgetItem.setItemMeta(m);
		this.gadgetItem = gadgetItem;
	}
	
	/**
	 * Get the gadget's name.
	 * This is used in the messages.yml and config.yml
	 * @return The gadget's name (as defined in the constructor)
	 */
	public String getGadgetName(){
		return gadgetName;
	}
	
	/**
	 * Get the main plugin reference
	 * @return {@link ovh.tgrhavoc.mvpgadgets.MVPGadgets MVPGadgets} reference.
	 */
	public MVPGadgets getPlugin(){
		return plugin;
	}	
}
