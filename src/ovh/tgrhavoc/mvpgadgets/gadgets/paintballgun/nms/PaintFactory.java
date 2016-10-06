package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PaintFactory {
	
	public static AbstractPaintHandler getPaintHandler( String version ) {
		try {
			final Class<?> clazz = Class.forName("ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms." + version + ".PaintballHandler");
			
			if (AbstractPaintHandler.class.isAssignableFrom(clazz)) {
				
				return (AbstractPaintHandler)clazz.getConstructor().newInstance();
			}
			
		}catch(final Exception ex){
			//Couldn't create handler.. Maybe wrong version being used?
			//Send some error to the user, server etc. Tell them to create an issue in
			//github for support and to add their server.jar to dropbox so that the
			//version can be implemented via nms
			ex.printStackTrace();
			Bukkit.getLogger().warning("Hello! Sorry, but version " + version
					+ " of CraftBukkit/Spigot doesn't seem to be supported by our plugin!");
			return null;
		}
		
		Bukkit.getLogger().warning("Seriously. If you see this message, something bad has screwed up.");
		return null;
	}
	
}
