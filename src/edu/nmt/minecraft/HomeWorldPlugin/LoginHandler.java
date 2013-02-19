package edu.nmt.minecraft.HomeWorldPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginHandler implements Listener{

	
	public Set<String> knownPlayers = null;
	
	public LoginHandler(){
		
		knownPlayers = new HashSet<String>();
		load();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if (! knownPlayers.contains(player.getName())){
			knownPlayers.add(player.getName());
			player.sendMessage("Welcome to the NMT Minecraft Server! Type /help to get started.");
		}
		else{
			
		}
		System.out.println("soefhseoihfse");
	}

	public void load(){
		ConfigManager cm = HomeWorldPlugin.configurations.get("login.yml");

		knownPlayers.clear();
	
		@SuppressWarnings("unchecked")
		ArrayList<String> keys = (ArrayList<String>) cm.getConfig().get("knownplayers");
		
		if (keys == null){
			return;
		}
		
		knownPlayers.addAll(keys);
		
		for (String player: keys){
			knownPlayers.add(player);
		}
		
		System.out.println("[HomeWorldPlugin] Loaded Login Info");
	}
	
	public void save(){
		
		ConfigManager cm = HomeWorldPlugin.configurations.get("login.yml");
		cm.getFile().delete();
		
		System.out.println(knownPlayers.toArray());
		
		cm.getConfig().set("knownplayers", knownPlayers.toArray());
		cm.saveConfig();
		
		System.out.println("[HomeWorldPlugin] Saved login info");
		
	}
}
