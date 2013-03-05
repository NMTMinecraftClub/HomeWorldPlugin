package edu.nmt.minecraft.HomeWorldPlugin;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import edu.nmt.minecraft.HomeWorldPlugin.economy.Currency;
import edu.nmt.minecraft.HomeWorldPlugin.economy.EconomyListener;
import edu.nmt.minecraft.HomeWorldPlugin.economy.EconomyManager;
import edu.nmt.minecraft.HomeWorldPlugin.land.LandManager;
import edu.nmt.minecraft.HomeWorldPlugin.market.MarketPlace;
//import edu.nmt.minecraft.HomeWorldPlugin.arena.Arena;

/**
 * Main class for the plugin. Everything starts from here
 * @author Matthew
 *
 */
public class HomeWorldPlugin extends org.bukkit.plugin.java.JavaPlugin{
	
	/**
	 * WorldGuard Plugin
	 */
	public static WorldGuardPlugin wgplugin = null;
	
	/**
	 * WorldEdit Plugin
	 */
	public static WorldEditPlugin weplugin = null;

	
	/**
	 * Default Constructor. Only sets up internal objects. Does not mess with plugins or hooks.
	 */
	public HomeWorldPlugin(){
		
	}
	
	public static EconomyManager economy = null;
	public static LandManager landManager = null;
	public static MarketPlace market;
	public static FileManager fileManager = null;
	public static HashMap<String, ConfigManager> configurations = null;
	//public static Whitelist whitelist;
	public static FileSavingThread savingThread = null;
	public static LoginHandler loginHandler = null;
	public static MobSpawnListener mobSpawnListener = null;
	
	
	/**
	 * This is ran once the plugin is enabled. It is ran after the constructor.
	 * loads the houses from a local file.
	 */
	public void onEnable(){		
		
		//set up configurations
		configurations = new HashMap<String, ConfigManager>();
		//configurations.put("market.yml", new ConfigManager(this, "market.yml"));
		configurations.put("login.yml", new ConfigManager(this, "login.yml"));
		//configurations.put("whitelist.yml", new ConfigManager(this, "whitelist.yml"));
		//TODO: switch economy to use configurations
		
		//get configurations
		for (ConfigManager cm: configurations.values()){
			cm.reloadConfig();
		}
		
		//setup fileManager
		fileManager = new FileManager();
		
		//setup market
		market = new MarketPlace(this, "market", new ProtectedCuboidRegion("__market__spawn", new BlockVector(200, 70, 144), new BlockVector(220, 90, 168)));
		
		//setup arena
		//getServer().getPluginManager().registerEvents(new Arena(), this);
		
		//setup economy
		economy = new EconomyManager();
		//TODO: turn these into a configuration file
		economy.addCurrency(new Currency("coal", Material.COAL, 0, 10));
		economy.addCurrency(new Currency("redstone", Material.REDSTONE, 0, 15));
		economy.addCurrency(new Currency("coal_ore", Material.COAL_ORE, 0, 22));
		economy.addCurrency(new Currency("iron_ore", Material.IRON_ORE, 0, 20));
		economy.addCurrency(new Currency("iron", Material.IRON_INGOT, 0, 20));
		economy.addCurrency(new Currency("redstone_ore", Material.REDSTONE_ORE, 0, 33));
		economy.addCurrency(new Currency("lapis", Material.INK_SACK, 4, 35));
		economy.addCurrency(new Currency("lapis_ore", Material.LAPIS_ORE, 0, 77));
		economy.addCurrency(new Currency("iblock", Material.IRON_BLOCK, 0, 180));
		economy.addCurrency(new Currency("gold_ore", Material.GOLD_ORE, 0, 200));
		economy.addCurrency(new Currency("gold",  Material.GOLD_INGOT, 0, 200));
		economy.addCurrency(new Currency("lblock", Material.LAPIS_BLOCK, 0, 315));
		economy.addCurrency(new Currency("diamond", Material.DIAMOND, 0, 400));
		economy.addCurrency(new Currency("diamond_ore", Material.DIAMOND_ORE, 0, 880));
		economy.addCurrency(new Currency("gblock", Material.GOLD_BLOCK, 0, 1800));
		economy.addCurrency(new Currency("dblock", Material.DIAMOND_BLOCK, 0, 3600));
		getServer().getPluginManager().registerEvents(new EconomyListener(), this);
		
		//setup worldguard hook
		wgplugin = getWorldGuard();
		if (wgplugin == null){
			getLogger().severe("HOMEWORLD FAILED TO LOAD WORLDGUARD");
		}
		
		//setup worldedit hook
		weplugin = getWorldEdit();
		if (weplugin == null){
			getLogger().severe("HOMEWORLD FAILED TO LOAD WORLDEDIT");
		}
		
		//set up the landmanager
		landManager = new LandManager();
		
		//setup whitelist
		//whitelist = new Whitelist();
		//getServer().getPluginManager().registerEvents(whitelist, this);
		//whitelist.load();
		
		//set up the thread that saves data
		savingThread = new FileSavingThread();
		savingThread.start();
		
		//set up login handler
		loginHandler = new LoginHandler();
		getServer().getPluginManager().registerEvents(loginHandler, this);
		
		try{
			mobSpawnListener = new MobSpawnListener();
			getServer().getPluginManager().registerEvents(mobSpawnListener, this);
		}catch(Exception e){
			System.out.println(e.getMessage().equals("Something with a wither"));
			
		}
		
		
		getLogger().info("[HomeWorldPlugin] HomeWorldPlugin has been enabled.");
	}
 
	/**
	 * ran when the plugin is being disabled. saves the houses to file.
	 */
	public void onDisable(){
		saveAll();
		
		getLogger().info("[HomeWorldPlugin] HomeWorldPlugin has been disbled.");
	}
	
	/**
	 * Reload method. Makes sure all data is saved to file.
	 */
	public void onReload(){
		saveAll();
		loadAll();
		getLogger().info("[HomeWorldPlugin] HomeWorldPlugin has been reloaded.");
	}
	
	/**
	 * method for WorldGuard to get the WorldGuard Plugin
	 * @return the WorldGuard Plugin
	 */
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	return null; // Maybe you want throw an exception instead
	    }
	    
	    return (WorldGuardPlugin) plugin;
	}
	
	/**
	 * method for WorldEdit to get the WorldEdit Plugin
	 * @return the WorldEdit Plugin
	 */
	public WorldEditPlugin getWorldEdit() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldEditPlugin) plugin;
	}
	
	/**
	 * commands are handled by CommandHandler
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return CommandHandler.commands(sender, cmd, label, args);
	}

	/**
	 * Message displayed to the player when he or she asks for help
	 * @param sender
	 */
	public static void help(CommandSender sender) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage("Only players can execute this command.");
			return;
		}
		
		Inventory inv = ((Player)sender).getInventory();
		if (inv.firstEmpty() == -1){
			sender.sendMessage("Please clear a spot in your inventory for the help book.");
			return;
		}
		
		//create a new book
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		

		//Get item meta
		BookMeta meta = (BookMeta) book.getItemMeta();
		
		
		//set title and author
		meta.setTitle("Welcome!");
		meta.setAuthor("NMT Minecraft Club");
		
		//set pages
		meta.addPage("Welcome to the NMT Minecraft Club Server! The NMT Miners (pun intended) have come together to create a dedicated minecraft server for the campus! This book will explain exactly how everything works on the server.");
		meta.addPage("The Spawn: The spawn exists in the HomeWorld. Here, PvP is turned off. The spawn includes portals to other world. One portal leads to a creative world. Another leads to a PvP world known as the wilderness.");
		meta.addPage("The Economy: coal, iron (iggots), gold (iggots), diamonds, redstone, and lapis can be traded for money, but only on the HomeWorld. Use /money to see your account. /deposit [ore] [ammount] and /withdraw [ore] [ammount] can be used for transactions");
		meta.addPage("Other economy commands: /wealth: Displays how much money you are carrying in physical currencies. /value: Displays how much an amount of a certain currency is worth. /pay: Pay a player a certain amount. /top: See the players with the highest accounts");
		meta.addPage("The MarketPlace. Markets are places where players can buy and sell items from one another. Right now, the only market is located in the spawn.");
		meta.addPage("Land: You can purchase plots of land to protect your creations from other players. use //wand to select a plot of lan. buy the land with /buyland [name]. Sell it for the orginal price with /sellland [name]. You can list the land you own with /listland.");
		meta.addPage("Arena: The arena is currently under construction. Sorry about that");
		
		//set the meta
		book.setItemMeta(meta);
		
		//add the item
		inv.addItem(book);
		
		sender.sendMessage("A book has been placed in your inventory.");
	
	}

	
	public static void saveAll() {
		for (ConfigManager cm: configurations.values()){
			cm.saveConfig();
		}
		economy.save();
		market.save();
		loginHandler.save();
		//whitelist.save();
	}
	
	public static void loadAll(){
		for (ConfigManager cm: configurations.values()){
			cm.reloadConfig();
		}
		economy.load();
		market.load();
		loginHandler.load();
		//whitelist.load();
	}
	
	
	

}

