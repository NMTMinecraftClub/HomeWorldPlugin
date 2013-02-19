package edu.nmt.minecraft.HomeWorldPlugin.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuPainter {

	public static ItemStack nameItem(ItemStack item, String name){

		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(item.getType().name());
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	/**
	 * Returns a place in the inventory, right-aligned to the offset
	 * 
	 * @param inv
	 *            the Inventory
	 * @param offset
	 *            the offset after the alignment
	 * @return a place in the inventory, right-aligned to the offset
	 */
	public static int getRight(Inventory inv, int offset) {
		return inv.getSize() - 1 - offset;
	}

	/**
	 * Returns a place in the inventory, left-aligned to the offset
	 * 
	 * @param inv
	 *            the Inventory
	 * @param offset
	 *            the offset after the alignment
	 * @return a place in the inventory, left-aligned
	 */
	public static int getLeft(Inventory inv, int offset) {
		return inv.getSize() - 9 + offset;
	}
	
	
	public static void paintMainMenu(Inventory inv, int index, ArrayList<MarketItemStack> market) {

		// clear inventory
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, null);
		}		

		// add menu buttons
		// add next buttons
		ItemStack next = nameItem(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x1), "Next page");
		ItemStack prev = nameItem(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x1), "Previous page");
		inv.setItem(getLeft(inv, 0), prev);
		inv.setItem(getRight(inv, 0), next);

		// add sell button
		inv.setItem(getRight(inv, 1), nameItem(new ItemStack(Material.WOOL, 1,(short) 0, (byte) 0x14), "Sell an item"));
		
		// add items
		//check if the index is in bounds
		if (index * 45 >= market.size()){
			return;
		}
		
		//display items
		for (int j = 0; j < java.lang.Math.min((inv.getSize() - 9),market.size() - (index*45)); j++) {
			MarketItemStack item = market.get((index * 45) + j);
			//display an item by it's price
			inv.setItem(j, nameItem(item, "$" + item.getPrice()));
		}

	}
	
	public static void paintBuyMenu(InventoryClickEvent event, HashMap<String, MarketItemStack> toBuy) {
		Inventory inv = event.getInventory();

		// clear inventory
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, null);
		}

		
		MarketItemStack item = toBuy.get(((Player) event.getWhoClicked()).getName());
		
		// add item
		inv.setItem(0, item);

		// add price
		int price = (int) item.getPrice();
		for (int i = 0; i < 9; i++) {
			int j = 0;
			while ((price % (java.lang.Math.pow(10, i + 1))) != 0) {
				j++;
				price -= java.lang.Math.pow(10, i);
			}
			// j = 0. make a zero
			if (j == 0) {

			}
			// j is not zero. make sticks
			else {
				inv.setItem(getRight(inv, 9 + i), (new ItemStack(Material.STICK, j)));
			}
		}

		// add menu buttons
		// add buy button
		ItemStack next = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x5);
		inv.setItem(getRight(inv, 1), nameItem(next, "Buy this item"));

		// add back button
		inv.setItem(getRight(inv, 0), nameItem(new ItemStack(Material.WOOL, 1,
				(short) 0, (byte) 0x2), "Return to Main Menu"));
	}
	
	public static void paintSellMenu(InventoryClickEvent event, HashMap<String, MarketItemStack> toSell) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();

		// clear inventory
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, null);
		}

		// add menu buttons
		// add sell button
		inv.setItem(getRight(inv, 1), nameItem(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x5), "Sell the selected item"));

		// add back button
		inv.setItem(getRight(inv, 0), nameItem(new ItemStack(Material.WOOL, 1,(short) 0, (byte) 0x2), "Return to Main Menu"));

		//add incrment and decrement buttons
		for (int i = 0; i < 9; i++){
			inv.setItem(getRight(inv, 27 + i), nameItem(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x3), "Increase price by $" + Math.pow(10, i)));
			inv.setItem(getRight(inv, 18 + i), nameItem(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 0x4), "Decrease price by $" + Math.pow(10, i)));
		}
		
		// add the item if it exists
		if (toSell.containsKey(player.getName())) {
			inv.setItem(0, toSell.get(player.getName()));

			// add price
			int price = (int) toSell.get(player.getName()).getPrice();
			for (int i = 0; i < 9; i++) {
				int j = 0;
				while ((price % (java.lang.Math.pow(10, i + 1))) != 0) {
					j++;
					price -= java.lang.Math.pow(10, i);
				}
				// j = 0. make a zero
				if (j == 0) {

				}
				// j is not zero. make sticks
				else {
					inv.setItem(getRight(inv, 9 + i), new ItemStack(Material.STICK, j));
				}
			}
		}
	}

}
