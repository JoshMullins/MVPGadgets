package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractDisguise implements IDisguise {

	private String customName;
	private EntityDisguises disguise;

	private Player player;
	private ItemStack hand, helm, chest, leg, boot;

	public AbstractDisguise(Player toDisguise, EntityDisguises disguise) {
		this(toDisguise, disguise, null);
	}

	public AbstractDisguise(Player toDisguise, EntityDisguises disguise,
			String name) {
		this(toDisguise, disguise, name, null, null, null, null, null);
	}

	public AbstractDisguise(Player p, EntityDisguises type, String name,
			ItemStack inhand, ItemStack helmet, ItemStack chestplate,
			ItemStack leggings, ItemStack boots) {

		this.customName = name;
		this.disguise = type;
		this.player = p;

		this.hand = inhand;
		this.helm = helmet;
		this.chest = chestplate;
		this.leg = leggings;
		this.boot = boots;

	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public EntityDisguises getDisguise() {
		return disguise;
	}

	public void setDisguise(EntityDisguises disguise) {
		this.disguise = disguise;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ItemStack getHand() {
		return hand;
	}

	public void setHand(ItemStack hand) {
		this.hand = hand;
	}

	public ItemStack getHelm() {
		return helm;
	}

	public void setHelm(ItemStack helm) {
		this.helm = helm;
	}

	public ItemStack getChest() {
		return chest;
	}

	public void setChest(ItemStack chest) {
		this.chest = chest;
	}

	public ItemStack getLeg() {
		return leg;
	}

	public void setLeg(ItemStack leg) {
		this.leg = leg;
	}

	public ItemStack getBoot() {
		return boot;
	}

	public void setBoot(ItemStack boot) {
		this.boot = boot;
	}

}
