package ovh.tgrhavoc.mvpgadgets.gadgets.mobcannon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

/**
 * @author Jordan Dalton
 *
 */
public class MobCannonGadget extends Gadget {

	/**
	 * @param plugin
	 * @param name
	 * @param itemStack
	 */
	public MobCannonGadget(MVPGadgets plugin) {
		super(plugin, "mobcannonGadget", new ItemStack(Material.TRIPWIRE_HOOK));
	}

	/* (non-Javadoc)
	 * @see ovh.tgrhavoc.mvpgadgets.gadgets.Gadget#execute(org.bukkit.entity.Player)
	 */
	@Override
	public void execute(Player player) {
		getPlugin().getMobCannon().launchMob("", player, getPlugin().getMobCannon().getUsedNames());
	}

}
