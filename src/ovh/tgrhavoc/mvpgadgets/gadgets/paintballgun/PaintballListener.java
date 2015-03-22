package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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

public class PaintballListener implements Listener {
	
	private class BlockData{
		Material material;
		byte data;
		
		@SuppressWarnings("deprecation")
		public BlockData(Block block){
			material = block.getType();
			data = block.getData();
		}
		public Material getMaterial() {
			return material;
		}
		public byte getData() {
			return data;
		}
	}
	
	Map<Location, BlockData> previousData = new HashMap<Location, BlockData>();
	
	MVPGadgets plugin;
	
	public PaintballListener(MVPGadgets plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
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
		if (previousData.containsKey(hitBlock.getLocation()))
			return;
		previousData.put(hitBlock.getLocation(), new BlockData(hitBlock));
		
		hitBlock.setType(Material.STAINED_CLAY);
		hitBlock.setData(DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getData());
		
		final Location loc = hitBlock.getLocation();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				if (loc.getBlock().getType() == Material.STAINED_CLAY){
					BlockData prevData = previousData.get(loc);
					loc.getBlock().setType(prevData.getMaterial());
					loc.getBlock().setData(prevData.getData());
					previousData.remove(loc);
				}
			}
			
		}, 20L * 4);
	}
	
	@SuppressWarnings("deprecation")
	public void disable(){
		for (Entry<Location, BlockData> entry: previousData.entrySet()){
			entry.getKey().getBlock().setType(entry.getValue().getMaterial());
			entry.getKey().getBlock().setData(entry.getValue().getData());
		}
	}
	
}
