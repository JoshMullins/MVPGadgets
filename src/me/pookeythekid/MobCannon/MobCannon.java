package me.pookeythekid.MobCannon;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
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

	private JavaPlugin plugin;
	private Map<String, EntityType> usedNames = new HashMap<>();
	private List<Snowman> snowmen = new ArrayList<>();
	private List<BlockState> blockList = new ArrayList<>();
    	private final Random rand = new Random();

	/**
	 * Simplest constructor. Will add EntityTypes and mob aliases to the class by itself, instead of having them inputted. Use the other constructor to customize mob aliases.
	 * 
	 * @param plugin - Main plugin class.
	 * @param blacklist - A Set of EntityTypes to remove from the default settings.
	 */
	public MobCannon(JavaPlugin plugin, Set<EntityType> blacklist) {
		Map<EntityType, Set<String>> map = new HashMap<>();

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

        for(EntityType type : types)
            if(blacklist == null || !blacklist.contains(type))
                map.put(type, new HashSet<String>());

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
        if(plugin == null)
            throw new IllegalArgumentException("plugin cannot be null!");
        if(mobAliases == null)
            throw new IllegalArgumentException("mobAliases cannot be null!");

        this.plugin = plugin;

		HashMap<String, EntityType> tempMap = new HashMap<String, EntityType>();

		for (EntityType eType : mobAliases.keySet()) {
			if (eType == EntityType.ENDER_DRAGON || eType == EntityType.WITHER)
				continue;

			if (!giants && eType == EntityType.GIANT)
				continue;

			for (String name : mobAliases.get(eType))
				if (!tempMap.containsKey(name.toLowerCase()))
					tempMap.put(name.toLowerCase(), eType);

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

		launchCommand.setDefault(PermissionDefault.OP);
        randomLaunch.setDefault(PermissionDefault.OP);
        allMobLaunch.setDefault(PermissionDefault.OP);
        allLaunchers.setDefault(PermissionDefault.OP);

        launchCommand.addParent(randomLaunch, true);
        launchCommand.addParent(allMobLaunch, true);
        launchCommand.addParent(allLaunchers, true);
        randomLaunch.addParent(allLaunchers, true);
        allMobLaunch.addParent(allLaunchers, true);

        pm.addPermission(launchCommand);
        pm.addPermission(randomLaunch);
        pm.addPermission(allMobLaunch);
        pm.addPermission(allLaunchers);
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("launchmob")) {
			if (!sender.hasPermission("mvpgadgets.launchmob")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to launch mobs.");
				return true;
			}

			if (!(sender instanceof Player)) {
                sender.sendMessage("Nope. You're a console.");
                return true;
            }

			if (args.length < 1)
				launchMob("", (Player) sender, usedNames);

            else
				launchMob(args[0], (Player) sender, usedNames);
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

				Creature creature = null;
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
				
				//TGRHavoc: Set the heal to the maximum it can so that the
				//Entity won't die and drop items (Issue #7 https://github.com/JOSHSLAYDE/MVPGadgets/issues/7)
				
				Damageable entityD = (Damageable) entity;
				entityD.setMaxHealth(Double.MAX_VALUE);
				entityD.setHealth(Double.MAX_VALUE);
				
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
			} else
				p.sendMessage(ChatColor.RED + "Mob name '" + mobName + "' is invalid.");
		} else {
			if (!p.hasPermission("mvpgadgets.launchmob.random")) {
				p.sendMessage(ChatColor.RED + "You must specify a creature to launch.");
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
                if(!p.hasPermission("mvpgadgets.launchmob." + getNameFrom(it.next().toString(), map)))
                    it.remove();

            if(tempEList.isEmpty()) {
                p.sendMessage(ChatColor.RED + "You do not have permission for any mobs.");
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

                List<Ocelot.Type> catTypes = Arrays.asList(Ocelot.Type.values());
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
			entityD.setHealth(Double.MAX_VALUE);

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
		return map.containsKey(name.toLowerCase());
	}

	public String getNameFrom(String name, Map<String, EntityType> map) {
		if (inputNameValid(name, map))
			return map.get(name.toLowerCase()).toString().toLowerCase();

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
