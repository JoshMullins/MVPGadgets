package me.pookeythekid.MobCannon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
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
import org.bukkit.scheduler.BukkitRunnable;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

import com.darkblade12.particleeffect.ParticleEffect;

/**
 * 
 * @author pookeythekid
 * @version 1.1.1
 *
 */
public class MobCannon implements CommandExecutor, Listener {
	
	final String BASE_PATH = "Messages.MobCannon";
	final String MOBCANNON = BASE_PATH +".mobcannon";
	final String MOBCANNONRELOAD = BASE_PATH +".mobcannonreload";
	final String MOBLIST = BASE_PATH +".moblist";
	final String MOBNAMES = BASE_PATH +".mobnames";
	final String LAUNCHMOBS = BASE_PATH+".launchmob";

	private MVPGadgets plugin;
	public Map<String, EntityType> usedNames = new HashMap<>();
	private List<Snowman> snowmen = new ArrayList<>();
	/* All things pertaining to blockList (list of launched-snowman-created snow blocks)
	 * were removed (or at least commented out) in v1.1.1 .
	 * They used to be necessary on top of cancelling the create event, but
	 * for reasons unknown they are now redundant (at least in MC 1.8+).
	 */
	// private List<BlockState> blockList = new ArrayList<>();
	private final Random rand = new Random();
	public boolean enabledOnce = false;
	private boolean registeredPerms = false;
	private PluginManager pluginManager;
	
	/**
	 * Simple constructor. Will add EntityTypes and mob aliases to the instance by itself, instead of having them inputted.
	 * Use the other constructor to customize mob aliases.
	 * 
	 * @param plugin The plugin that's using this cannon.
	 * @param blacklist A Set of EntityTypes to remove from the default settings.
	 */
	public MobCannon(MVPGadgets plugin, Set<EntityType> blacklist) {
		mobCannon(plugin, blacklist);
	}

	/**
	 * Simple constructing method. Will add EntityTypes and mob aliases to the instance by itself, instead of having them inputted.
	 * 
	 * @param plugin The plugin that's using this cannon.
	 * @param blacklist A Set of EntityTypes to remove from the default settings.
	 */
	public void mobCannon(MVPGadgets plugin, Set<EntityType> blacklist) {
		Map<EntityType, Set<String>> map = new HashMap<>();

		// Casting Entity to Creature doesn't work with setting targets for other creatures.
		// Ghast has Craftbukkit casting issue with Creature; can't set it to non-hostile without Creature.
		// Magmacube has same casting issue, also can't set it to non-hostile without Creature.
		// Slime is same thing as Magmacube.
		// Boss mobs removed.

		EntityType[] types = {
				EntityType.BAT,
				EntityType.BLAZE,
				EntityType.CAVE_SPIDER,
				EntityType.CHICKEN,
				EntityType.COW,
				EntityType.CREEPER,
				EntityType.ENDERMAN,
				EntityType.GIANT,
				EntityType.HORSE,
				EntityType.IRON_GOLEM,
				EntityType.MUSHROOM_COW,
				EntityType.OCELOT,
				EntityType.PIG,
				EntityType.PIG_ZOMBIE,
				EntityType.SHEEP,
				EntityType.SILVERFISH,
				EntityType.SKELETON,
				EntityType.SNOWMAN,
				EntityType.SPIDER,
				EntityType.SQUID,
				EntityType.VILLAGER,
				EntityType.WITCH,
				EntityType.WOLF,
				EntityType.ZOMBIE
		};

		for (EntityType type : types)
			if (blacklist == null || !blacklist.contains(type))
				map.put(type, new HashSet<String>());

		mobCannon(plugin, map, map.containsKey(EntityType.GIANT));
	}

	/**
	 * More complex connstructor. Has input for alternate mob names and use of boss mobs and giants. Do NOT use null for any parameters.
	 * Exact same thing as the mobCannon(plugin, mobAliases, giants) method, except it's a constructor.
	 * 
	 * @param plugin The plugin that's using this cannon.
	 * @param mobAliases Keys of actual mobs, values of acceptable mob names. An empty Set<String> String will work, since the default names
	 * (i.e. IRON_GOLEM will look like "iron_golem" and "irongolem") are generated regardless.
	 * @param giants Defines whether the plugin allows giants (hundred-foot-tall zombies) or not. Giants are not at all recommended.
	 */
	public MobCannon(MVPGadgets plugin, Map<EntityType, Set<String>> mobAliases, boolean giants) {
		mobCannon(plugin, mobAliases, giants);
	}

	/**
	 * More complex constructing method. Has input for alternate mob names and use of boss mobs and giants.
	 * Do NOT use null for any parameters.
	 * 
	 * @param plugin The plugin that's using this cannon.
	 * @param mobAliases Keys of actual mobs, values of acceptable mob names. An empty Set<String> will work, since the default names
	 * (i.e. IRON_GOLEM will look like "iron_golem" and "irongolem") are generated regardless.
	 * @param giants Defines whether the plugin allows giants (hundred-foot-tall zombies) or not.
	 */
	public void mobCannon(MVPGadgets plugin, Map<EntityType, Set<String>> mobAliases, boolean giants) {
		if(plugin == null)
			throw new IllegalArgumentException("plugin cannot be null!");
		else
			this.plugin = plugin;

		if(mobAliases == null)
			this.mobCannon(plugin, null);

		HashMap<String, EntityType> tempMap = new HashMap<>();

		for (EntityType eType : mobAliases.keySet()) {
			if (eType == EntityType.ENDER_DRAGON || eType == EntityType.WITHER)
				continue;

			if (!giants && eType == EntityType.GIANT)
				continue;

			for (String name : mobAliases.get(eType))
				if (!tempMap.containsKey(name.toLowerCase()))
					tempMap.put(name.toLowerCase(), eType);

			if (!tempMap.containsKey(eType.toString().	toLowerCase()))
				tempMap.put(eType.toString().toLowerCase(), eType);

			if (!tempMap.containsKey(eType.toString().replace("_", "").toLowerCase()))
				tempMap.put(eType.toString().replace("_", "").toLowerCase(), eType);
		}

		usedNames = tempMap;

		pluginManager = plugin.getServer().getPluginManager();
		if (!enabledOnce) {
			pluginManager.registerEvents(this, plugin);
			enabledOnce = true;
			// Without this check, the events will register multiple times, making them fire as many times as this is called.
		}
		//registerPerms(pluginManager);
	}

	/**
	 * Register permissions to the server.
	 * 
	 * @param pm PluginManager to use.
	 * @deprecated In favor of String-based permissions.
	 */
	@Deprecated
	public void registerPerms(PluginManager pm) {
		/*
		 * Permissions layout
		 * 
		 * mobcannon.use: base help command.
		 *     parents: all of the other permissions. The help menu only shows commands that the user has permission for.
		 * mobcannon.launchmob: base permission to be able to launch a mob.
		 *     parents: mobcannon.launchmob.random, mobcannon.launchmob.all, mobcannon.launchmob.*, mobcannon.*
		 * mobcannon.launchmob.random: launch random mobs without specification in the /launchmob [mob] command.
		 *     parents: mobcannon.launchmob.*, mobcannon.*
		 * mobcannon.launchmob.all: permission to all mobs, but does not grant access to the random launcher.
		 *     parents: mobcannon.launchmob.*, mobcannon.*
		 * mobcannon.moblist: list all available mobs, meaning those that both the user has permission for and the config file allows.
		 *     parents: mobcannon.*
		 * mobcannon.mobnames: list all alternate names for a given mob name.
		 *     parents: mobcannon.*
		 * mobcannon.reload: reload the plugin.
		 *     parents: mobcannon.*
		 * mobcannon.*: all permissions.
		 */
		if (!registeredPerms) {
			Permission base = new Permission("mvpgadgets.mobcannon.use");
			Permission launchCommand = new Permission("mvpgadgets.mobcannon.launchmob");
			Permission randomLaunch = new Permission("mvpgadgets.mobcannon.launchmob.random");
			Permission allMobLaunch = new Permission("mvpgadgets.mobcannon.launchmob.all");
			Permission allLaunchers = new Permission("mvpgadgets.mobcannon.launchmob.*");
			Permission mobList = new Permission("mvpgadgets.mobcannon.moblist");
			Permission mobNames = new Permission("mvpgadgets.mobcannon.mobnames");
			Permission reload = new Permission("mvpgadgets.mobcannon.reload");
			Permission allPerms = new Permission("mvpgadgets.mobcannon.*");

			base.setDefault(PermissionDefault.OP);
			launchCommand.setDefault(PermissionDefault.OP);
			randomLaunch.setDefault(PermissionDefault.OP);
			allMobLaunch.setDefault(PermissionDefault.OP);
			allLaunchers.setDefault(PermissionDefault.OP);
			mobList.setDefault(PermissionDefault.OP);
			mobNames.setDefault(PermissionDefault.OP);
			reload.setDefault(PermissionDefault.OP);
			allPerms.setDefault(PermissionDefault.OP);

			base.addParent(launchCommand, true);
			base.addParent(randomLaunch, true);
			base.addParent(allMobLaunch, true);
			base.addParent(allLaunchers, true);
			base.addParent(mobList, true);
			base.addParent(mobNames, true);
			base.addParent(reload, true);
			base.addParent(allPerms, true);

			launchCommand.addParent(randomLaunch, true);
			launchCommand.addParent(allMobLaunch, true);
			launchCommand.addParent(allLaunchers, true);
			launchCommand.addParent(allPerms, true);

			randomLaunch.addParent(allLaunchers, true);
			randomLaunch.addParent(allPerms, true);
			allMobLaunch.addParent(allLaunchers, true);
			allMobLaunch.addParent(allPerms, true);
			allLaunchers.addParent(allPerms, true);
			mobList.addParent(allPerms, true);
			mobNames.addParent(allPerms, true);
			reload.addParent(allPerms, true);

			pm.addPermission(base);
			pm.addPermission(launchCommand);
			pm.addPermission(randomLaunch);
			pm.addPermission(allMobLaunch);
			pm.addPermission(allLaunchers);
			pm.addPermission(mobList);
			pm.addPermission(mobNames);
			pm.addPermission(reload);
			pm.addPermission(allPerms);

			registeredPerms = true;
		}
	}

	/**
	 * Unregister permissions from the server.
	 * 
	 * @param pm PluginManager to use.
	 * @deprecated In favor of String-based permissions.
	 */
	@Deprecated
	public void removePerms(PluginManager pm) {
		if (registeredPerms) {
			pm.removePermission("mvpgadgets.mobcannon.use");
			pm.removePermission("mvpgadgets.mobcannon.launchmob");
			pm.removePermission("mvpgadgets.mobcannon.launchmob.random");
			pm.removePermission("mvpgadgets.mobcannon.launchmob.all");
			pm.removePermission("mvpgadgets.mobcannon.launchmob.*");
			pm.removePermission("mvpgadgets.mobcannon.moblist");
			pm.removePermission("mvpgadgets.mobcannon.mobnames");
			pm.removePermission("mvpgadgets.mobcannon.reload");
			pm.removePermission("mvpgadgets.mobcannon.*");

			registeredPerms = false;
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("launchmob")) {
			if (!sender.hasPermission("mvpgadgets.mobcannon.launchmob")) {
				sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getMessageFromConfig("Messages.CONSOLE"));
				return true;
			}

			if (args.length < 1)
				launchMob("", (Player) sender, usedNames);

			else
				launchMob(args[0], (Player) sender, usedNames);
		}

		if (cmd.getName().equalsIgnoreCase("mobcannon")) {
			if (sender.hasPermission("mvpgadgets.mobcannon.use")) {
				sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON + ".TITLE"));
				sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON + ".HELP"));
				if (sender.hasPermission("mobcannon.reload"))
					sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON + ".RELOAD"));
				if (sender.hasPermission("mobcannon.launchmob"))
					sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON+".LAUNCH_MOB"));
				if (sender.hasPermission("mobcannon.moblist"))
					sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON+ ".MOBLIST"));
				if (sender.hasPermission("mobcannon.mobnames"))
					sender.sendMessage(plugin.getMessageFromConfig(MOBCANNON + ".MOBNAMES"));
			} else
				sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
		}

		if (cmd.getName().equalsIgnoreCase("mobcannonreload")) {
			if (sender.hasPermission("mvpgadgets.mobcannon.reload")) {
				reloadCannon();
				sender.sendMessage(plugin.getMessageFromConfig(MOBCANNONRELOAD + ".RELOADED"));
			} else
				sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
		}

		if (cmd.getName().equalsIgnoreCase("moblist")) {
			if (sender.hasPermission("mvpgadgets.mobcannon.moblist")) {
				StringBuilder mobNames = new StringBuilder();
				for (String name : usedNames.keySet()) {
					if (sender.hasPermission("mvpgadgets.mobcannon.launchmob." + name.toLowerCase()) || sender.hasPermission("mvpgadgets.mobcannon.launchmob.all"))
						mobNames.append(name.toLowerCase() + ", ");
				}
				if (mobNames.length() == 0) {
					sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
					return true;
				}
				mobNames.setLength(mobNames.length() - 2); // Remove the extra comma and space at the end
				sender.sendMessage(
						plugin.getMessageFromConfig(MOBLIST + ".AVAILABLE_MOBS").replace("{MOB_NAMES}", mobNames.toString()));
			} else
				sender.sendMessage(plugin.getMessageFromConfig("Messages.GENERIC_NO_PERMISSION"));
		}

		if (cmd.getName().equalsIgnoreCase("mobnames")) {
			if (sender.hasPermission("mvpgadgets.mobcannon.mobnames")) {
				if (args.length > 0) {
					if (getNameFrom(args[0], usedNames) != null) {
						String mobName = getNameFrom(args[0], usedNames);
						StringBuilder names = new StringBuilder();
						for (String name : usedNames.keySet())
							if (getNameFrom(name, usedNames).equalsIgnoreCase(mobName))
								names.append(name.toLowerCase() + ", ");
						if (names.length() > 0)
							names.setLength(names.length() - 2); // Remove the extra comma and space
						else {
							names.setLength(0);
							names.append("None");
						}
						sender.sendMessage(plugin.getMessageFromConfig(MOBNAMES + ".ALTERNATIVE_NAMES")
								.replace("{NAME}", args[0]).replace("{NAMES}", names.toString()));
					} else
						sender.sendMessage(plugin.getMessageFromConfig(MOBNAMES + ".INVALID_MOB").replace("{NAME}", args[0]));
				} else
					sender.sendMessage(plugin.getMessageFromConfig(MOBNAMES + ".LITTLE_ARGUMENTS"));
			}
		}

		return true;
	}
	
	public void reloadCannon() {
		plugin.reloadConfig();
		pluginManager = plugin.getServer().getPluginManager(); // Can't hurt to reinitialize it
		//removePerms(pluginManager);
		mobCannon(plugin, null);

		Set<EntityType> blacklist = new HashSet<>();
		List<String> list = plugin.getConfig().getStringList("blacklist");
		for (String str : list)
			blacklist.add(getEntityTypeFrom(str, usedNames));

		mobCannon(plugin, blacklist);

		if (!plugin.getConfig().getBoolean("simpleBlacklist")) {
			Map<EntityType, Set<String>> mobAliases = new HashMap<>();
			Set<String> keySet = plugin.getConfig().getKeys(false);
			for (String key : keySet) {
				if (getNameFrom(key, usedNames) != null && !blacklist.contains(usedNames.get(key.toLowerCase()))) {
					Set<String> names = new HashSet<>();
					names.add(key);
					if (plugin.getConfig().getStringList(key) != null)
						names.addAll(plugin.getConfig().getStringList(key));
					mobAliases.put(getEntityTypeFrom(key, usedNames), names);
				}
			}
			mobCannon(plugin, mobAliases, mobAliases.containsKey(EntityType.GIANT));
		}
	}
	
	/**
	 * The mob-launching method.
	 * 
	 * @param mobName Mob name input.
	 * @param p The player involved in the launch.
	 * @param map A Map of aliases and the EntityTypes they belong to.
	 */
	public void launchMob(String mobName, final Player p, Map<String, EntityType> map) {
		if (!mobName.isEmpty()) {
			if (inputNameValid(mobName, map)) {
				if (!p.hasPermission("mvpgadgets.mobcannon.launchmob." + getNameFrom(mobName, map))
						&& !p.hasPermission("mvpgadgets.mobcannon.launchmob.all")) {
					p.sendMessage(plugin.getMessageFromConfig(LAUNCHMOBS + ".NO_PERMISSION_MOB").replace("{MOB_NAME}", mobName));
					return;
				}

				Creature creature = null;
				Entity entity;

				final Location ploc = p.getLocation();
				Location spawnLoc = ploc.add(0, 2, 0);

				switch (getNameFrom(mobName, map)) {
				case "bat":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.BAT);
					break;
				case "blaze":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.BLAZE);
					creature = (Creature) entity;
					break;
				case "cave_spider":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);
					creature = (Creature) entity;
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
					break;
				case "enderman":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);
					creature = (Creature) entity;
					break;
				case "giant":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.GIANT);
					creature = (Creature) entity;
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
					ocelot.setCatType(Ocelot.Type.values()[rand.nextInt(Ocelot.Type.values().length)]);
					ocelot.setTamed(true);
					if (rand.nextBoolean() && !ocelot.getCatType().equals(Ocelot.Type.WILD_OCELOT))
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
					break;
				case "sheep":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SHEEP);
					break;
				case "silverfish":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SILVERFISH);
					creature = (Creature) entity;
					break;
				case "skeleton":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
					creature = (Creature) entity;
					break;
				case "snowman":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SNOWMAN);
					creature = (Creature) entity;
					snowmen.add((Snowman) creature);
					break;
				case "spider":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
					creature = (Creature) entity;
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
					break;
				case "wolf":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
					creature = (Creature) entity;
					break;
				case "zombie":
					entity = p.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
					creature = (Creature) entity;
					break;
				}

				if(creature != null)
					creature.setTarget(null);
				entity.setVelocity(ploc.getDirection().multiply(2));

				//TGRHavoc: Set the health to the maximum it can so that the
				//Entity won't die and drop items (Issue #7 https://github.com/JOSHSLAYDE/MVPGadgets/issues/7)

				Damageable entityD = (Damageable) entity;
				entityD.setMaxHealth(Double.MAX_VALUE);
				entityD.setHealth(1024D); //apparently 1.8 only allows a max of 1024.0, but setMaxHealth can far exceed that...

				final Entity entity2 = entity;
				new BukkitRunnable() {

					@Override
					public void run() {
						Location eloc = entity2.getLocation();
						ParticleEffect.EXPLOSION_LARGE.display(1, 1, 1, 1, 5, eloc, 100);
						eloc.getWorld().playSound(eloc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						entity2.remove();
						/*
						for (BlockState blockstate : blockList) {
							blockstate.setType(Material.AIR);
							blockstate.update(true, true);
						}
						 */
					}
				}.runTaskLater(plugin, 20);
			} else
				p.sendMessage(plugin.getMessageFromConfig(LAUNCHMOBS + ".INVALID_MOB").replace("{MOB_NAME}", mobName));
		} else {
			if (!p.hasPermission("mvpgadgets.mobcannon.launchmob.random")) {
				p.sendMessage(plugin.getMessageFromConfig(LAUNCHMOBS +".SPECIFY_MOB"));
				return;
			}

			ArrayList<EntityType> eList = new ArrayList<EntityType>();
			for (EntityType eType : map.values())
				if (!eList.contains(eType) && eType != EntityType.GIANT)
					eList.add(eType);

			final Location ploc = p.getLocation();
			Location spawnLoc = ploc.add(0, 2, 0);

			List<EntityType> tempEList = new ArrayList<>();
			tempEList.addAll(eList);
			Iterator<EntityType> it = tempEList.iterator();

			while(it.hasNext())
				if(!p.hasPermission("mobcannon.launchmob." + getNameFrom(it.next().toString(), map)))
					it.remove();

			if(tempEList.isEmpty()) {
				p.sendMessage(plugin.getMessageFromConfig(LAUNCHMOBS +".NO_PERMISSION"));
				return;
			}

			EntityType eType = tempEList.get(rand.nextInt(tempEList.size()));

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
				ocelot.setTamed(true);

				List<Ocelot.Type> catTypes = new ArrayList<>();
				catTypes.addAll(Arrays.asList(Ocelot.Type.values()));
				catTypes.remove(Ocelot.Type.WILD_OCELOT);
				ocelot.setCatType(catTypes.get(rand.nextInt(catTypes.size())));

				if (rand.nextBoolean())
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

			Damageable entityD = (Damageable) entity;
			entityD.setMaxHealth(Double.MAX_VALUE);
			entityD.setHealth(1024D);

			final Entity entity2 = entity;
			entity.setVelocity(ploc.getDirection().multiply(2));

			new BukkitRunnable() {

				@Override
				public void run() {
					Location eloc = entity2.getLocation();
					ParticleEffect.EXPLOSION_LARGE.display(1, 1, 1, 1, 5, eloc, 100);
					eloc.getWorld().playSound(eloc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					entity2.remove();
					/*
					for (BlockState blockstate : blockList) {
						blockstate.setType(Material.AIR);
						blockstate.update(true, true);
					}
					 */
				}

			}.runTaskLater(plugin, 20);
		}
	}

	/**
	 * Test if the given name is an existing mob alias.
	 * 
	 * @param name The String to be tested for name validity.
	 * @param map A Map of aliases and the EntityTypes they belong to.
	 * @return Whether the given String is an existing mob alias.
	 */
	public boolean inputNameValid(String name, Map<String, EntityType> map) {
		return map.containsKey(name.toLowerCase());
	}

	/**
	 * Gives the lower-case form of the proper EntityType name from the given alias.
	 * 
	 * @param name Alias to convert.
	 * @param map A Map of aliases and the EntityTypes they belong to.
	 * @return The lower-case form of the proper EntityType name from the given alias; null if the alias is invalid.
	 */
	public String getNameFrom(String name, Map<String, EntityType> map) {
		if (inputNameValid(name, map))
			return map.get(name.toLowerCase()).toString().toLowerCase();

		return null;
	}
	
	/**
	 * Gives the EntityType of the name of the given alias.
	 * 
	 * @param name Alias to use.
	 * @param map A Map of aliases and the EntityTypes they belong to.
	 * @return The EntityType that corresponds to the give mob name.
	 */
	public EntityType getEntityTypeFrom(String name, Map<String, EntityType> map) {
		if (inputNameValid(name, map))
			return map.get(name.toLowerCase());
		
		return null;
	}

	@EventHandler
	public void onBlockForm(final EntityBlockFormEvent e) {
		Entity entity = e.getEntity();
		if (entity.getType().equals(EntityType.SNOWMAN)) {
			if (snowmen.contains((Snowman) entity)) {
				// blockList.add(e.getBlock().getState());
				e.setCancelled(true);
				e.getBlock().getState().update(true, true);

				/*
				new BukkitRunnable() {

					@Override
					public void run() {
						BlockState blockstate = e.getBlock().getState();
						blockstate.setType(Material.AIR);
						blockstate.update(true, true);
					}

				}.runTaskLater(plugin, 22);
				 */
			}
		}
	}

	/*
	@EventHandler
	public void onBlockEvent(BlockEvent ev) {
		BlockState blockstate = ev.getBlock().getState();
		if (blockList.contains(blockstate))
			blockList.remove(blockstate);
	}
	 */

	public Map<String, EntityType> getUsedNames() {
		return usedNames;
	}
}
