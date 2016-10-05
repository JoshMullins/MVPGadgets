package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.v1_10_R1;


import java.util.Collection;

import org.bukkit.entity.Player;

import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.AbstractDisguise;
import ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms.EntityDisguises;

// v1_10_R1 = 1.10 & 1.10.2
public class Disguise extends AbstractDisguise {
	
	public Disguise(Player toDisguise, EntityDisguises disguise) {
		super(toDisguise, disguise);
	}


	@Override
	public void sendDisguise(Collection<? extends Player> players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDisguise(Player... players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDisguise(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDisguise(Player updateFor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDisguise(Player... players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDisguise(Collection<? extends Player> player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPacket(Player player, Object packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPacket(Collection<? extends Player> players, Object packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPacket(Object packet, Player... players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeDisguise(EntityDisguises newDisguise) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEntityClassName(EntityDisguises entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeDisguise() {
		// TODO Auto-generated method stub

	}

}
