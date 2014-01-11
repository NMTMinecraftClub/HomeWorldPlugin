package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
	
	/**
	 * command handler. handles all commands
	 */
	public static boolean commands(CommandSender sender, Command cmd, String label, String[] args){
		
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
