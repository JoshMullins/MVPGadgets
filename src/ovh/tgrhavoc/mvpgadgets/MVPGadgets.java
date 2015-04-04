package ovh.tgrhavoc.mvpgadgets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import me.pookeythekid.MobCannon.MobCannon;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ovh.tgrhavoc.mvpgadgets.events.GadgetHandler;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.DisguiseGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.mobcannon.MobCannonGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.PaintballGunGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.PaintballListener;
import ovh.tgrhavoc.mvpvpgadgets.tests.JarUtil;
import ovh.tgrhavoc.utils.VaultUtil;

public class MVPGadgets extends JavaPlugin {
	
	static List<Gadget> availableGadgets = new ArrayList<Gadget>();
	private YamlConfiguration messages;
	private MobCannon mobCannon;
	private PaintballListener paintListener = new PaintballListener(this);
	
	private static Economy economy = null;
	private static Permission permission = null;
	
	@Override
	public void onDisable(){
		paintListener.disable();
	}
	
	@Override
	public void onEnable(){
		saveDefaultConfig();
		initConfigs();
		
		if (getConfig().getBoolean("vault"))
			initVault();
		
		getServer().getPluginManager().registerEvents(new GadgetHandler(this), this);
		
		registerGadgets();
		registerGadetEvents();
		registerCommands();
	}
	
	private void registerGadgets() {
		mobCannon = new MobCannon(this, null);
		
		addGadget(new HorseGadget(this));
		addGadget(new MobCannonGadget(this));
		addGadget(new PaintballGunGadget(this));
		addGadget(new DisguiseGadget(this));
		
		addGadget(new GUIGadget(this));
	}
	@Deprecated
	private void registerGadetEvents() {
		getServer().getPluginManager().registerEvents(paintListener, this); //Going to keep this here for now.
	}
	private void registerCommands(){
		mobCannon.reloadCannon();
		getCommand("mobcannon").setExecutor(mobCannon);
		getCommand("mobcannonreload").setExecutor(mobCannon);
		getCommand("launchmob").setExecutor(mobCannon);
		getCommand("moblist").setExecutor(mobCannon);
		getCommand("mobnames").setExecutor(mobCannon);
	}
	
	/**
	 * Get the instance of the {@link me.pookeythekid.MobCannon.MobCannon MobCannon} class used by this plugin.
	 * @return MobCannon used by the plugin
	 */
	public MobCannon getMobCannon(){
		return mobCannon;
	}
	
	private void initConfigs(){
		//Messages.yml
		File messagesFile = new File(this.getDataFolder(), "messages.yml");
		try {
			writeToFile(getResource("messages.yml"), messagesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		//End messages.yml
		
	}
	
	private void writeToFile(InputStream in, File file) throws IOException{
		OutputStream out = new FileOutputStream(file);
		
		byte[] buffer = new byte[1024];
		int bytesRead;
		while((bytesRead = in.read(buffer)) !=-1){
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.flush();
		out.close();
		
	}
	
	/**
	 * Get the messages.yml file in a YamlConfiguration class.
	 * @return YamlConfiguration that represnts the Messages.yml file in the resources folder
	 */
	public YamlConfiguration getMessages(){
		return messages;
	}
	
	/**
	 * Get the price of a gadget.
	 * @param gadget Gadget you want to get the price for
	 * @return The price of the gadget as defined in the config file as a double
	 */
	public double getGadgetPrice(Gadget gadget){
		return getGadgetPrice(gadget.getGadgetName());
	}
	
	/**
	 * Get the price of a gadget.
	 * @param gadgetName The gadget's name you want to check. You can do this by calling the {@link gadgets.Gadget#getGadgetName getGadgetName()} method in the Gadget
	 * @return The price of the gadget as defined in the config file as a double
	 */
	public double getGadgetPrice(String gadgetName){
		return getConfig().getDouble("Gadget_Prices." + gadgetName, 10.0d);
	}
	
	/**
	 * Add a gadget to the list (This will allow it to appear in the Gadget selector)
	 * @param g Gadget to add to the list.
	 */
	public static void addGadgetStatic(Gadget g){
		if (availableGadgets.contains(g)){
			Bukkit.getLogger().info("Someone tried to register a gadget that already exists");
			return;
		}
		availableGadgets.add(g);
	}

	/**
	 * Add a gadget to the list (This will allow it to appear in the Gadget selector)
	 * @param g Gadget to add to the list.
	 */
	public void addGadget(Gadget g){
		if (availableGadgets.contains(g)){
			Bukkit.getLogger().info("Someone tried to register a gadget that already exists");
			return;
		}
		availableGadgets.add(g);
	}
	
	/**
	 * Get the list of the gadgets that are available to use
	 * @return A list of gadgets that are available
	 */
	public List<Gadget> getGadgets(){
		return availableGadgets;
	}
	
	/**
	 * Get a message from the messages.yml file
	 * This method automatically translates the character '&' in the string to the proper colour code.
	 * @param messagePath Node to the string you want to get (e.g. Message.MyGadget.Message)
	 * @return Returns a colour-translated string
	 */
	public String getMessageFromConfig(String messagePath){
		return ChatColor.translateAlternateColorCodes('&', getMessages().getString(messagePath));
	}
	
	//Start Vault hook
	/**
	 * Check if this plugin was able to successfully hook into the vault plugin (Permission and Economy)
	 * @return True if the plugin was able to hook into vault
	 */
	public boolean hookedVault(){
		return (economy != null && permission != null);
	}
	
	/**
	 * Get the Economy class
	 * @return Economy class as provided by Vault
	 */
	public Economy getEconomy(){
		return economy;
	}
	
	/**
	 * Get the Permission class
	 * @return Permission class as provided by Vault
	 */
	public Permission getPermission(){
		return permission;
	}
	
	private void initVault(){
		if (!setupEconomy())
			System.out.println("Sorry, couldn't hook economy plugin. Maybe you don't have one installed?");
		else
			System.out.println("Vault economy hook successful.");
		if(!setupPermissions())
			System.out.println("Sorry, couldn't hook permissions plugin.");
		else
			System.out.println("Vault permissions hooked");
		VaultUtil.setPlugin(this);
	}
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = 
				getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) { 
			permission = permissionProvider.getProvider(); 
		} 
		return (permission != null);
	}
	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = 
				getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	    if (economyProvider != null) {
	    	economy = economyProvider.getProvider();
	    }
	    return (economy != null);
    }
	//End vault hook
	
	//Method which loads .class files found in the "mods" folder so you can dynamcaly add or remove gadgets
	@SuppressWarnings({ "unused", "unchecked" })
	@Deprecated
    private void loadGadgetClasses() {
        File basePath = new File(this.getDataFolder() + "/mods");
        File[] files = new File(this.getDataFolder() + "/mods").listFiles();

        boolean shouldLoad = true;

        ClassLoader cl = null;

        try {
            URL url = basePath.toURL();
            URL[] urls = new URL[]{url};
            cl = new URLClassLoader(urls);
        } catch (Exception e) {
            shouldLoad = false;
            e.printStackTrace();
        }

        if (shouldLoad) {
            for (File file : files) {
                String fileName = file.getName();
                //If it is not a .class file contiue to the next entry.
                if (!fileName.split(".")[fileName.split(".").length - 1].equals(".class")) continue;

                try {

                    @SuppressWarnings("rawtypes")
					Class clazz = cl.loadClass(fileName.replace(".class",""));
                    //Our .class file extends Gadget
                    if(clazz.isAssignableFrom(Gadget.class)){
                        Gadget gadget = (Gadget) clazz.newInstance();
                        addGadget(gadget);
                    }

                } catch (Exception e) {

                }
            }
        }
    }

	@Deprecated
	public void test(){
		for (String s: JarUtil.getGadetClasses(this.getDataFolder().getParent() + "/MVPGadgets.jar")){
			try {
				System.out.println("Constructing... " + s);
				
				if (s.contains("\\$[0-9]"))
					System.out.println("Found funny class: " + s);
				
				Class<?> gadgetClass = Class.forName(s);
				Constructor<?> constr = gadgetClass.getConstructor();
				Gadget gadgetFromClass = null;
				if (constr.getParameterTypes().length > 0){
					System.out.println("Found aruments for " + s);
					
				}else{ // No constructor args (Assume it's a gadget)
					gadgetFromClass = (Gadget)constr.newInstance();
				}
				 
				//addGadget(gadgetFromClass);
				System.out.println("Just added a gadget " + gadgetFromClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
