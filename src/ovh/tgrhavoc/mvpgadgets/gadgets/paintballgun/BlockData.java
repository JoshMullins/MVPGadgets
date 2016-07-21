package ovh.tgrhavoc.mvpgadgets.gadgets.paintballgun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.SkullType;
import org.bukkit.block.Banner;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

/**
 * It turns out this entire class wasn't even needed, forever RIP in peace.
 * This has been kept as a potentially useful thing if someone else needs
 * to edit blocks and stuff.
 * @authors pookeythekid, Jordan Dalton
 *
 */
public class BlockData {
	
	enum MetaType {
		BANNER("Banner"),
		BEACON("Beacon"),
		BREWINGSTAND("BrewingStand"),
		CHEST("Chest"),
		COMMANDBLOCK("CommandBlock"),
		CREATURESPAWNER("CreatureSpawner"),
		DISPENSER("Dispenser"),
		DROPPER("Dropper"),
		FURNACE("Furnace"),
		HOPPER("Hopper"),
		JUKEBOX("Jukebox"),
		NOTEBLOCK("NoteBlock"),
		SIGN("Sign"),
		SKULL("Skull");
		
		String type;
		MetaType (String type) {
			this.type = type;
		}
		public String getType() {
			return this.type;
		}
	}
	
	private BlockState state;
	private boolean isMetaType;
	private MetaType metaType;
	// boolean isInventoryHolder;
	
	private Banner banner;
	private Beacon beacon;
	private BrewingStand brewingStand;
	private Map<Integer, ItemStack> brewingItems = new HashMap<>();
	private Chest chest;
	private CommandBlock commandBlock;
	private CreatureSpawner creatureSpawner;
	private Dispenser dispenser;
	private Dropper dropper;
	private Furnace furnace;
	private Hopper hopper;
	private Jukebox jukebox;
	private NoteBlock noteblock;
	private Sign sign;
	private Skull skull;
	
	public BlockData(MVPGadgets plugin, Block block){
		state = block.getState();
		
		switch (state.getType()) {
		case BANNER:
			this.metaType = MetaType.BANNER;
			this.banner = (Banner) state;
			this.isMetaType = true;
			break;
		case BEACON:
			this.metaType = MetaType.BEACON;
			this.beacon = (Beacon) state;
			this.isMetaType = true;
			break;
		case BREWING_STAND:
			this.metaType = MetaType.BREWINGSTAND;
			this.brewingStand = (BrewingStand) state;
			for (int i=0; i<4; i++) {
				this.brewingItems.put(i, this.brewingStand.getInventory().getItem(i));
			}
			this.isMetaType = true;
			break;
		case CHEST:
			this.metaType = MetaType.CHEST;
			this.chest = (Chest) state;
			this.isMetaType = true;
			break;
		case COMMAND:
			this.metaType = MetaType.COMMANDBLOCK;
			this.commandBlock = (CommandBlock) state;
			this.isMetaType = true;
			break;
		case MOB_SPAWNER:
			this.metaType = MetaType.CREATURESPAWNER;
			this.creatureSpawner = (CreatureSpawner) state;
			this.isMetaType = true;
			break;
		case DISPENSER:
			this.metaType = MetaType.DISPENSER;
			this.dispenser = (Dispenser) state;
			this.isMetaType = true;
			break;
		case DROPPER:
			this.metaType = MetaType.DROPPER;
			this.dropper = (Dropper) state;
			this.isMetaType = true;
			break;
		case FURNACE:
			this.metaType = MetaType.FURNACE;
			this.furnace = (Furnace) state;
			this.isMetaType = true;
			break;
		case HOPPER:
			this.metaType = MetaType.HOPPER;
			this.hopper = (Hopper) state;
			this.isMetaType = true;
			break;
		case JUKEBOX:
			this.metaType = MetaType.JUKEBOX;
			this.jukebox = (Jukebox) state;
			this.isMetaType = true;
			break;
		case NOTE_BLOCK:
			this.metaType = MetaType.NOTEBLOCK;
			this.noteblock = (NoteBlock) state;
			this.isMetaType = true;
			break;
		case SIGN_POST:
			this.metaType = MetaType.SIGN;
			this.sign = (Sign) state;
			this.isMetaType = true;
			break;
		case WALL_SIGN:
			this.metaType = MetaType.SIGN;
			this.sign = (Sign) state;
			this.isMetaType = true;
			break;
		case SKULL:
			this.metaType = MetaType.SKULL;
			this.skull = (Skull) state;
			this.isMetaType = true;
			break;
		default:
			this.metaType = null;
			this.isMetaType = false;
			break;
		}
		
		Bukkit.broadcastMessage(String.valueOf(isMetaType));
		if (this.isMetaType)
			Bukkit.broadcastMessage(this.metaType.toString());
		// End constructor
	}
	
	public Material getMaterial() {
		return this.state.getType();
	}
	public BlockState getData() {
		return state;
	}
	public boolean isMetaType() {
		return this.isMetaType;
	}
	public MetaType getMetaType() {
		return this.metaType;
	}
	public Banner getBanner() {
		return this.banner;
	}
	public Beacon getBeacon() {
		return this.beacon;
	}
	public BrewingStand getBrewingStand() {
		return this.brewingStand;
	}
	public Chest getChest() {
		return this.chest;
	}
	public CommandBlock getCommandBlock() {
		return this.commandBlock;
	}
	public CreatureSpawner getCreatureSpawner() {
		return this.creatureSpawner;
	}
	public Dispenser getDispenser() {
		return this.dispenser;
	}
	public Dropper getDropper() {
		return this.dropper;
	}
	public Furnace getFurnace() {
		return this.furnace;
	}
	public Hopper getHopper() {
		return this.hopper;
	}
	public Jukebox getJukebox() {
		return this.jukebox;
	}
	public NoteBlock getNoteblock() {
		return this.noteblock;
	}
	public Sign getSign() {
		return this.sign;
	}
	public Skull getSkull() {
		return this.skull;
	}
	
	public void updateSpecialState(BlockData data) {
		if (data.isMetaType())
			switch (data.getMetaType()) {
			case BANNER:
				Banner banner = data.getBanner();
				List<Pattern> patterns = banner.getPatterns();
				banner.update(true);
				Banner banner2 = (Banner) banner.getBlock().getState();
				banner2.setPatterns(patterns);
				banner2.update(true);
				break;
			case BEACON:
				Beacon beacon = data.getBeacon();
				/*
				BeaconState beaconState = new BeaconState(beacon);
				PotionEffectType potEffectPrimary = beaconState.getPrimary();
				PotionEffectType potEffectSecondary = beaconState.getSecondary();
				*/
				beacon.update(true);
				Beacon beacon2 = (Beacon) beacon.getBlock().getState();
				/*
				BeaconState beaconState2 = new BeaconState(beacon2);
				beaconState2.setPrimary(potEffectPrimary);
				beaconState2.setSecondary(potEffectSecondary);
				*/
				beacon2.update(true);
				break;
			case BREWINGSTAND:
				BrewingStand brewingStand = data.getBrewingStand();
				//Bukkit.broadcastMessage(String.valueOf(brewingStand.getInventory().getItem(0).getAmount()));
				int brewingTime = brewingStand.getBrewingTime();
				brewingStand.update(true);
				BrewingStand brewingStand2 = (BrewingStand) brewingStand.getBlock().getState();
				ItemStack item1 = this.brewingItems.get(0);
				ItemStack item2 = this.brewingItems.get(1);
				ItemStack item3 = this.brewingItems.get(2);
				ItemStack item4 = this.brewingItems.get(3);
				brewingStand2.getInventory().setContents(new ItemStack[] {item1, item2, item3, item4});
				brewingStand2.setBrewingTime(brewingTime);
				brewingStand2.update(true);
				break;
			case CHEST:
				Chest chest = data.getChest();
				Inventory chestInv = chest.getBlockInventory();
				chest.update(true);
				Chest chest2 = (Chest) chest.getBlock().getState();
				chest2.getBlockInventory().setContents(chestInv.getContents());
				chest2.update(true);
				break;
			case COMMANDBLOCK:
				CommandBlock commandBlock = data.getCommandBlock();
				String command = commandBlock.getCommand();
				String name = commandBlock.getName();
				commandBlock.update(true);
				CommandBlock commandBlock2 = (CommandBlock) commandBlock.getBlock().getState();
				commandBlock2.setCommand(command);
				commandBlock2.setName(name);
				commandBlock2.update(true); //lol that rhymes (then again they all rhyme, but this one rolls off the tongue)
				break;
			case CREATURESPAWNER:
				CreatureSpawner creatureSpawner = data.getCreatureSpawner();
				int delay = creatureSpawner.getDelay();
				EntityType spawnedType = creatureSpawner.getSpawnedType();
				creatureSpawner.update(true);
				CreatureSpawner creatureSpawner2 = (CreatureSpawner) creatureSpawner.getBlock().getState();
				creatureSpawner2.setDelay(delay);
				creatureSpawner2.setSpawnedType(spawnedType);
				creatureSpawner2.update(true); //I guess the ones that rhyme best have 2 or more syllables
				break;
			case DISPENSER:
				Dispenser dispenser = data.getDispenser();
				Inventory dispenserInv = dispenser.getInventory();
				dispenser.update(true);
				Dispenser dispenser2 = (Dispenser) dispenser.getBlock().getState();
				dispenser2.getInventory().setContents(dispenserInv.getContents());
				dispenser2.update(true);
				break;
			case DROPPER:
				Dropper dropper = data.getDropper();
				Inventory dropperInv = dropper.getInventory();
				dropper.update(true);
				Dropper dropper2 = (Dropper) dropper.getBlock().getState();
				dropper2.getInventory().setContents(dropperInv.getContents());
				dropper2.update(true);
				break;
			case FURNACE:
				Furnace furnace = data.getFurnace();
				short burnTime = furnace.getBurnTime();
				short cookTime = furnace.getCookTime();
				FurnaceInventory furnaceInv = furnace.getInventory();
				ItemStack fuel = furnaceInv.getFuel();
				ItemStack smelting = furnaceInv.getSmelting();
				ItemStack result = furnaceInv.getResult();
				furnace.update(true);
				Furnace furnace2 = (Furnace) furnace.getBlock().getState();
				FurnaceInventory furnaceInv2 = furnace2.getInventory();
				furnace2.setBurnTime(burnTime);
				furnace2.setCookTime(cookTime);
				furnaceInv2.setFuel(fuel);
				furnaceInv2.setSmelting(smelting);
				furnaceInv2.setResult(result);
				furnace2.update(true);
				break;
			case HOPPER:
				Hopper hopper = data.getHopper();
				Inventory hopperInv = hopper.getInventory();
				hopper.update(true);
				Hopper hopper2 = (Hopper) hopper.getBlock().getState();
				hopper2.getInventory().setContents(hopperInv.getContents());
				hopper2.update(true);
				break;
			case JUKEBOX:
				Jukebox jukebox = data.getJukebox();
				Material playing = jukebox.getPlaying();
				jukebox.update(true);
				Jukebox jukebox2 = (Jukebox) jukebox.getBlock().getState();
				jukebox2.setPlaying(playing);
				break;
			case NOTEBLOCK:
				NoteBlock noteblock = data.getNoteblock();
				Note note = noteblock.getNote();
				noteblock.update(true);
				NoteBlock noteblock2 = (NoteBlock) noteblock.getBlock().getState();
				noteblock2.setNote(note);
				noteblock2.update(true);
				break;
			case SIGN:
				Sign sign = data.getSign();
				sign.update(true);
				Sign sign2 = (Sign) sign.getBlock().getState();
				for (int i=0; i<sign.getLines().length; i++)
					sign2.setLine(i, sign.getLine(i));
				sign2.update(true);
				break;
			case SKULL:
				Skull skull = data.getSkull();
				SkullType skullType = skull.getSkullType();
				BlockFace rotation = skull.getRotation();
				String owner = skull.getOwner();
				skull.update(true);
				Skull skull2 = (Skull) skull.getBlock().getState();
				skull2.setSkullType(skullType);
				skull2.setRotation(rotation);
				skull2.setOwner(owner);
				skull2.update(true);
				break;
			default:
				break;
			}
		else throw new IllegalArgumentException("BlockData must be a MetaType to update its special state.");
	}
}