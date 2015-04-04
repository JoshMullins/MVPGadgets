package ovh.tgrhavoc.mvpgadgets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;

/**
 * GadgetEvent is called every time someone right clicks with a gadget
 * @author Jordan Dalton
 *
 */
public class GadgetEvent extends Event implements Cancellable{
	
	private static HandlerList handlers = new HandlerList();
	
	private boolean cancelled;
	
	Gadget myGadget;
	Player myPlayer;
	
	public GadgetEvent(Gadget gadget, Player player) {
		myGadget = gadget;
		myPlayer = player;
	}
	
	/**
	 * Get the player who triggered the event
	 * @return Player who triggered the event
	 */
	public Player getPlayer(){
		return myPlayer;
	}
	
	/**
	 * Get the gadget that the player right clicked.
	 * @return Gadget the player used.
	 */
	public Gadget getGadget(){
		return myGadget;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean c) {
		cancelled = c;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
