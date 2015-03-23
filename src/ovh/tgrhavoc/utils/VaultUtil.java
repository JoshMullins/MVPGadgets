package ovh.tgrhavoc.utils;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

public class VaultUtil {	
	public static MVPGadgets plugin;
	
	public static void setPlugin(MVPGadgets plugin){
		VaultUtil.plugin = plugin;
	}
	
	public static boolean transaction(Player player, double amount){
		EconomyResponse response = plugin.getEconomy().withdrawPlayer(player.getPlayer(), amount);
		return response.transactionSuccess() && response.type == EconomyResponse.ResponseType.SUCCESS;
		
	}
	
}
