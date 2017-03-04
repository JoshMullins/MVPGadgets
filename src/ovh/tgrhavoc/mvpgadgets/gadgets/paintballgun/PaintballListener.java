package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;
import ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun.nms.AbstractPaintHandler;

public class PaintballListener implements Listener {
	
	List<Location> hitBlocks = new ArrayList<>();
	
	MVPGadgets plugin;
	AbstractPaintHandler paintHandler;
	Random rand = new Random();
	
	public PaintballListener(MVPGadgets plugin, AbstractPaintHandler paintHandler) {
		this.plugin = plugin;
		this.paintHandler = paintHandler;
		
		plugin.setPaintListener(this);
	}
	
	@EventHandler
	public void onPaintballHit(ProjectileHitEvent e){
		
		if (! (e.getEntity() instanceof Snowball) )
			return;
		
		if (! e.getEntity().hasMetadata("isPaintball"))
			return;

		BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
		Block hitBlock = null;
		while(iterator.hasNext()){
			hitBlock = iterator.next();
			if (hitBlock.getType().isBlock() && hitBlock.getType().isSolid())
				break;
		}
		if (hitBlocks.contains(hitBlock.getLocation()))
			return;

		hitBlocks.add(hitBlock.getLocation());
		
		paintHandler.updateBlock(Bukkit.getOnlinePlayers(), hitBlock, Material.STAINED_CLAY,
				DyeColor.values()[rand.nextInt(DyeColor.values().length)].getDyeData());
		
		final Location loc = hitBlock.getLocation();
		final Block hitBlock2 = hitBlock;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				hitBlock2.getState().update();
				hitBlocks.remove(loc);
			}
			
		}, 20L * 4);
	}
	
	public void disable(){
		for (Location loc : hitBlocks) {
			loc.getBlock().getState().update();
			hitBlocks.remove(loc);
		}
	}
	
}
