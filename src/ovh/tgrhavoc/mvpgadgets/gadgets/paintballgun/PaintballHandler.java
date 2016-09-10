package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import java.util.Collection;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

public class PaintballHandler {
	
	public static void sendChangeBlock(Player player, Block newBlock, Material material, byte data) {
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(
				((CraftWorld)newBlock.getWorld()).getHandle(), new BlockPosition(
						newBlock.getLocation().getX(), newBlock.getLocation().getY(), newBlock.getLocation().getZ()));
		packet.block = CraftMagicNumbers.getBlock(material).fromLegacyData(data);
		CraftPlayer cP = (CraftPlayer)player;
		cP.getHandle().playerConnection.sendPacket(packet);
	}
	
	public static void updateBlock(Collection<? extends Player> players, Block block, Material material, byte data) {
		for (Player player : players) {
			sendChangeBlock(player, block, material, data);
		}
	}
	
}
