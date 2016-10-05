package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.v1_8_R3;

import java.util.Collection;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.AbstractDisguise;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.EntityDisguises;

// v1_8_R3 = 1.8.x
public class Disguise extends AbstractDisguise {
	
	public Disguise(Player toDisguise, EntityDisguises disguise) {
		super(toDisguise, disguise);
		
		// Bukkit.broadcastMessage("Reflectively created disguise class for " + toDisguise.getName() + " as " + disguise.name());
	}

	@Override
	public void sendDisguise(Collection<? extends Player> players) {
		for(Player p: players){
			if (p.equals(getPlayer()))
				continue;
			
			sendDisguise(p);
		}
	}

	@Override
	public void sendDisguise(Player... players) {
		for(Player p: players){
			if (p.equals(getPlayer()))
				continue;
			
			sendDisguise(p);
		}
	}

	@Override
	public void sendDisguise(Player player) {
		if (player.equals(this.getPlayer()))
			throw new IllegalArgumentException("Target player cannot be the disguised player");
		
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getPlayer().getEntityId());
		World world = ((CraftWorld)getPlayer().getWorld()).getHandle();
		
		try {
			Class<?> entityClass = Class.forName(getDisguise().getClassName());
			EntityLiving entity = null;
			if (EntityLiving.class.isAssignableFrom(entityClass)) {
				entity = (EntityLiving) entityClass.getConstructor(World.class).newInstance(world);
			}
			
			if (entity == null){
				System.out.println("Error.. Entity is now null");
				return;
			}
			
			//Hopefully entity is now the entity the player wants to be!
			entity.setPosition(getPlayer().getLocation().getX(), getPlayer().getLocation().getY(),
					getPlayer().getLocation().getZ());
			entity.d(getPlayer().getEntityId());
			
			if (this.getCustomName() != null) {
				entity.setCustomName(getCustomName());
				entity.setCustomNameVisible(true);
			}
			
			//FIXME: Handle special entity types (wither skeletons etc)
			
			PacketPlayOutSpawnEntityLiving livingPacket = new PacketPlayOutSpawnEntityLiving(entity);
			sendPacket(player, packet);
			sendPacket(player, livingPacket);
			
			//FIXME: Ad amour contents below
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateDisguise(Player updateFor) {
		sendDisguise(updateFor);
	}

	@Override
	public void updateDisguise(Player... players) {
		sendDisguise(players);
	}

	@Override
	public void updateDisguise(Collection<? extends Player> players) {
		sendDisguise(players);
	}

	@Override
	public void sendPacket(Player player, Object packet) {
		if( ! (packet instanceof Packet) )
			throw new IllegalArgumentException("sendPacket called without a Packet ");
		CraftPlayer cP = (CraftPlayer)player;
		cP.getHandle().playerConnection.sendPacket((Packet)packet);
	}

	@Override
	public void sendPacket(Collection<? extends Player> players, Object packet) {
		if( ! (packet instanceof Packet) )
			throw new IllegalArgumentException("sendPacket called without a Packet ");
		
		for(Player p: players) {
			sendPacket(p, packet);
		}
	}

	@Override
	public void sendPacket(Object packet, Player... players) {
		if( ! (packet instanceof Packet) )
			throw new IllegalArgumentException("sendPacket called without a Packet ");
		
		for(Player p: players)
			sendPacket(p, packet);
	}

	@Override
	public String getEntityClassName(EntityDisguises entity) {
		// None Auto-generated method stub
		return null;
	}

	@Override
	public void removeDisguise() {
		PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(getPlayer().getEntityId());
		PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn( ((CraftPlayer)getPlayer()).getHandle());
		
		for(Player p: Bukkit.getOnlinePlayers()) {
			if (p.equals(getPlayer()))
				continue;
			sendPacket(p, destroyPacket);
			sendPacket(p, spawnPacket);
		}
		
	}

	@Override
	public void changeDisguise(EntityDisguises newDisguise) {
		this.setDisguise(newDisguise);
		sendDisguise(Bukkit.getOnlinePlayers());
	}

}
