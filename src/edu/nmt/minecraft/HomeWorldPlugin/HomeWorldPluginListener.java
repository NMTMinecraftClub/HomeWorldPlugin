/**
 * 
 */
package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

/**
 * @author andreasanchez
 * 
 *
 */
public class HomeWorldPluginListener implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void netherPortalItems(EntityPortalEnterEvent event){
		
		if (!event.getLocation().getWorld().getName().toLowerCase().startsWith("homeworld")){
			if (!event.getEntityType().equals(EntityType.PLAYER)){
				event.getEntity().remove();
			}
		}
		
	}
}
