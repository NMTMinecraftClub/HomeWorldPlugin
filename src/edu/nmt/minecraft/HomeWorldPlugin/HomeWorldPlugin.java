package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * Main class for the plugin. Everything starts from here
 * @author Matthew
 *
 */
public class HomeWorldPlugin extends org.bukkit.plugin.java.JavaPlugin{
	
	public static HomeWorldPluginListener mobSpawnListener = null;
	
	/**
	 * This is ran once the plugin is enabled. It is ran after the constructor.
	 * loads the houses from a local file.
	 */
	public void onEnable(){		
		
		try{
			mobSpawnListener = new HomeWorldPluginListener();
			getServer().getPluginManager().registerEvents(mobSpawnListener, this);
			getLogger().info("Wilderness listener has been enabled");
		}catch(Exception e){
			getLogger().warning("Unable to enable wilderness listener");
		}
		
	}
 
	public void onDisable(){

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

}