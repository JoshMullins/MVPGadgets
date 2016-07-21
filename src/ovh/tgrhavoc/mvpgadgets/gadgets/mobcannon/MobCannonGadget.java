package ovh.tgrhavoc.mvpgadgets.gadgets.mobcannon;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;


public class MobCannonGadget extends Gadget {

	public MobCannonGadget(MVPGadgets plugin, UUID owningPlayer) {
		super(plugin, "mobcannonGadget", new ItemStack(Material.TRIPWIRE_HOOK), owningPlayer);
	}
	
	public MobCannonGadget(MVPGadgets plugin) {
		super(plugin, "mobcannonGadget", new ItemStack(Material.TRIPWIRE_HOOK));
	}

	@Override
	public void execute(Player player) {
		getPlugin().getMobCannon().launchMob("", player, getPlugin().getMobCannon().getUsedNames());
	}

	@Override
	public void registerEvents(MVPGadgets plugin, PluginManager pm) {
		return;
	}

}
