package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
	
	/**
	 * command handler. handles all commands
	 */
	public static boolean commands(CommandSender sender, Command cmd, String label, String[] args){
		
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
