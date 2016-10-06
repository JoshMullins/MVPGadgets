package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.v1_10_R1;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.PacketPlayOutBlockChange;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.AbstractPaintHandler;

public class PaintballHandler extends AbstractPaintHandler {

	@Override
	public void sendChangeBlock(Player player, Block newBlock, Material material, byte data) {
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(
				((CraftWorld)newBlock.getWorld()).getHandle(), new BlockPosition(
						newBlock.getLocation().getX(), newBlock.getLocation().getY(), newBlock.getLocation().getZ()));
		packet.block = CraftMagicNumbers.getBlock(material).fromLegacyData(data);
		CraftPlayer cP = (CraftPlayer)player;
		cP.getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void updateBlock(Collection<? extends Player> players, Block block, Material material, byte data) {
		for (Player player : players) {
			sendChangeBlock(player, block, material, data);
		}
	}

}
