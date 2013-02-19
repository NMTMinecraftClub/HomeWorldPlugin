package edu.nmt.minecraft.HomeWorldPlugin;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


/**
 * class that acts as a Whitelist
 * @author Matthew
 *
 */
public class Whitelist implements Listener{

	private Set<String> usernames = null;
	private Set<String> ipv4s = null;
	private Set<String> ipv4masks = null;
	
	/**
	 * Save whitelist data to file
	 */
	public void save(){
		
		ConfigManager cm = HomeWorldPlugin.configurations.get("whitelist.yml");
		cm.getFile().delete();
		
		cm.getConfig().set("usernames", usernames.toArray());
		cm.getConfig().set("ips", ipv4s.toArray());
		cm.getConfig().set("masks", ipv4masks.toArray());
		
		cm.saveConfig();
	}

	/**
	 * load whitelist data from file
	 */
	@SuppressWarnings("unchecked")
	public void load(){
		ConfigManager cm = HomeWorldPlugin.configurations.get("whitelist.yml");
		
		//get each item
		ArrayList<String> m = ((ArrayList<String>) cm.getConfig().get("usernames"));
		if (m != null){
			usernames = new HashSet<String>(m);
		}
		else{
			usernames = new HashSet<String>();
		}
		
		m = ((ArrayList<String>) cm.getConfig().get("ips"));
		if (m != null){
			ipv4s = new HashSet<String>(m);
		}
		else{
			ipv4s = new HashSet<String>();
		}
		
		
		m = ((ArrayList<String>) cm.getConfig().get("masks"));
		if (m != null){
			ipv4masks = new HashSet<String>(m);
		}
		else{
			ipv4masks = new HashSet<String>();
		}
		
		
	}
	
	public final static boolean isIP(String ipAddress){
	    String[] parts = ipAddress.split("\\.");
	    if (parts.length != 3){
	        return false;
	    }

	    for (String s: parts){
	        int i = Integer.parseInt(s);

	        if ((i < 0) || (i > 255)){
	            return false;
	        }
	    }

	    return true;
	}
	
	
	private boolean isOKIP(String ipAddress) {
		
		String[] addressParts = ipAddress.split("\\.");
		for (String mask: ipv4masks){
			
			String[] maskParts = mask.split("\\.");
			
			//length check
			if (maskParts.length > addressParts.length){
				return false;
			}
			
			boolean match = true;
			for (int i = 0; i < maskParts.length; i++){
				
				if (Integer.parseInt(maskParts[i]) != Integer.parseInt(addressParts[i])){
					match = false;
				}
			}
			
			if (match == true){
				return true;
			}

		}
		
		return true;
	}
	
	public void addPlayer(String name){
		
		if (! usernames.contains(name)){
			usernames.add(name);
		}
	}
	
	public void addIP(String address){
		
		if (! ipv4s.contains(address)){
			ipv4s.add(address);
		}
	}
	
	public void addMask(String mask){
		
		if (! ipv4masks.contains(mask)){
			ipv4masks.add(mask);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkWhitelist(PlayerLoginEvent event) {
		
		//check first for name
		String name = event.getPlayer().getName();
		if (usernames.contains(name)){
			event.allow();
			return;
		}
		
		//check for ipv4s
		InetAddress address = event.getAddress();
		if (ipv4s.contains(address.toString())){
			event.allow();
			return;
		}
		
		//check if mask is ok.
		if (isOKIP(address.toString().substring(1))){
			event.allow();
			return;
		}
		
		//deny
		event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Sorry, you are not whitelisted!!");
	}

	
	
	
	
	
}
