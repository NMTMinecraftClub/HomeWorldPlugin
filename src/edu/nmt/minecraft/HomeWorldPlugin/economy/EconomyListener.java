package edu.nmt.minecraft.HomeWorldPlugin.economy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import edu.nmt.minecraft.HomeWorldPlugin.HomeWorldPlugin;

/**
 * Listener for economy related events
 * @author Matthew
 *
 */
public class EconomyListener implements Listener{
	
	/**
	 * hook to grab the economy
	 */
	private EconomyManager economy;
	
	/**
	 * Default constructor
	 */
	public EconomyListener(){
		
		//try to grab the EconomyManager
		economy = HomeWorldPlugin.economy;
		
	}
	
	/**
	 * Creates accounts for users when they log in, if they don't already have an account
	 * @param event PlayerLoginEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
    public void createAccount(PlayerLoginEvent event) {
		
		//try to grab the economy. I know
		while (economy == null){
			economy = HomeWorldPlugin.economy;
		}
		
		//If player does not have an account, create one
		if (!(economy.hasAccount(event.getPlayer().getName()))){
			economy.addAccount(new InventoryAccount(event.getPlayer().getName(), EconomyManager.startingBalance));
			event.getPlayer().sendMessage("Your new account has been made");
			System.out.println("[HomeWorldPlugin-Economy] made new account for " + event.getPlayer().getName());
		}
	}
}
