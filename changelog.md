ChangeLog - Please create an issue to log your changes.
=====

### v1.4.0
###### Date: 04-Mar-2017
###### Developer: TGRHavoc
- Fix package declaration for MobCannon
- Update PaintballListener for 1.11
- Add PaintballHandler for 1.11
- Update Disguise to 1.11

### Bukkit 1.8.x Release
###### Date: 05-Oct-2016
###### Developer: TGRHavoc
- Set the default values to "guiGadgetWorlds" to an empty array, just to allow users to use the plugin out-of-the-box. I've also added a new config option to allow the users to set whether the players can drop the gadget items or not.

- Because of the added config option I've also added a new message to tell the players they cannot drop items.

- Re-done how the gadgets are registered on the server. The main gadget class (Gadget.java) checks to see if it's already called the Gadget#registerEvent method and shouldn't call it multiple times.

### Turned this into releasable, working plugin
###### Date: 21-Jul-2016
###### Developer: pookeythekid
- Recoded the entire paintball gun so that it uses packets now. This is much better at not destroying the contents of container blocks and signs.
  - Also massively expanded PaintballGunListener's BlockData class (and made it its own separate class), though it turned out to be useless once I went to packets.

- Redesigned the gadget-adding system. Players now own their individual sets of Gadgets; each players' gadgets have their events registered **once**. Re-registering gadgets was a major bug.
  - The MVPGadgets class's _availableGadgets_ contains ownerless gadgets, for the sake of seeing what they are without having redundant listeners registered.

- Added a few settings in messages.yml.

- Made a bunch of OCD edits throughout the code and config files.

- Wrote up an HTML file in the resources folder that covers all commands and permissions. Feel free to add to it if necessary.

- Did I say I fixed a crapton of bugs?

- Added commands /mvpgadgets reload and /gadget
  - /gadget will swap your gadget out for the GUI Gadget and open the GUI display.

- Added config settings guiGadgetWorlds and guiGadgetSlot (I think the latter is correct, too lazy to check right now). Basically self-explanatory.

- Made gadgets functional after a plugin reload with players online, instead of the plugin vomiting NPEs everywhere.

### Disguises Updated
###### Date: 4-Oct-2015
###### Developer: TGRHavoc
- Changed the way disguises are implemented. Instead of being dependant on
reflection utils, disguises can now have direct access to NMS code. To
implement, just do the following:
    1. Create folder in "ovh.tgrhavoc.mvpgadgets.disguisegadget.nms" that is
the name of the version you want to implment (e.g. v1_8_R3 for 1.8.8).
    2. Add "Disguise.java" (Case-sensitive)
    3. Implement the methods, using the NMS code for you perticular version.
    4. Win

### JavaDocs been added
###### Date: 4-Apr-2015
###### Developer: TGRHavoc
- Added the JavaDocs to the gh-pages branch

### Disguises now work (Tested on 1.8 if someone could test on earlier versions that would be great!)
###### Date: 28-Mar-2015
###### Developer: TGRHavoc
- Disguises now work (Tested on 1.8) with no errors.
- Messsages.yml
  - Disguises names so server owner can customize them if they want.
  - Added messages for when player disguises, changes disguise or
removes disguise.

### A big update
###### Date: 22-Mar-2015
###### Developer: TGRHavoc
- messages.yml
  - Added "description" array to the file, allows the server owner to change the descriptions applied to gadgets.
  - PRICE: The price of the gadget.
  - PERMISSION: Permission message (As suggested by @JoshMullins)
  - HAS_PERMISSION: Allows the server owner to change what is displayed when the user does/doesn't have permission for the gadget.
-plugin.yml
  - Added "authors" to credit the developer that have contributed to the plugin (Feel free to add anyone I've missed out)
  - Added "softdepend" for vault intergration
  - Added "Gadget_Prices" so owner can change prices of gadgets.
- config.yml
  - Added "vault" to allow the server owner to enable/disable vault integration as they wish.
- MVPGadgets.java
  - Added vault hook.
  - If you want to check if Vaults is enabled/has been hooked then do MVPGadgets#hookedVault() this will return a Boolean (False if server owner has set config to false/Vault isn't installed) - I will add another check to see if it's a vault problem or if config is set to false.
  - Added methods getGadgetPrice(Gadget) and getGadgetPrice(String) to get prices of gadgets.
- Gadgets.java
  - Added a method to get the GUI item for the gadget (This has description, permission and price (If enabled) added to it).
  - Added some more string to help getting certain paths in configs.
- ReadMe.md
  - Updated to include extra features added
- .classpath
  - Added the vault as a lib
- players cannon use a gadget without permission (mvpgadgets.<gadgetName> (gadgetName is defined in the gadget class))
- Add the "purchasing" mechanism
- Change GUI to use the new itemstack instead of the old. (Now shows description, price (if any) and if player has permission)

### Update plugin.yml
###### Date: 23-Mar-2015
###### Developer: pookeythekid
- Add in MobCannon commands, different command for each feature of the mob cannon

### Added the MobCannonGadget.
###### Date: 23-Mar-2015
###### Developer: TGRHavoc
- A gadget for the mob cannon command was added.
- Painball gun gadget was added

### Mainly updated the horse gadget.
###### Date: 22-Mar-2015
###### Developer: TGRHavoc
- Player now spawn a random type of horse.
- All horses (and donkeys) have correct explosion effect.
- getPluginManager() changed to getServer().getPluginManager()
- Messages.yml now contains a bunch of nodes the server owners can change to suit their language or, if they just don't like our colours.

### Massive MobCannon Updates
###### Date: 21-Mar-2015
###### Developer: pookeythekid
- In very short, added commands and permissions, fixed bugs, and created a new config file.

### Removed unnecessary code
###### Date: 18-Mar-2015
###### Developer: bwfcwalshyPluginDev
- Removed unnecessary code

### Update MobCannon.java
###### Date: 9-Mar-2015
###### Developer: pookeythekid
- Fixed typo in blacklist check.

### Multiple updates to MobCannon.java
###### Date: 12-Feb-2015
###### Developer: AdamQpzm
- Update new lines - It's generally not conventional to have so many new lines. It's also not consistent with the other files in the project. There seems to be a new blank line after most, if not all, lines. I've removed a lot of the unnecessary blank lines, and have left some in to aid in readability.
- Update the fields to be private and not needlessly static - This is another simple one. Convention dictates the correct use of access modifiers. Since I didn't really see a reason any of the fields should be public (and certainly not public and non-final) I made them private. There was also a static field - static should only really be used when there's a good reason for it (and the main reason being immutable constants). Since a good reason for this wasn't clear, I removed it.
- Updated EntityType map in simple constructor - The way the EntityType map was set up before was using the same HashSet for every EntityType. So far as I could tell, this was meant to represent mob aliases. Using the same Object for that doesn't really make sense - if at any point one tries to add a mob alias, then the alias will be applied to every EntityType (as it uses the same Object). I also altered the system to loop over an array of EntityTypes to reduce code duplication - this also makes it easier to add an EntityType at any point - simply add the EntityType to the array.
- Added null check for constructor - It makes more sense to throw an IllegalArgumentException with a helpful message than to just include in the JavaDocs comment not to pass null values. At best, that approach would cause NPEs to be thrown and would be harder to debug for develoers. At worst, it could cause even more damage to other parts.
- Added null safety to eType - Another NPE avoiding change. Before, it would throw an NPE when performing the equals() check if any of the values in the list were actually null - now it will simply ignore null values without throwing an exception.
- Updated blacklist logic - No sense in checking the Map for disallowed values after adding them, when you could just not add them in the first place, eh?
- Updated style and logic - The style was a little inconsistent - for example, sometimes missing the braces for single statement ifs, and sometimes not. I updated it to be more consistent. I also updated the logic to be a bit more logical - for example, there's no reason to check if the args length is greater than 0 in the else after checking if it's less than 1 - if wasn't less than 1, then it has to be greater than 0! If it was less than 1, the greater-than-0 statement would run (due to the else).
- Created Random field - There's no real reason to create a new Random object every time. Doing so is wasteful, and apparently results in 'less random' results too.
- Removed potential infinite runtime - There were a couple of things like this. For example, one place used a while loop to check whether someone had a permission. If they didn't, it'd take a random entity type and see if they had that permission, and so on until it found one the player did have permission for. This will freeze the server until it finds one they do have permission for - and since it's technically possible, however unlikely, that the same random number is generated lots of times, there's no upper limit on how long this operation can take. If they happen to have the random permission, but not permission for any of the mobs, this operation will definitely take forever. Oops!


### Deleted unnecessary files
###### Date: 12-Feb-2015
###### Developer: AdamQpzm
- .classpath, .project and .settings/ were removed.

### Update MobCannon.java
###### Date: 11-Jan-2015
###### Developer: pookeythekid
- MobCannon.java was updated so that ALL entities fired would have full max health and wouldn't die uppon impact with ground

### Set the entitie's heal to the maximum value
###### Date: 3-Jan-2015
###### Developer: TGRHavoc
- MobCannon.java was updated so that selected entities fired would have full max health and wouldn't die uppon impact with ground

### Package cleanup and other
###### Date: 29-Dec-2014
###### Developer: TGRHavoc
- All classes were moved to their respected folders
- Fixed some errors in the code (e.g. player.getName() in HorseListener)
- JarUtil initialises (and adds) Gadgets that are in the "gadgets" package
- Removed the use of "JavaUtil" (Was just for demonstraive purposes)

### Update MVPGadgets.java
###### Date: 29-Dec-2014
###### Developer: pookeythekid
- Fixed a potential index out of bounds on line 77

### Added way to dynamicaly load Gadgets
###### Date: 29-Dec-2014
###### Developer: TheFreakLord
- Classes that were put into a "mods" folder would have been loaded at runtime.

### Added Mob Cannon Feature
###### Date: 2-Dec-2014
###### Developer: pookeythekid
- New command created (/launchmob (/launch, /lmob, /lm) [mob name (optional)])
- Darkblade12's particle effect library was added for explosion effects
- Permissions added to the commands
  - Base command permission: mvpgadgets.launchmob
  - All command permissions: mvpgadgets.launchmob.*
  - All per-mob launch permission: mvpgadgets.launchmob.all
  - Per-mob launch permssions: mvpgadgets.launchmob.mob_name (all lower case)
  - Random mob launch permission: mvpgadgets.launchmob.random

### Update HorseListener.java
###### Date: 2-Dec-2014
###### Developer: 97WaterPolo
- HorseListener was updated so that players could only ride their own horses.

### Let's get the dice rolling
###### Date: 30-Nov-2014
###### Developer: TGRHavoc
- Project was born
  - Plugin.yml was created with the basic information
  - MVPGadgets.java was created with minimal functionality in order to work
  - GadgetEvent was created to execute events that involve a gadget (Right click)
  - GadgetHandler created to handle event above, calles the execute method in the Gadget class.
  - Abstract gadget class was created
  - GUIGadget was created (Allows players to select a gadget)
  - Horse gadget was created
