package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
	
	/**
	 * command handler. handles all commands
	 */
	public static boolean commands(CommandSender sender, Command cmd, String label, String[] args){
		
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
		
		/**
		 * player wants to deposit money
		 */
		if(cmd.getName().equalsIgnoreCase("deposit")){
			if (args.length == 3){
				if (!(sender instanceof BlockCommandSender)){
					return false;
				}
				HomeWorldPlugin.economy.deposit(Bukkit.getPlayer(args[0]), args[1],Integer.parseInt(args[2]));
				return true;
			}
			if (args.length == 2){
				HomeWorldPlugin.economy.deposit(sender, args[0],Integer.parseInt(args[1]));
				return true;
			}
			else{
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
		}
		
		/**
		 * player wants to withdraw
		 */
		if(cmd.getName().equalsIgnoreCase("withdraw")){
			if (args.length == 3){
				if (!(sender instanceof BlockCommandSender)){
					return false;
				}
				HomeWorldPlugin.economy.withdraw(Bukkit.getPlayer(args[0]), args[1],Integer.parseInt(args[2]));
				return true;
			}
			if (args.length == 2){
				HomeWorldPlugin.economy.withdraw(sender, args[0],Integer.parseInt(args[1]));
				return true;
			}
			else{
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
		}
		
		/**
		 * player wants to see how much wealth they are carrying
		 */
		if(cmd.getName().equalsIgnoreCase("wealth")){
			if (args.length != 0){
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			else{
				HomeWorldPlugin.economy.calculateWealth(sender);
			}
			return true;
		}
		
		/**
		 * player wanted to check the value of something
		 */
		if(cmd.getName().equalsIgnoreCase("value")){
			if (args.length == 1){
				HomeWorldPlugin.economy.checkValue(sender, args[0], 1);
				return true;
			}
			if (args.length == 2){
				HomeWorldPlugin.economy.checkValue(sender, args[0], Integer.parseInt(args[1]));
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
		}
		
		/**
		 * player wants to check their balance
		 */
		if(cmd.getName().equalsIgnoreCase("money")){
			if (args.length == 0){
				HomeWorldPlugin.economy.showBalance(sender);
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
		}
		
		/**
		 * server wants to set a players balance
		 */
		if(cmd.getName().equalsIgnoreCase("setbalance")){
			if (args.length == 2){
				HomeWorldPlugin.economy.setBalance(sender, args[0], Double.parseDouble(args[1]));
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
		}
		
		/**
		 * player wants to give another player a certain amount
		 */
		if(cmd.getName().equalsIgnoreCase("pay")){
			if (args.length == 2){
				HomeWorldPlugin.economy.pay(sender, args[0], Double.parseDouble(args[1]));
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
		}
		
		/**
		 * player wants to see the top accounts on the server
		 */
		if(cmd.getName().equalsIgnoreCase("top")){
			if (args.length == 1){
				HomeWorldPlugin.economy.top(sender, Integer.parseInt(args[0]));
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
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
		
		/**
		 * help command
		 */
		if(cmd.getName().equalsIgnoreCase("help")){
			if (args.length == 0){
				HomeWorldPlugin.help(sender);
				return true;
			}
			
			sender.sendMessage("Wrong number of arguments.");
			return false;
		}	
		
		
		return false;
	}
}
