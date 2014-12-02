package ovh.tgrhavoc.mvpgadgets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ovh.tgrhavoc.mvpgadgets.gadgets.Gadget;


public class GadgetEvent extends Event implements Cancellable{
	
	private static HandlerList handlers = new HandlerList();
	
	private boolean cancelled;
	
	Gadget myGadget;
	Player myPlayer;
	
	public GadgetEvent(Gadget gadget, Player player) {
		myGadget = gadget;
		myPlayer = player;
	}
	
	public Player getPlayer(){
		return myPlayer;
	}
	
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
