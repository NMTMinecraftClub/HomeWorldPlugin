/**
 * 
 */
package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;

/**
 * @author andreasanchez
 * 
 *
 */
public class HomeWorldPluginListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void witherSpawn(CreatureSpawnEvent event){	
		if(event.getEntityType().equals(EntityType.WITHER)){
			
			System.out.println("Wither spawn attempt: " + event.getLocation() + " " + event.getSpawnReason());
			
			if (! event.getLocation().getWorld().getName().equals("Wilderness")){
				System.out.println("tried to spawn wither in world that isn't the wilderness");
				event.setCancelled(true);
			}
			else{
				System.out.println("spawned a wither");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void netherPortalItems(EntityPortalEnterEvent event){
		
		if (!event.getLocation().getWorld().getName().toLowerCase().startsWith("homeworld")){
			if (!event.getEntityType().equals(EntityType.PLAYER)){
				event.getEntity().remove();
			}
		}
		
	}
}
