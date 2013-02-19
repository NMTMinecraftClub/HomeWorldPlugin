package edu.nmt.minecraft.HomeWorldPlugin.market;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MarketPlayerInfo {
	
	static int maxSlots = 20;
	
	private int index;
	private int whichMenu;
	private String name;
	private int slots;
	private Inventory view;
	
	public MarketPlayerInfo(Player player){
		index = 0;
		whichMenu = 0;
		this.name = player.getName();
		slots = 0;
		view = Bukkit.getServer().createInventory(player, 54);
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getMenu(){
		return whichMenu;
	}
	
	public String getName(){
		return name;
	}
	
	public int getNumSlots(){
		return slots;
	}
	
	public Inventory getInventory(){
		return view;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void setMenu(int whichMenu){
		this.whichMenu = whichMenu;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setNumSlots(int slots){
		this.slots = slots;
	}
	
}
