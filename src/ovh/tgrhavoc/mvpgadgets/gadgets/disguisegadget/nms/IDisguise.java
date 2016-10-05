package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms;

import java.util.Collection;

import org.bukkit.entity.Player;

public interface IDisguise {
	
	public void sendDisguise(Collection<? extends Player> players);
	public void sendDisguise(Player...players);
	public void sendDisguise(Player player);
	
	public void updateDisguise(Player updateFor);
	public void updateDisguise(Player...players);
	public void updateDisguise(Collection<? extends Player> player);
	
	/***
	 * Send a raw packet to a specific player.
	 * @param player The Bukkit player to send the packet to
	 * @param packet Must be an object that implements the raw Packet interface (e.g. net.minecraft.v1_8_R3.Packet)
	 */
	public void sendPacket(Player player, Object packet);
	
	/***
	 * Send a raw packet to a specific players.
	 * @param player A collection of Bukkit players to send the packet to (e.g. {@link Bukkit.getOnlinePlayers()})
	 * @param packet Must be an object that implements the raw Packet interface (e.g. net.minecraft.v1_8_R3.Packet)
	 */
	public void sendPacket(Collection<? extends Player> players, Object packet);
	
	public void sendPacket(Object packet, Player...players);
	
	public void changeDisguise(EntityDisguises newDisguise);
	
	@Deprecated
	public String getEntityClassName(EntityDisguises entity);
	
	public void removeDisguise();
	
	
}
