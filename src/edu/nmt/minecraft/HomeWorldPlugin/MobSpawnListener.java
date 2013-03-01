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
public class MobSpawnListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void witherSpawn(CreatureSpawnEvent event){
		//CreatureSpawnEvent.SpawnReason.BUILD_WITHER.equals(true);
		
		System.out.println("");
		//checks if a wither is spawned in HomeWorld
		if(CreatureSpawnEvent.SpawnReason.BUILD_WITHER.equals(true)){
			System.out.println("tryed to spawn wither");
			
			if(! event.getLocation().getWorld().getName().equals("Wilderness")){
				System.out.println("Not in the Wilderness");
				event.getEntity().getType().isSpawnable();
				CreatureSpawnEvent.SpawnReason.BUILD_WITHER.equals(false);
			}
		}
		
	}
	
}
