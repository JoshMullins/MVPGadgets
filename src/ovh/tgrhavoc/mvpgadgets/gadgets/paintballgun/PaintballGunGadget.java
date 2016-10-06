package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.AbstractPaintHandler;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.PaintFactory;

public class PaintballGunGadget extends Gadget {
    	
	public PaintballGunGadget(MVPGadgets plugin) {
		super(plugin, "paintballGadget", new ItemStack(Material.DIAMOND_BARDING));
	}

	@Override
	public void execute(Player player) {
		Snowball projectile = (Snowball)player.launchProjectile(Snowball.class);
		projectile.setMetadata("isPaintball", new FixedMetadataValue(getPlugin(), true));
	}

	@Override
	public void registerEvents(MVPGadgets plugin, PluginManager pm) {
		AbstractPaintHandler pH = PaintFactory.getPaintHandler(MVPGadgets.getNmsVersion());

		if (pH != null)
			pm.registerEvents(new PaintballListener(plugin, pH), plugin);
		else
			Bukkit.getLogger().warning("Couldn't create paintball handler... Paintball gadget won't work");
	}

}
