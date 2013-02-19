package edu.nmt.minecraft.HomeWorldPlugin.economy;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import edu.nmt.minecraft.HomeWorldPlugin.HomeWorldPlugin;

class BalanceComparator implements Comparator<Account>{
	   
    public int compare(Account account1, Account account2){
   
        /*
         * parameter are of type Object, so we have to downcast it
         * to Employee objects
         */
       
        double emp1Balance = account1.getBalance();        
        double emp2Balance = account2.getBalance();
       
        if(emp1Balance > emp2Balance)
            return 1;
        else if(emp1Balance < emp2Balance)
            return -1;
        else
            return 0;    
    }
   
}

/**
 * Handles all economy related commands, etc
 * @author Matthew
 */
public class EconomyManager {
	
	/**
	 * List of all currently loaded currencies
	 */
	private List<Currency> currencies;
	
	/**
	 * List of all currently loaded accounts
	 */
	private LinkedList<InventoryAccount> accounts;
	
	/**
	 * starting balance for new accounts
	 */
	public static double startingBalance = 50.0;
	
	/**
	 * Default Constructor 
	 */
	public EconomyManager(){
		//create a list for currencies
		currencies = new LinkedList<Currency>();
		
		//load accounts from file
		load();
	}
	
	/**
	 * Save economy data to file
	 */
	public void save(){
		try {
			HomeWorldPlugin.fileManager.save(accounts, "accounts");
		} catch (IOException e) {
			System.err.println("[HomeWorldPlugin-Economy] Error Saving File");
			e.printStackTrace();
		}
	}

	/**
	 * load economy data from file
	 */
	@SuppressWarnings("unchecked")
	public void load(){
		try {
			accounts = (LinkedList<InventoryAccount>) HomeWorldPlugin.fileManager.load("accounts", false);
		} catch (IOException e) {
			System.err.println("[HomeWorldPlugin-Economy] Error Loading File");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a currency to the list of valid currencies
	 * @param newCurrency the new Currency type to be added
	 */
	public void addCurrency(Currency newCurrency){
		currencies.add(newCurrency);
	}
	
	public Currency getCurrency(String currencyName){
		for (Currency c: currencies){
			if (c.getName().equals(currencyName)){
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Returns the list of currencies
	 * @return the list of currencies
	 */
	public List<Currency> getCurrencies(){
		return currencies;
	}
	
	/**
	 * Checks if the given currency is in the list of valid currencies
	 * @param currency
	 * @return true if currency is a valid currency, false if not
	 */
	public boolean containsCurrency(Currency currency){
		if (currencies.contains(currency)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Checks if the given currency is in the list of valid currencies
	 * @param currency
	 * @return true if currency is a valid currency, false if not
	 */
	public boolean containsCurrency(String currencyName){
		for (Currency c: currencies){
			if (c.getName().equals(currencyName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the dollar value of a given currency
	 * @param currencyName Name of the currency
	 * @return -1 if invalid, else dollar value
	 */
	public int getCurrencyValue(String currencyName){
		for (Currency c: currencies){
			if (c.getName().equals(currencyName)){
				return c.getValue(1);
			}
		}
		return -1;
	}
	
	/**
	 * gets the Material of a given currency 
	 * @param currencyName Name of the currency
	 * @return null if invalid, else Material of currency
	 */
	public Material getCurrencyMaterial(String currencyName){
		for (Currency c: currencies){
			if (c.getName().equals(currencyName)){
				return c.getMaterial();
			}
		}
		return null;
	}
	
	/**
	 * Checks the value of a given amount of a given currency
	 * @param sender The player executing the command
	 * @param name The name of the currency
	 * @param amount The amount specified
	 */
	public void checkValue(CommandSender sender, String name, int amount){
		if (!(containsCurrency(name))){
			sender.sendMessage("That is not a valid currency");
			return;
		}
		String message = amount + " " + name + " is worth $" + (amount * getCurrencyValue(name));
		sender.sendMessage(message);
	}
	
	/**
	 * Checks if an itemstack is a specified currency
	 * @param stack Itemstack
	 * @param currencyName Currency name
	 * @return true if so, false if not
	 */
	public boolean isCurrency(ItemStack stack, String currencyName){
		if (stack.getType().equals(getCurrencyMaterial(currencyName))){
			if (stack.getData().getData() == getCurrency(currencyName).getMaterialData()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Allows a player to withdraw an amount of physical currency into his or her inventory, given he or she has the needed funds
	 * @param sender The player executing the command
	 * @param currency The name of the physical currency specified
	 * @param amount The amount of physical currency to be withdrawn
	 */
	public void withdraw(CommandSender sender, String currency, int amount) {
		
		//make sure the amount specified was a positive number
		if (amount <= 0){
			sender.sendMessage("Please enter a positive amount");
			return;
		}
		
		//make sure its a player
		if (!(sender instanceof Player)){
			sender.sendMessage("Sorry, only players can execute this command");
			return;
		}
		
		//make sure the player is in the right world
		if (!(Bukkit.getWorld("HomeWorld").getPlayers().contains(sender))){
			sender.sendMessage("Sorry, you have to be on the HomeWorld to withdraw");
			return;
		}
		
		//make sure player has enough funds
		if (java.lang.Math.floor(getAccount(sender.getName()).getBalance() / getCurrencyValue(currency)) < amount){
			sender.sendMessage("Sorry, you dont have enough money");
			return;
		}
		
		//get the players inventory
		Inventory playerInventory = ((Player) sender).getInventory();
		
		//check inventory for empty spots
		int i = 0;
		for (ItemStack stack : playerInventory.getContents()){
			if (stack != null){
				if (isCurrency(stack, currency)){
					i += (64 - stack.getAmount());
				}
			}
			else{
				i += 64;
			}
		}
		
		//make sure enough was found
		if (i < amount){
			sender.sendMessage("Sorry, there is not enough space in your inventory.");
			return;
		}
		
		//add items
		i = amount;
		int j = 0;
		for (ItemStack stack : playerInventory.getContents()){
			if (stack != null){
				if (isCurrency(stack, currency)){
					//finish off this stack
					if (i + stack.getAmount() <= 64){
						playerInventory.setItem(j, null);
						playerInventory.addItem(new ItemStack(stack.getType(), stack.getAmount() + i, (short) 0, getCurrency(currency).getMaterialData()));
						i = 0;
					}
					//add the whole stack and keep going
					else{
						i -= (64 - stack.getAmount());
						playerInventory.setItem(j, null);
						playerInventory.addItem(new ItemStack(stack.getType(), 64, (short) 0, getCurrency(currency).getMaterialData()));
					}
				}
			}
			else{
				//finish off this stack
				if (i <= 64){
					playerInventory.addItem(new ItemStack(getCurrencyMaterial(currency), i, (short) 0, getCurrency(currency).getMaterialData()));
					i = 0;
				}
				//add the whole stack and keep going
				else{
					i -= 64;
					playerInventory.addItem(new ItemStack(getCurrencyMaterial(currency), 64, (short) 0, getCurrency(currency).getMaterialData()));
				}
			}
			
			if (i == 0){
				//remove funds
				getAccount(sender.getName()).withdraw(amount * getCurrencyValue(currency));
				sender.sendMessage(amount + " " + currency + " was withdrawn.");
				return;
			}
			j++;
		}
		
		//hopefully, this statement never runs
		sender.sendMessage("something bad happened");
	}

	/**
	 * Returns a Players Account
	 * @param name Name of the player
	 * @return Player's account
	 */
	public Account getAccount(String name) {
		for (Account a: accounts){
			if (a.getOwner().equals(name)){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a player has an account
	 * @param name Name of the player
	 * @return true if player has an account, false if not
	 */
	public boolean hasAccount(String name){
		for (Account a: accounts){
			if (a.getOwner().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds an account
	 * @param account Account to be added
	 */
	public void addAccount(InventoryAccount account){
		accounts.add(account);
	}

	/**
	 * Allows a player to deposit an amount of physical currency into his or her account, given he or she has the currency
	 * @param sender The player executing the command
	 * @param currency The name of the physical currency specified
	 * @param amount The amount of physical currency to be deposited
	 */
	public void deposit(CommandSender sender, String currency, int amount){
		
		//make sure amount specified was a positive number
		if (amount <= 0){
			sender.sendMessage("Please enter a positive amount");
			return;
		}
		
		//make sure its a player
		if (!(sender instanceof Player)){
			sender.sendMessage("Sorry, only players can execute this command");
			return;
		}
		
		//make sure the player is in the right world
		if (!(Bukkit.getWorld("HomeWorld").getPlayers().contains(sender))){
			sender.sendMessage("Sorry, you have to be on the HomeWorld to deposit items");
			return;
		}
		
		//get the players inventory
		Inventory playerInventory = ((Player) sender).getInventory();
		
		//check inventory
		int i = 0;
		for (ItemStack stack : playerInventory.getContents()){
			if (stack != null){
				if (isCurrency(stack, currency)){
					i += stack.getAmount();
				}
			}
		}
		
		//make sure enough was found
		if (i < amount){
			sender.sendMessage("Sorry, you dont have enough " + currency);
			return;
		}
		
		//remove items
		i = amount;
		int j = 0;
		for (ItemStack stack : playerInventory.getContents()){
			if (stack != null){
				if (isCurrency(stack, currency)){
					//finish off this stack
					if (i < stack.getAmount()){
						
						stack.setAmount(stack.getAmount() - i);
						i = 0;
						playerInventory.setItem(j, new ItemStack(stack.getType(), stack.getAmount(), (short) 0, getCurrency(currency).getMaterialData()));
						
					}
					//remove the whole stack and keep going
					else{
						i -= stack.getAmount();
						playerInventory.setItem(j, null);
					}
					
					if (i == 0){
						//add funds
						getAccount(sender.getName()).deposit(amount * getCurrencyValue(currency));
						sender.sendMessage(amount + " " + currency + " was deposited.");
						return;
					}
				}
			}
			j++;
		}
		
		//hopefully, this statement never executes
		sender.sendMessage("something bad happened");
		
	}
	
	/**
	 * Calculates the sum of all physical currencies in the player's inventory
	 * @param sender The player executing the command
	 */
	public void calculateWealth(CommandSender sender){
		
		//make sure its a player
		if (!(sender instanceof Player)){
			sender.sendMessage("Sorry, only players can execute this command");
			return;
		}
		
		//get the players inventory
		Inventory playerInventory = ((Player) sender).getInventory();
		
		//calculate wealth
		int wealth = 0;
		for (ItemStack stack : playerInventory.getContents()){
			if (stack != null){
				for (Currency c: currencies){
					if (isCurrency(stack, c.getName())){
						wealth += c.getValue(stack.getAmount());
					}
				}
			}
		}
		
		//tell the player
		sender.sendMessage("You are carrying $" + wealth + " worth in materials");
	}

	/**
	 * Shows a player his or her current balance
	 * @param sender The player executing the command
	 */
	public void showBalance(CommandSender sender) {
		if (!(hasAccount(sender.getName()))){
			sender.sendMessage("Error: for some reason you do not have an account");
			return;
		}
		sender.sendMessage("Your current balance is: $" + getAccount(sender.getName()).getBalance());
	}

	/**
	 * Gets the balance of a given player
	 * @param name Name of the player
	 * @return player's balance in dollars
	 */
	public double getBalance(String name) {
		if (!(hasAccount(name))){
			return -1;
		}
		return getAccount(name).getBalance();
	}

	/**
	 * Removes a given amount of money from a player's account
	 * @param name Name of the player
	 * @param amount amount to be removed
	 */
	public void withdraw(String name, double amount) {
		if (!(hasAccount(name))){
			return;
		}
		Account account = getAccount(name);
		account.withdraw(amount);
	}
	
	/**
	 * Adds a given amount of money to a player's account
	 * @param name Name of the player
	 * @param amount amount to be added
	 */
	public void deposit(String name, double amount) {
		if (!(hasAccount(name))){
			return;
		}
		Account account = getAccount(name);
		account.deposit(amount);
	}

	/**
	 * Sets a players balance. Can only be executed by the server
	 * @param server Server
	 * @param player Player to set balance
	 * @param amount amount to be set
	 */
	public void setBalance(CommandSender server, String player, double amount) {

		//make sure its the server
		if (server instanceof Player){
			server.sendMessage("Sorry, only the server can execute this command");
			return;
		}
		
		//check if player has an account
		for (InventoryAccount a: accounts){
			if (a.getOwner().equals(player)){
				//set and return
				a.setBalance(amount);
				server.sendMessage("Account was set");
				return;
			}
		}
		
		//create account since player doesn't already have one
		InventoryAccount account = new InventoryAccount(player, amount);
		
		//add to accounts
		accounts.add(account);
		
		server.sendMessage("Account was created an set");
	}

	public void pay(CommandSender sender, String receiver, double amount) {

		if (!hasAccount(sender.getName())){
			sender.sendMessage("I'm sorry, but you do not have an account");
			return;
		}
		
		if (!hasAccount(receiver)){
			sender.sendMessage("I'm sorry, but " + receiver + " does not have an account");
			return;
		}
		
		//make sure sender has enough money
		Account sendersAccount = getAccount(sender.getName());
		if (sendersAccount.getBalance() < amount){
			sender.sendMessage("I'm sorry, but you do not have that much money in your account");
			return;
		}
		
		//take money from the sender
		sendersAccount.withdraw(amount);
		
		//pay the player
		Account receiverAccount = getAccount(receiver);
		receiverAccount.deposit(amount);
		
		//notify both players
		sender.sendMessage("You have payed " + receiver + " $" + amount + " dollars");
		Bukkit.getPlayer(receiver).sendMessage("" + sender.getName() + " has payed you $" + amount + " dollars");
		
	}

	public void top(CommandSender sender, int number) {

		sender.sendMessage("here are the top " + number + " accounts on the server:");
		
		java.util.Collections.sort(accounts, new BalanceComparator());
		java.util.Collections.reverse(accounts);
		
		int i = 0;
		for (InventoryAccount a: accounts){
			sender.sendMessage(a.getOwner() + ": " + a.getBalance());
			i++;
			if (i == number){
				return;
			}
		}
		
	}
	
}
