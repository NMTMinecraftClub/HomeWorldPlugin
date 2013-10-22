package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
	
	/**
	 * command handler. handles all commands
	 */
	public static boolean commands(CommandSender sender, Command cmd, String label, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("addMember")){
			
			if (args.length != 2){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.addMember(sender, args[0], args[1]);
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("removeMember")){
			
			if (args.length != 2){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.removeMember(sender, args[0], args[1]);
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("addOwner")){
			
			if (args.length != 2){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.addOwner(sender, args[0], args[1]);
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("removeOwner")){
			
			if (args.length != 2){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.removeOwner(sender, args[0], args[1]);
			}
			return true;
		}
		
		/**
		 * player wants to change the flags on their land
		 */
		if(cmd.getName().equalsIgnoreCase("flag")){
			
			if (args.length != 3){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.flagManager.setFlag(sender, args);
			}
			return true;
		}
		
		/**
		 * player wants to buy a plot of land
		 */
		if(cmd.getName().equalsIgnoreCase("buyland")){
			if (args.length != 1){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.buyLand(sender, args[0]);
			}
			return true;
		}
		
		/**
		 * player wants to sell a plot of land
		 */
		if(cmd.getName().equalsIgnoreCase("sellland")){
			if (args.length != 1){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.sellLand(sender, args[0]);
			}
			return true;
		}
		
		/**
		 * player wants to see the land they own
		 */
		if(cmd.getName().equalsIgnoreCase("listland")){
			if (args.length != 0){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.listRegions(sender);
			}
			return true;
		}
		
		//gets the price of your selection
		if(cmd.getName().equalsIgnoreCase("price")){
			if (args.length != 0){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.landManager.getPrice(sender);
			}
			return true;
		}
		
		/**
		 * whitelist commands
		 */
//		if(cmd.getName().equalsIgnoreCase("white")){
//			
//			
//			if (sender instanceof Player){
//				sender.sendMessage("Sorry, only the server can execute this command");
//				return false;
//			}
//			
//			
//			if (args.length == 3){
//				if (args[0].equals("add")){
//					if (args[1].equals("user")){
//						HomeWorldPlugin.whitelist.addPlayer(args[2]);
//						return true;
//					}
//					if (args[1].equals("ip")){
//						HomeWorldPlugin.whitelist.addIP(args[2]);
//						return true;
//					}
//					if (args[1].equals("mask")){
//						HomeWorldPlugin.whitelist.addMask(args[2]);
//						return true;
//					}
//				}
//				
//				sender.sendMessage("Wrong param");
//				return false;
//			}
//			
//			sender.sendMessage("Wrong number of arguments.");
//			return false;
//		}
		
//		/**
//		 * help command
//		 */
//		if(cmd.getName().equalsIgnoreCase("help")){
//			if (args.length == 0){
//				HomeWorldPlugin.help(sender);
//				return true;
//			}
//			
//			sender.sendMessage("Wrong number of arguments.");
//			return false;
//		}	
		
		
		return false;
	}
}
