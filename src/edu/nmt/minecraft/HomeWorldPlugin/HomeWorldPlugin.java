package edu.nmt.minecraft.HomeWorldPlugin;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import edu.nmt.minecraft.HomeWorldPlugin.util.FileSavingThread;

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
	
	public static Economy economy = null;
	public static HashMap<String, ConfigManager> configurations = null;
	//public static Whitelist whitelist;
	public static FileSavingThread savingThread = null;
	public static LoginHandler loginHandler = null;
	public static HomeWorldPluginListener mobSpawnListener = null;
	
	/**
	 * This is ran once the plugin is enabled. It is ran after the constructor.
	 * loads the houses from a local file.
	 */
	public void onEnable(){		
		
		if (!this.getDataFolder().exists()){
			this.getDataFolder().mkdir();
		}
		
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
		
		setupEconomy();
		
		//setup market		
		//setup arena
		//getServer().getPluginManager().registerEvents(new Arena(), this);
		
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
		
		//setup whitelist
		//whitelist = new Whitelist();
		//getServer().getPluginManager().registerEvents(whitelist, this);
		//whitelist.load();
		
		//set up the thread that saves data
		if (savingThread == null){
			savingThread = new FileSavingThread();
			savingThread.start();
		}
		
		//set up login handler
		loginHandler = new LoginHandler();
		getServer().getPluginManager().registerEvents(loginHandler, this);
		
		try{
			mobSpawnListener = new HomeWorldPluginListener();
			getServer().getPluginManager().registerEvents(mobSpawnListener, this);
			getLogger().info("Wilderness listener has been enabled");
		}catch(Exception e){
			getLogger().warning("Unable to enable wilderness listener");
		}
		
		//register the economy
		try{
			this.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, economy, this, ServicePriority.Normal);
		}
		catch (Exception e){
			getLogger().warning("[HomeWorldPlugin] Unable to register Economy.");
		}
		
		
		getLogger().info("[HomeWorldPlugin] HomeWorldPlugin has been enabled.");
	}
 
	/**
	 * ran when the plugin is being disabled. saves the houses to file.
	 */
	public void onDisable(){
		if (savingThread != null){
			savingThread.die = true;
		}
		saveAll();
		getLogger().info("[HomeWorldPlugin] HomeWorldPlugin has been disbled.");
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
		meta.addPage("Welcome to the NMT Minecraft Club Server!\n\nThis book will explain exactly how everything works on the server.");
		meta.addPage("The Spawn: The spawn is in the HomeWorld but includes portals to other worlds. To see what portals are available, visit the giant NMT building near the center of the spawn.");
		meta.addPage("Economy Currencies: coal, coal_block, iron, iron_block, redstone, redstone_block, quartz, gold, gold_block, diamond, diamond_block.\n\nThese currencies can be put into your game account at a bank.");
		meta.addPage("Economy Commands:\n\ndeposit [currency] #\n- deposits # of currency into the bank\nwithdraw [currency] #\n- withdraws # of currency from the bank");
		meta.addPage("\nvalue [currency]\n- Gives you the $ value of currency\n\nvalue bank\n- gives you the # of currencies in the bank\n\nvalue current\n- gives you the dollar value of currencies in your inventory");
		meta.addPage("\nmoney\n- gives you your account balance\n\npay [player] #\n- pays player # of money\n\ntop #\n- displays the top # of accounts on the server");
		meta.addPage("Land: Purchase plots of land to protect your creations from other players. use //wand to select a plot of land. buy the land with /buyland [name]. Sell it for the orginal price with /sellland [name]. You can list the land you own with /listland.");
		meta.addPage("The MarketPlace. Markets are places where players can buy and sell items from one another. Right now, the only market is located in the spawn.");
		meta.addPage("Feel free to ask anyone on the server to help. Have fun and watch out for creepers.");
		
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
		loginHandler.save();
		//whitelist.save();
	}
	
	public static void loadAll(){
		for (ConfigManager cm: configurations.values()){
			cm.reloadConfig();
		}
		loginHandler.load();
		//whitelist.load();
	}
	
	/**
	 * Uses Vault to hook into an economy plugin
	 * @return
	 */
	public static boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	

}