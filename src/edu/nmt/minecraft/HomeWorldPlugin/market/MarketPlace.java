package edu.nmt.minecraft.HomeWorldPlugin.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import edu.nmt.minecraft.HomeWorldPlugin.ConfigManager;
import edu.nmt.minecraft.HomeWorldPlugin.HomeWorldPlugin;


/**
 * Represents a Marketplace, for buying and selling items
 * 
 * @author Matthew
 * 
 */
public class MarketPlace implements Listener {
	
	/**
	 * The List of items that are currently up for sale on the market
	 */
	private ArrayList<MarketItemStack> market;
	
	private HashMap<String, MarketPlayerInfo> playerInfo;

	private HashMap<String, MarketItemStack> toSell;
	private HashMap<String, MarketItemStack> toBuy;
	
	private ConfigManager configuration;
	
	private ProtectedRegion region;

	/**
	 * Default Constructor
	 */
	public MarketPlace(HomeWorldPlugin plugin, String name, ProtectedRegion region) {
		// change these to load from file
		market = new ArrayList<MarketItemStack>();
		playerInfo = new HashMap<String, MarketPlayerInfo>();
		toSell = new HashMap<String, MarketItemStack>();
		toBuy = new HashMap<String, MarketItemStack>();
		configuration = new ConfigManager(plugin, name + ".yml");
		this.region = region;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		load();
		
		
	}

	/**
	 * Event that is triggered when the player opens a marketplace chest Dresses
	 * the chest.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void marketStart(PlayerInteractEvent event) {

		Player player = (Player) event.getPlayer();

		// make sure player is at market
		if (!atMarket(event)) {
			return;
		}
		
		//make sure player is not in creative mode
		if (player.getGameMode() == org.bukkit.GameMode.CREATIVE){
			player.sendMessage("Sorry, creative players can not use a market.");
			return;
		}

		// create first time data if needed
		if (!playerInfo.containsKey(player.getName())) {
			playerInfo.put(player.getName(), new MarketPlayerInfo(player));
		}

		// paint the chest for the main menu
		// clear inventory
		Inventory inv = playerInfo.get(player.getName()).getInventory();


		MenuPainter.paintMainMenu(inv, 0, market);

		player.openInventory(playerInfo.get(player.getName()).getInventory());

	}

	/**
	 * Event triggered when an item is clicked in the chest
	 * 
	 * @param event
	 */
	@EventHandler(ignoreCancelled=true)
	public void itemClicked(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		// don't do anything if the player isn't in the marketplace
		if (! atMarket(player, event.getInventory())) {
			return;
		}		
		
		//make sure there is an economy
		if (HomeWorldPlugin.economy == null){
			if (!HomeWorldPlugin.setupEconomy()){
				player.sendMessage("Error. No economy plugin loaded.");
				return;
			}
		}

		//don't allow any right clicking
		if (event.isRightClick()){
			event.setResult(org.bukkit.event.Event.Result.DENY);
	        event.setCancelled(true);
	        return;
		}
		
		// make sure we are in bounds
		if (event.getSlot() < 0) {
			return;
		}

		// choose the right menu
		// main menu
		if (playerInfo.get(player.getName()).getMenu() == 0) {
			player.sendMessage("Main menu");
			mainMenu(event);
		}
		// buy menu
		else if (playerInfo.get(player.getName()).getMenu() == 1) {
			player.sendMessage("Buy menu");
			buyMenu(event);
		}
		// sell menu
		else if (playerInfo.get(player.getName()).getMenu() == 2) {
			player.sendMessage("Sell menu");
			sellMenu(event);
		}

		event.setResult(org.bukkit.event.Event.Result.DENY); // Dunno if this was needed
        event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void marketClosed(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		
		if (atMarket(player, event.getInventory())) {
			playerInfo.remove(player.getName());
			if (toSell.containsKey(player.getName())){
				toSell.remove(player.getName());
			}
		}
	}

	/**
	 * The sell menu. Called when a player wants to buy an item
	 * 
	 * @param event
	 *            the InventoryClickEvent
	 * @param
	 */
	private void buyMenu(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();

		// click was made in the player's inventory
		if (event.getRawSlot() > event.getSlot()) {
			return;
		}

		// item being bought was clicked
		if (event.getSlot() == 0) {
			// do nothing
			return;
		}

		// buy button was pressed
		if (event.getSlot() == MenuPainter.getRight(inv, 1)) {
			player.sendMessage("You pressed the buy button");
			
			buyItem(player, toBuy.get(player.getName()), event);

			// paint the chest for the main menu
			MenuPainter.paintMainMenu(event.getInventory(), 0, market);
			playerInfo.get(player.getName()).setIndex(0);
			playerInfo.get(player.getName()).setMenu(0);
			
			//update the menu of anyone who was viewing the new change
			updateAllMainMenus();
			return;

		}

		// player pressed the back button
		else if (event.getSlot() == MenuPainter.getRight(inv, 0)) {
			player.sendMessage("You pressed the back button");

			// paint the chest for the main menu
			MenuPainter.paintMainMenu(event.getInventory(), 0, market);
			playerInfo.get(player.getName()).setIndex(0);
			playerInfo.get(player.getName()).setMenu(0);
			return;
		}
	}

	/**
	 * The sell menu. Called when a player wants to sell an item
	 * 
	 * @param event
	 *            the InventoryClickEvent
	 */
	private void sellMenu(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();

		// create a market item is one does not exist yet
		if (!toSell.containsKey(player.getName())) {
			toSell.put(player.getName(), new MarketItemStack(new ItemStack(Material.AIR), player.getName(), 0, true, false));
		}

		// click was made in the player's inventory
		if (event.getRawSlot() > event.getSlot()) {

			player.sendMessage("You want to put that up for sale");
			player.sendMessage("$" + toSell.get(player.getName()).getPrice());

			// put the item up for sale
			MarketItemStack oldItem = toSell.get(player.getName());
			MarketItemStack newItem = new MarketItemStack(event.getCurrentItem(), player.getName(), oldItem.getPrice(), true, false);
			toSell.remove(oldItem);
			toSell.put(player.getName(), newItem);

			// repaint
			inv.setItem(0, newItem);

			return;
		}

		// if click was to change a place in the value
		MarketItemStack marketItem = toSell.get(player.getName());
		for (int i = 0; i < 9; i++) {
			
			//decrement
			if (event.getSlot() == MenuPainter.getRight(inv, (18 + i))) {

				// make sure the value is still positive
				if (marketItem.getPrice() - java.lang.Math.pow(10, i) < 0) {
					marketItem.setPrice(0);
					MenuPainter.paintSellMenu(event, toSell);
					return;
				}
				
				//set price
				marketItem.setPrice(marketItem.getPrice() - java.lang.Math.pow(10, i));

				// repaint
				MenuPainter.paintSellMenu(event, toSell);
				return;
			}
			
			//increment
			if (event.getSlot() == MenuPainter.getRight(inv, (27 + i))) {
				
				//make sure the price isn't over bounds
				if (marketItem.getPrice() + java.lang.Math.pow(10, i) > 999999999) {
					marketItem.setPrice(999999999);
					MenuPainter.paintSellMenu(event, toSell);
					return;
				}
				
				//set price
				marketItem.setPrice(marketItem.getPrice() + java.lang.Math.pow(10, i));

				MenuPainter.paintSellMenu(event, toSell);
				return;
			}
		}

		// sell button was pressed
		if (event.getSlot() == MenuPainter.getRight(inv, 1)) {
			player.sendMessage("You pressed the sell button");

			// make sure there is an item to sell
			if (toSell.get(player.getName()).getType().equals(Material.AIR)) {
				player.sendMessage("You haven't put anything up for sale yet.");
				return;
			}

			// remove the item from the player's inventory
			ItemStack item = toSell.get(player.getName()).toItemStack();
			
			
			player.getInventory().setItem(player.getInventory().first(item), null);

			// place the item on the market
			market.add(toSell.get(player.getName()));
			toSell.remove(player.getName());

			// paint the chest for the main menu
			MenuPainter.paintMainMenu(event.getInventory(), 0, market);
			playerInfo.get(player.getName()).setIndex(0);
			playerInfo.get(player.getName()).setMenu(0);
			
			//update the menu of anyone who was viewing the new change
			updateAllMainMenus();
			return;
		}

		// player pressed the back button
		else if (event.getSlot() == MenuPainter.getRight(inv, 0)) {
			player.sendMessage("You pressed the back button");
			
			//remove the toSell
			toSell.remove(player.getName());

			// paint the chest for the main menu
			MenuPainter.paintMainMenu(event.getInventory(), 0, market);
			playerInfo.get(player.getName()).setIndex(0);
			playerInfo.get(player.getName()).setMenu(0);
			return;
		}
	}
	
	private void updateAllMainMenus(){
		for (MarketPlayerInfo info: playerInfo.values()){
			if (info.getMenu() == 0){
				MenuPainter.paintMainMenu(info.getInventory(), info.getIndex(), market);
			}
		}
	}

	/**
	 * The main menu. Called when an item was clicked in the market
	 * 
	 * @param event
	 *            the InventoryClickEvent
	 * @param item
	 */
	public void mainMenu(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();

		// click was made in the player's inventory
		if (event.getRawSlot() > event.getSlot()) {
			return;
		}

		// if previous button was pressed
		if (event.getSlot() == MenuPainter.getLeft(inv, 0)) {
			int index = playerInfo.get(player.getName()).getIndex();

			// make sure we are not out of bounds
			if (index == 0) {
				return;
			}

			// redraw the chest
			MenuPainter.paintMainMenu(event.getInventory(), index - 1, market);

			// reajust the index for the player
			playerInfo.get(player.getName()).setIndex(index - 1);
			return;
		}
		// if next button was pressed
		if (event.getSlot() == MenuPainter.getRight(inv, 0)) {
			int index = playerInfo.get(player.getName()).getIndex();

			// redraw the chest
			MenuPainter.paintMainMenu(event.getInventory(), index + 1, market);

			// reajust the index for the player
			playerInfo.get(player.getName()).setIndex(index + 1);
			return;
		}

		// if sell item button was pressed
		if (event.getSlot() == MenuPainter.getRight(inv, 1)) {
			// set chest up for item menu
			player.sendMessage("You clicked the sell button");

			// paint the sell menu
			MenuPainter.paintSellMenu(event, toSell);

			// set the player's menu
			playerInfo.get(player.getName()).setMenu(2);

			return;
		}

		// if another item was pressed that wasn't air
		if (!event.getCurrentItem().getType().equals(Material.AIR)) {
			//set the item being evaluated
			toBuy.put(player.getName(), market.get(playerInfo.get(player.getName()).getIndex() * 45 + event.getSlot()));
			
			// paint the buy item
			MenuPainter.paintBuyMenu(event, toBuy);

			// set the player's menu
			playerInfo.get(player.getName()).setMenu(1);
			

			player.sendMessage("You want to buy that item");
			return;
		}

		player.sendMessage("You clicked air");
	}

	/**
	 * Player buys an item. Item is taken off of the market
	 * 
	 * @param player
	 *            the player buying the item
	 * @param item
	 *            the item to be bought
	 * @param inv
	 *            the inventory of the market (to be repainted)
	 */
	private void buyItem(Player player, MarketItemStack marketItem,
			InventoryClickEvent event) {

		// make sure the item wasn't quickly bought buy someone else
		if (marketItem == null) {
			player.sendMessage("I'm sorry, but this item has already been sold");
			return;
		}
		
		// make sure the item wasn't quickly bought buy someone else
		if (marketItem.hasSold()) {
			player.sendMessage("I'm sorry, but this item has already been sold");
			return;
		}

		// if the player has room and can afford the item, buy it
		if (!playerHasSpace(player)) {
			player.sendMessage("I'm sorry, but you don't have enough space.");
			return;
		}

		if ((!playerCanAfford(player, marketItem))
				&& (!player.getName().equals(marketItem.getSeller()))) {
			player.sendMessage("You can't afford that.");
			return;
		}

		// buy the item
		
		marketItem.setSold(true);
		player.getInventory().addItem(marketItem);

		HomeWorldPlugin.economy.depositPlayer(marketItem.getSeller(), marketItem.getPrice());
		Player seller = Bukkit.getPlayer(marketItem.getSeller());
				
		if (seller != null){
			seller.sendMessage(player.getName() + " just bought your " + marketItem.toString()
					+ " for $" + marketItem.getPrice());
		}
				

		HomeWorldPlugin.economy.withdrawPlayer(player.getName(),
				marketItem.getPrice());
		player.sendMessage("You have bought: " + marketItem.toString()
				+ " for $" + marketItem.getPrice());

		// remove it from the market
		for (MarketItemStack item: market){
			if (item.equals(marketItem)){
				market.remove(item);
				break;
			}
		}
		

		// redraw the chest
		MenuPainter.paintMainMenu(event.getInventory(), 0, market);
		playerInfo.get(player.getName()).setIndex(0);
	}

	/**
	 * Returns whether or not a player can afford an item
	 * 
	 * @param player
	 *            The player
	 * @param item
	 *            The item the player wants to buy
	 * @return whether or not a player can afford an item
	 */
	private boolean playerCanAfford(Player player, MarketItemStack item) {
		double money = HomeWorldPlugin.economy.getBalance(player.getName());

		if (item.isBulk()) {
			if (money >= item.getPrice()) {
				return true;
			}
		}

		if (money >= (item.getPrice() * item.getAmount())) {
			return true;
		}

		return false;
	}

	/**
	 * Returns whether or not a player has an open inventory space
	 * 
	 * @param player
	 *            The player
	 * @return whether or not a player has an open inventory space
	 */
	private boolean playerHasSpace(Player player) {
		Inventory inv = player.getInventory();
		for (ItemStack st : inv.getContents()) {
			if (st == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * paints the chest with items that are currently on the market.
	 */
	

	

	private boolean atMarket(Player player, Inventory inv) {
		if (atMarket(player.getLocation())){
			if (inv.getSize() == 54){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns whether or not a player is inside of the marketplace. TODO:
	 * change the area to be a parameter for the object
	 * 
	 * @param player
	 * @param inv
	 * @return
	 */
	private boolean atMarket(Location location) {
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		if (region.contains(new Vector(x, y, z))){
			return true;
		}
		
		return false;
	}

	private boolean atMarket(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) {
			return false;
		}
		
		if (atMarket(event.getPlayer().getLocation())){
			if (atMarket(event.getClickedBlock().getLocation())){
				if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Save market data to file
	 */
	public void save(){
		
		configuration.getFile().delete();
		HashMap<String, Map<String, Object>> items = new HashMap<String, Map<String, Object>>();
		int i = 0;
		for (MarketItemStack m: market){
			items.put(Integer.toString(i), m.serialize());
			i++;
		}
		
		configuration.getConfig().createSection("items", items);
		configuration.saveConfig();
		
		System.out.println("[HomeWorldPlugin] Saved market");
		
	}

	/**
	 * load market data from file
	 */
	public void load(){

		//clear the old market
		market.clear();
		
		//get each item
		MemorySection itemlocation = (MemorySection) configuration.getConfig().get("items");
		
		if (itemlocation == null){
			return;
		}
		
		Set<String> items = itemlocation.getKeys(false);
		for (String index: items){

			//get the properties
			Set<String> itemProperties = ((MemorySection) itemlocation.get(index)).getKeys(false);
			
			//create a new mapping for the properties
			Map<String, Object> map = new HashMap<String, Object>();
			
			//add the properties to the new map
			for (String s: itemProperties){
				
				//fix for enchantments
				if (s.equals("enchantments")){
					//get all the enchantments
					Set<String> enchantments = ((MemorySection)itemlocation.get(index + "." + s)).getKeys(false);
					
					Map<String, Object> enchantmentMap = new HashMap<String, Object>();
					for (String enchantment: enchantments){
						enchantmentMap.put(enchantment, itemlocation.get(index + "." + s + "." + enchantment));
					}

					map.put(s, enchantmentMap);
				}
				else{
					map.put(s, itemlocation.get(index + "." + s));
				}
				
			}
			
			//create and add the new item
			MarketItemStack item = MarketItemStack.deserialize(map);
			market.add(item);
		}
		
		System.out.println("[HomeWorldPlugin] Loaded Market");
	}
}
