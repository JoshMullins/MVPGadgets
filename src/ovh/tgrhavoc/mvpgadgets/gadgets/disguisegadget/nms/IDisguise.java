package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms;

import java.util.Collection;

import net.minecraft.server.v1_8_R3.Packet;

import org.bukkit.entity.Player;

public interface IDisguise {
	
	public void sendDisguise(Collection<? extends Player> players);
	public void sendDisguise(Player...players);
	public void sendDisguise(Player player);
	
	public void updateDisguise(Player updateFor);
	public void updateDisguise(Player...players);
	public void updateDisguise(Collection<? extends Player> player);
	
	@SuppressWarnings("rawtypes")
	public void sendPacket(Player player, Packet packet);
	@SuppressWarnings("rawtypes")
	public void sendPacket(Collection<? extends Player> players, Packet packet);
	@SuppressWarnings("rawtypes")
	public void sendPacket(Packet packet, Player...players);
	
	public void changeDisguise(EntityDisguises newDisguise);
	
	public String getEntityClassName(EntityDisguises entity);
	
	public void removeDisguise();
	
	
}
