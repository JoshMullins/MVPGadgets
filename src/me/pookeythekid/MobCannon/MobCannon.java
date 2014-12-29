package me.pookeythekid.MobCannon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.darkblade12.particleeffect.ParticleEffect;

/**
 * 
 * @author pookeythekid
 * @version 0.0.3
 *
 */
public class MobCannon implements CommandExecutor, Listener {

	public JavaPlugin plugin;

	public static Map<String, EntityType> usedNames = new HashMap<>();

	private List<Snowman> snowmen = new ArrayList<>();

	private List<BlockState> blockList = new ArrayList<>();


	/**
	 * Simplest constructor. Will add EntityTypes and mob aliases to the class by itself, instead of having them inputted. Use the other constructor to customize mob aliases.
	 * 
	 * @param plugin - Main plugin class.
	 * @param blacklist - A Set of EntityTypes to remove from the default settings.
	 */
	public MobCannon(JavaPlugin plugin, Set<EntityType> blacklist) {

		Map<EntityType, Set<String>> map = new HashMap<>();

		Set<String> names = new HashSet<>();

		map.put(EntityType.BAT, names);

		map.put(EntityType.BLAZE, names);

		map.put(EntityType.CAVE_SPIDER, names);

		map.put(EntityType.CHICKEN, names);

		map.put(EntityType.COW, names);

		map.put(EntityType.CREEPER, names);

		map.put(EntityType.ENDERMAN, names);

		map.put(EntityType.GIANT, names);

		map.put(EntityType.HORSE, names);

		map.put(EntityType.IRON_GOLEM, names);

		map.put(EntityType.MUSHROOM_COW, names);

		map.put(EntityType.OCELOT, names);

		map.put(EntityType.PIG, names);

		map.put(EntityType.PIG_ZOMBIE, names);

		map.put(EntityType.SHEEP, names);

		map.put(EntityType.SILVERFISH, names);

		map.put(EntityType.SKELETON, names);

		map.put(EntityType.SNOWMAN, names);

		map.put(EntityType.SPIDER, names);

		map.put(EntityType.SQUID, names);

		map.put(EntityType.VILLAGER, names);

		map.put(EntityType.WITCH, names);

		map.put(EntityType.WOLF, names);

		map.put(EntityType.ZOMBIE, names);

		if (blacklist != null) {

			for (EntityType eType : map.keySet()) {

				if (blacklist.contains(eType))

					map.remove(eType);

			}

		}

		mobCannon(plugin, map, false);


	}

	/**
	 * More complex connstructor. Has input for alternate mob names and use of boss mobs and giants. Do NOT use null for any parameters.
	 * Exact same thing as the mobCannon(plugin, mobAliases, giants) function, except it's a constructor.
	 * 
	 * @param plugin - Main plugin class.
	 * @param mobAliases - Keys of actual mobs, values of acceptable mob names. An empty Set<String> String will work, since the default names (i.e.: IRON_GOLEM will look like "iron_golem" and "irongolem") are generated regardless.
	 * @param giants - Defines whether the plugin allows giants (hundred-foot-tall zombies) or not. Giants are not at all recommended.
	 */
	public MobCannon(JavaPlugin plugin, Map<EntityType, Set<String>> mobAliases, boolean giants) {

		mobCannon(plugin, mobAliases, giants);

	}

	/**
	 * Has input for alternate mob names and use of boss mobs and giants. Do NOT use null for any parameters.
	 * 
	 * @param plugin - Main plugin class.
	 * @param mobAliases - Keys of actual mobs, values of acceptable mob names. An empty Set<String> will work, since the default names (i.e.: IRON_GOLEM will look like "iron_golem" and "irongolem") are generated regardless.
	 * @param giants - Defines whether the plugin allows giants (hundred-foot-tall zombies) or not.
	 */
	public void mobCannon(JavaPlugin plugin, Map<EntityType, Set<String>> mobAliases, boolean giants) {

		this.plugin = plugin;

		HashMap<String, EntityType> tempMap = new HashMap<String, EntityType>();

		for (EntityType eType : mobAliases.keySet()) {

			if (eType.equals(EntityType.ENDER_DRAGON) || eType.equals(EntityType.WITHER))

				continue;

			if (!giants && eType.equals(EntityType.GIANT))

				continue;

			for (String name : mobAliases.get(eType)) {

				if (!tempMap.containsKey(name.toLowerCase())) {

					tempMap.put(name.toLowerCase(), eType);

				}

			}

			if (!tempMap.containsKey(eType.toString().toLowerCase()))

				tempMap.put(eType.toString().toLowerCase(), eType);

			if (!tempMap.containsKey(eType.toString().replace("_", "").toLowerCase()))

				tempMap.put(eType.toString().replace("_", "").toLowerCase(), eType);

		}

		usedNames = tempMap;


		PluginManager pm = plugin.getServer().getPluginManager();

		pm.registerEvents(this, plugin);

		Permission launchCommand = new Permission("mvpgadgets.launchmob");

		Permission randomLaunch = new Permission("mvpgadgets.launchmob.random");

		Permission allMobLaunch = new Permission("mvpgadgets.launchmob.all");

		Permission allLaunchers = new Permission("mvpgadgets.launchmob.*");

		pm.addPermission(launchCommand);

		pm.getPermission("mvpgadgets.launchmob").setDefault(PermissionDefault.OP);

		pm.addPermission(randomLaunch);

		pm.getPermission("mvpgadgets.launchmob.random").setDefault(PermissionDefault.OP);

		pm.addPermission(allMobLaunch);

		pm.getPermission("mvpgadgets.launchmob.all").setDefault(PermissionDefault.OP);

		pm.addPermission(allLaunchers);

		pm.getPermission("mvpgadgets.launchmob.*").setDefault(PermissionDefault.OP);

		pm.getPermission("mvpgadgets.launchmob").addParent(randomLaunch, true);

		pm.getPermission("mvpgadgets.launchmob").addParent(allMobLaunch, true);

		pm.getPermission("mvpgadgets.launchmob").addParent(allLaunchers, true);

		pm.getPermission("mvpgadgets.launchmob.random").addParent(allLaunchers, true);

		pm.getPermission("mvpgadgets.launchmob.all").addParent(allLaunchers, true);


	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("launchmob")) {

			if (!sender.hasPermission("mvpgadgets.launchmob")) {

				sender.sendMessage(ChatColor.RED + "You don't have permission to launch mobs.");

				return true;

			}

			if (!(sender instanceof Player))

				sender.sendMessage("Nope. You're a console.");

			else {

				if (args.length < 1)

					launchMob("", (Player) sender, usedNames);

				else if (args.length > 0)

					launchMob(args[0], (Player) sender, usedNames);

			}

		}

		return true;

	}


	public void launchMob(String mobName, final Player p, Map<String, EntityType> map) {

		if (!mobName.isEmpty()) {

			if (inputNameValid(mobName, map)) {

				if (!p.hasPermission("mvpgadgets.launchmob." + getNameFrom(mobName, map))) {

					p.sendMessage(ChatColor.RED + "You don't have permission to launch a(n) " + mobName);

					return;

				}

				Creature creature;

				Entity entity;

				final Location ploc = p.getLocation();

				Location spawnLoc = ploc.add(0, 2, 0);

				switch (getNameFrom(mobName, map)) {

				// Casting Entity to Creature doesn't work with setting targets for other creatures.
				// Ghast has Craftbukkit casting issue with Creature; can't set it to non-hostile without Creature.
				// Magmacube has same casting issue, also can't set it to non-hostile without Creature.
				// Slime is same thing as Magmacube.
				// Boss mobs removed.

				case "bat":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.BAT);

					break;

				case "blaze":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.BLAZE);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "cave_spider":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "chicken":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.CHICKEN);

					break;

				case "cow":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.COW);

					break;

				case "creeper":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.CREEPER);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "enderman":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "giant":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.GIANT);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "horse":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.HORSE);

					break;

				case "iron_golem":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.IRON_GOLEM);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "mushroom_cow":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.MUSHROOM_COW);

					break;

				case "ocelot":

					Ocelot ocelot = (Ocelot) p.getWorld().spawnEntity(spawnLoc, EntityType.OCELOT);

					ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]);

					ocelot.setTamed(true);

					if (new Random().nextBoolean() && !ocelot.getCatType().equals(Ocelot.Type.WILD_OCELOT))

						ocelot.setBaby();

					entity = (Entity) ocelot;

					for (Entity e : ploc.getChunk().getEntities()) {

						if (e.getType().equals(EntityType.OCELOT)) {

							Ocelot o = (Ocelot) e;

							if (!o.isAdult() && o.getCatType().equals(Ocelot.Type.WILD_OCELOT))

								o.remove();

						}

					}

					break;

				default:
				case "pig":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.PIG);

					break;

				case "pig_zombie":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.PIG_ZOMBIE);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "sheep":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SHEEP);

					break;

				case "silverfish":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SILVERFISH);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "skeleton":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "snowman":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SNOWMAN);

					creature = (Creature) entity;

					creature.setTarget(null);

					snowmen.add((Snowman) creature);

					break;

				case "spider":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "squid":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SQUID); // Note: Squids aren't affected by setVelocity

					break;

				case "villager":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);

					break;

				case "witch":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.WITCH);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "wolf":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				case "zombie":

					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);

					creature = (Creature) entity;

					creature.setTarget(null);

					break;

				}

				entity.setVelocity(ploc.getDirection().multiply(2));

				final Entity entity2 = entity;

				new BukkitRunnable() {

					@Override
					public void run() {

						Location eloc = entity2.getLocation();

						ParticleEffect.EXPLOSION_LARGE.display(1, 1, 1, 1, 5, eloc, 100);

						eloc.getWorld().playSound(eloc, Sound.EXPLODE, 1, 1);

						entity2.remove();

						for (BlockState blockstate : blockList) {

							blockstate.setType(Material.AIR);

							blockstate.update(true, true);

						}

					}

				}.runTaskLater(plugin, 20);

			}

			else

				p.sendMessage(ChatColor.RED + "Mob name '" + mobName + "' is invalid.");

		}

		else {

			if (!p.hasPermission("mvpgadgets.launchmob.random")) {

				p.sendMessage(ChatColor.RED + "You must specify a creature to launch.");

				return;

			}

			ArrayList<EntityType> eList = new ArrayList<EntityType>();

			for (EntityType eType : map.values()) {

				if (!eList.contains(eType) && eType != EntityType.GIANT)

					eList.add(eType);

			}

			final Location ploc = p.getLocation();

			Location spawnLoc = ploc.add(0, 2, 0);
			
			EntityType eType = eList.get(new Random().nextInt(eList.size()));
			
			while (!p.hasPermission("mvpgadgets.launchmob." + getNameFrom(eType.toString(), map)))
				
				eType = eList.get(new Random().nextInt(eList.size()));

			Entity entity;

			entity = p.getWorld().spawnEntity(spawnLoc, eType);

			if (entity instanceof Monster || entity.getType().equals(EntityType.IRON_GOLEM) || entity.getType().equals(EntityType.SNOWMAN)
					|| entity.getType().equals(EntityType.WOLF)) {

				Creature creature = (Creature) entity;

				creature.setTarget(null);

			}

			if (entity.getType().equals(EntityType.SNOWMAN))

				snowmen.add((Snowman) entity);

			if (entity.getType().equals(EntityType.OCELOT)) {

				entity.remove();

				Ocelot ocelot = (Ocelot) p.getWorld().spawnEntity(spawnLoc, EntityType.OCELOT);

				ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]); 

				ocelot.setTamed(true);

				while (ocelot.getCatType().equals(Ocelot.Type.WILD_OCELOT))

					ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]); 

				if (new Random().nextBoolean())

					ocelot.setBaby();

				entity = (Entity) ocelot;

				for (Entity e : ploc.getChunk().getEntities()) {

					if (e.getType().equals(EntityType.OCELOT)) {

						Ocelot o = (Ocelot) e;

						if (!o.isAdult() && o.getCatType().equals(Ocelot.Type.WILD_OCELOT))

							o.remove();

					}

				}

			}

			final Entity entity2 = entity;

			entity.setVelocity(ploc.getDirection().multiply(2));

			new BukkitRunnable() {

				@Override
				public void run() {

					Location eloc = entity2.getLocation();

					ParticleEffect.EXPLOSION_LARGE.display(1, 1, 1, 1, 5, eloc, 100);

					eloc.getWorld().playSound(eloc, Sound.EXPLODE, 1, 1);

					entity2.remove();

				}

			}.runTaskLater(plugin, 20);

		}

	}

	public boolean inputNameValid(String name, Map<String, EntityType> map) {

		boolean valid = false;

		if (map.containsKey(name.toLowerCase()))

			valid = true;

		return valid;

	}

	public String getNameFrom(String name, Map<String, EntityType> map) {

		if (inputNameValid(name, map)) {

			return map.get(name.toLowerCase()).toString().toLowerCase();

		}

		return null;

	}


	@EventHandler
	public void onBlockForm(final EntityBlockFormEvent e) {

		Entity entity = e.getEntity();

		if (entity.getType().equals(EntityType.SNOWMAN) && snowmen.contains((Snowman) entity)) {

			blockList.add(e.getBlock().getState());

			e.setCancelled(true);

			e.getBlock().getState().update(true, true);

			new BukkitRunnable() {

				@Override
				public void run() {

					BlockState blockstate = e.getBlock().getState();

					blockstate.setType(Material.AIR);

					blockstate.update(true, true);

				}

			}.runTaskLater(plugin, 22);

		}

	}


}
