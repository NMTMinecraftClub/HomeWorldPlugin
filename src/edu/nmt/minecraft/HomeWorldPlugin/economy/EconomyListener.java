package edu.nmt.minecraft.HomeWorldPlugin.economy;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
	
	/*
	 * Removes a percentage (40-70) of currency from a player's enderchest upon death in the wilderness
	 * @param event PlayerDeathEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void wildernessDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(player.getLocation().getWorld().getName().equals("Wilderness")) {
			Random random = new Random();
			int percentLost = random.nextInt(40) + 30;
			int inventoryAmount = 0;
			int valueLost = 0;

			// Iterates through arraylist depositing items
		 	for(Currency currency: economy.getCurrencies()){
		 		inventoryAmount = economy.countInventory(player.getEnderChest(), currency);
				economy.depositCurrency(player.getEnderChest(), currency, (int)Math.ceil(inventoryAmount * percentLost / 100.));
				valueLost += economy.getCurrencyValue(currency.getName()) * (int)Math.ceil(inventoryAmount * percentLost / 100.);
		 	}

			player.sendMessage("You died in the wilderness");
			player.sendMessage("$" + valueLost + " was lost from your EnderChest (" + percentLost + "% of currencies)");
		}
	}
}
