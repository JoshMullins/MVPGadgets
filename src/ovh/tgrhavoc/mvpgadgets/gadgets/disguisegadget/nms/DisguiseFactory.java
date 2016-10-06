package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms;

import org.bukkit.entity.Player;

public class DisguiseFactory {

	public DisguiseFactory() {
		// None Auto-generated constructor stub
	}
	
	
	public static AbstractDisguise getDisguiseFor(Player player, EntityDisguises disguise, String version) {
		try {
			final Class<?> clazz = Class.forName("ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms." + version + ".Disguise");
			// player.sendMessage("Getting to if");
			if (AbstractDisguise.class.isAssignableFrom(clazz)) {
				// player.sendMessage("Hopefully sending the correct class");
				return (AbstractDisguise)clazz.getConstructor(Player.class, EntityDisguises.class).newInstance(player, disguise);
			}
			
		}catch(final Exception ex){
			//Couldn't create disguise.. Maybe wrong version being used?
			//Send some error to the user, server etc. Tell them to create an issue in
			//github for support and to add their server.jar to dropbox so that the
			//version can be implemented via nms
			ex.printStackTrace();
			player.sendMessage("Hello! Sorry, but version " + version
					+ " of CraftBukkit/Spigot doesn't seem to be supported by our plugin!");
			return null;
		}
		
		player.sendMessage("Null. Seriously. If you see this message, something bad has screwed up... And I mean something HUGE.");
		return null;
	}

}
