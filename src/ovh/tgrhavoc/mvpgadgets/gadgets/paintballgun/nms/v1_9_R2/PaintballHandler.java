package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.v1_9_R2;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.PacketPlayOutBlockChange;
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
