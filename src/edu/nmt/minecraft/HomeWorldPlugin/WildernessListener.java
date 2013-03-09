/**
 * 
 */
package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * @author andreasanchez
 * 
 *
 */
public class WildernessListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void witherSpawn(CreatureSpawnEvent event){
		//CreatureSpawnEvent.SpawnReason.BUILD_WITHER.equals(true);
		//checks if a wither is spawned in HomeWorld
		if(event.getEntity().equals(org.bukkit.entity.EntityType.WITHER)){
			System.out.println("WITHER!!");
			if (! event.getLocation().getWorld().getName().matches("Wilderness")){
				System.out.println("tried to spawn wither in world that isn't the wilderness");
				event.setCancelled(true);
			}
		}
	}	
}
