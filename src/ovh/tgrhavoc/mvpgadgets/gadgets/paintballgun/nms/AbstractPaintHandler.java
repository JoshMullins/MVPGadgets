package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 * 
 * @author Jordan Dalton
 */
public abstract class AbstractPaintHandler {
	
	/***
	 * Send a packet to the player to change the block at newBlock position to the material supplied
	 * 
	 * @param player The player who you want to see the change
	 * @param newBlock The block to change
	 * @param material The material to change the block to
	 * @param data Extra data you need for the new block. E.g. Dye colour used in wool
	 */
	public abstract void sendChangeBlock(Player player, Block newBlock, Material material, byte data);
	
	/***
	 * Send the change packet to a collection of players.
	 * This method should call the {@link AbstractPaintHandler.sendChangeBlock sendChangeBlock} method for each player in the collection. 
	 * @param players A collection of players who you want to update the block for. E.g. Bukkit.getOnlinePlayers()
	 * @param block The block to change
	 * @param material The material to change the block to
	 * @param data Extra data you need for the new block. E.g. Dye colour used in wool
	 */
	public abstract void updateBlock(Collection<? extends Player> players, Block block, Material material, byte data);
	
}
