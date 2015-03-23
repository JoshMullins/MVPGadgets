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
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadgetListener;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseListener;
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
	public static Permission permission = null;
	
	@Override
	public void onDisable(){
		paintListener.disable();
	}
	
	@Override
	public void onEnable(){
		saveDefaultConfig();
		
		try {
			initConfigs();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (getConfig().getBoolean("vault"))
			initVault();
		
		getServer().getPluginManager().registerEvents(new GadgetHandler(this), this);
		
		registerGadgets();
		registerGadetEvents();
		
		
		mobCannon.reloadCannon();
		getCommand("mobcannon").setExecutor(mobCannon);
		getCommand("mobcannonreload").setExecutor(mobCannon);
		getCommand("launchmob").setExecutor(mobCannon);
		getCommand("moblist").setExecutor(mobCannon);
		getCommand("mobnames").setExecutor(mobCannon);
	}
	
	private void registerGadgets() {
		mobCannon = new MobCannon(this, null);
		
		addGadget(new HorseGadget(this));
		addGadget(new MobCannonGadget(this));
		addGadget(new PaintballGunGadget(this));
		
		GUIGadget g = new GUIGadget(this);
		GUIGadgetListener l = new GUIGadgetListener(this, g);
		g.setListener(l);
		addGadget(g);
		getServer().getPluginManager().registerEvents(l, this);
	}
	
	public MobCannon getMobCannon(){
		return mobCannon;
	}
	
	private void registerGadetEvents() {
		getServer().getPluginManager().registerEvents(new HorseListener(this), this);
		getServer().getPluginManager().registerEvents(paintListener, this);
	}
	
	private void initConfigs() throws IOException{
		//Messages.yml
		File messagesFile = new File(this.getDataFolder(), "messages.yml");
		if (!messagesFile.exists()){
			messagesFile.createNewFile();
			//Couldn't find an easy way with YamlConfiguration, so I did this the old-fashioned way
			InputStream in = getResource("messages.yml");
			OutputStream out = new FileOutputStream(messagesFile);
			
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = in.read(buffer)) !=-1){
				out.write(buffer, 0, bytesRead);
			}
			in.close();
			out.flush();
			out.close();
		}
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		//End messages.yml
		
	}
	
	public YamlConfiguration getMessages(){
		return messages;
	}
	
	public double getGadgetPrice(Gadget gadget){
		return getGadgetPrice(gadget.getGadgetName());
	}
	
	public double getGadgetPrice(String gadgetName){
		return getConfig().getDouble("Gadget_Prices." + gadgetName, 10.0d);
	}
	
	public static void addGadgetStatic(Gadget g){
		if (availableGadgets.contains(g)){
			Bukkit.getLogger().info("Someone tried to register a gadget that already exists");
			return;
		}
		availableGadgets.add(g);
	}

	public void addGadget(Gadget g){
		if (availableGadgets.contains(g)){
			Bukkit.getLogger().info("Someone tried to register a gadget that already exists");
			return;
		}
		availableGadgets.add(g);
	}
	
	public List<Gadget> getGadgets(){
		return availableGadgets;
	}
	
	public String getMessageFromConfig(String messagePath){
		return ChatColor.translateAlternateColorCodes('&', getMessages().getString(messagePath));
	}
	
	//Start Vault hook
	public boolean hookedVault(){
		return (economy != null && permission != null);
	}
	public Economy getEconomy(){
		return economy;
	}
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
