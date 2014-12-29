package ovh.tgrhavoc.mvpgadgets;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import me.pookeythekid.MobCannon.MobCannon;
import net.minecraft.util.org.apache.commons.lang3.ObjectUtils.Null;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ovh.tgrhavoc.mvpgadgets.events.GadgetHandler;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.guigadget.GUIGadgetListener;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseGadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.horse.HorseListener;
import ovh.tgrhavoc.mvpvpgadgets.tests.JarUtil;

public class MVPGadgets extends JavaPlugin {
	
	static List<Gadget> availableGadgets = new ArrayList<Gadget>();

	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new GadgetHandler(this), this);
		
		//registerGadgets();
		
		//loadGadgetClasses();
		
		test();
		
		registerGadetEvents();
		
		getCommand("launchmob").setExecutor(new MobCannon(this, null));
		
	}
	
	private void test(){
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
	
	private void registerGadgets() {
		addGadget(new GUIGadget());
		addGadget(new HorseGadget());
	}

	private void registerGadetEvents() {
		Bukkit.getPluginManager().registerEvents(new GUIGadgetListener(this), this);
		Bukkit.getPluginManager().registerEvents(new HorseListener(this), this);
	}
	
	public static void addGadgetStatic(Gadget g){
		if (availableGadgets.contains(g))
			return;
		availableGadgets.add(g);
	}

	public void addGadget(Gadget g){
		if (availableGadgets.contains(g))
			return;
		availableGadgets.add(g);
	}
	
	public List<Gadget> getGadgets(){
		return availableGadgets;
	}
	
	//Method which loads .class files found in the "mods" folder so you can dynamcaly add or remove gadgets
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

}
