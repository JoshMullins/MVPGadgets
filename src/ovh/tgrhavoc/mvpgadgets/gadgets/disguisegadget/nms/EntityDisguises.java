package ovh.tgrhavoc.mvpgadgets.gadgets.disguisegadget.nms;

import org.bukkit.Bukkit;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

public enum EntityDisguises {
	ZOMBIE("EntityZombie"),
	WITHER_SKELETON("EntitySkeleton"),
	SKELETON("EntitySkeleton"),
	ZOMBIE_PIG("EntityPigZombie"),
	BLAZE("EntityBlaze"),
	ENDERMAN("EntityEnderman"),
	CREEPER("EntityCreeper"),
	SPIDER("EntitySpider"),
	WITCH("EntityWitch"),
	WITHER_BOSS("EntityWither"),
	GHAST("EntityGhast"),
	GIANT("EntityGiantZombie"), 
	SLIME("EntitySlime"), 
	CAVE_SPIDER("EntityCaveSpider"),
	SILVERFISH("EntitySilverfish"),
	MAGMA_CUBE("EntityMagmaCube"),
	BAT("EntityBat"), PIG("EntityPig"),
	SHEEP("EntitySheep"),
	COW("EntityCow"),
	CHICKEN("EntityChicken"),
	SQUID("EntitySquid"),
	WOLF("EntityWolf"),
	OCELOT("EntityOcelot"),
	HORSE("EntityHorse"),
	VILLAGER("EntityVillager"),
	IRON_GOLEM("EntityIronGolem"),
	SNOWMAN("EntitySnowman"),
	ENDER_DRAGON("EntityEnderDragon"),
	MOOSHROOM("EntityMushroomCow"),
	
	// 1.9 Creatures
	ENDERMITE("EntityEndermite"),
	GUARDIAN("EntityGuardian"),
	RABBIT("EntityRabbit"),
	
	// 1.10
	POLAR_BEAR("EntityPolarBear");
	
	
	private final String cls;
	
	EntityDisguises(String cls) {
		this.cls = cls;
	}
	
	public String getName(MVPGadgets plugin){
		return plugin.getMessageFromConfig("Messages.Disguises.Names." + this.name());
	}
	
	public static EntityDisguises getByName(String name){
		for (EntityDisguises d: EntityDisguises.values()){
			if(d.name().equalsIgnoreCase(name))
				return d;
		}
		return null;
	}
	
	public static EntityDisguises getByMessageName(MVPGadgets plugin, String messageName) {
		for(EntityDisguises d: EntityDisguises.values()){
			if (d.getName(plugin).equals(messageName))
				return d;
		}
		
		return null;
	}

	public String getClassName() {
		return "net.minecraft.server."
				+ Bukkit.getServer().getClass().getPackage().getName()
						.substring(23) + "." + cls;
	}

}
