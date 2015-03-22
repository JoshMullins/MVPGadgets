package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

public class PaintballGunGadget extends Gadget {

	public PaintballGunGadget(MVPGadgets plugin) {
		super(plugin, "paintballGadget", new ItemStack(Material.DIAMOND_BARDING));
		
	}

	@Override
	public void execute(Player player) {
		Snowball projectile = (Snowball)player.launchProjectile(Snowball.class);
		projectile.setMetadata("isPaintball", new FixedMetadataValue(getPlugin(), true));
	}

}
