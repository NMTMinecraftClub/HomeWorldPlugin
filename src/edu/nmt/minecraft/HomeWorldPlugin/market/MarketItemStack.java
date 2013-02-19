package edu.nmt.minecraft.HomeWorldPlugin.market;

import java.util.Map;

import org.bukkit.inventory.ItemStack;


/**
 * Represents an item on the market. Includes item info and market info
 * @author Matthew
 *
 */
public class MarketItemStack extends ItemStack{


	/**
	 * The seller of the item(s)
	 */
	private String seller;
	
	/**
	 * the price of the item, either bulk or individual price
	 */
	private double price;
	
	/**
	 * whether or not the item is sold in bulk
	 */
	private boolean bulk;
	
	/**
	 * whether or not the item has sold. Needed in case two or more players buy an item at the same time
	 */
	private boolean hasSold;
	
	/**
	 * default constructor
	 */
	public MarketItemStack(ItemStack stack, String seller, double price, boolean bulk, boolean hasSold) {
		super(stack);
		this.seller = seller;
		this.price = price;
		this.bulk = bulk;
		this.hasSold = hasSold;
	}
	
	/**
	 * Returns the seller of the ItemStack
	 * @return the seller of the ItemStack
	 */
	public String getSeller(){
		return seller;
	}
	
	/**
	 * Returns the price of the ItemStack
	 * @return the price of the ItemStack
	 */
	public double getPrice(){
		return price;
	}
	
	/**
	 * Returns whether or not the ItemStack is for bulk sale
	 * @return whether or not the ItemStack is for bulk sale
	 */
	public boolean isBulk(){
		return bulk;
	}
	
	/**
	 * Returns whether or not an item has been already sold
	 * @return whether or not an item has been already sold
	 */
	public boolean hasSold(){
		return hasSold;
	}
	
	/**
	 * Sets the price of the ItemStack
	 * @param price the price of the ItemStack to be set
	 */
	public void setPrice(double price){
		this.price = price;
	}
	
	/**
	 * Sets the seller of the ItemStack
	 * @param seller the seller of the ItemStack to be set
	 */
	public void setSeller(String seller){
		this.seller = seller;
	}

	/**
	 * Sets the ItemStack to be or not be for bulk sale
	 * @param bulk
	 */
	public void setBulk(boolean bulk){
		this.bulk = bulk;
	}
	
	/**
	 * Sets whether or not an item has been sold
	 * @param hasSold whether or not an item has been sold
	 */
	public void setSold(boolean hasSold){
		this.hasSold = hasSold;
	}
	
	@Override
	public String toString(){
		return super.toString() + " price " + this.price + " seller " + this.seller + " isBulk " + bulk + " hasSold " + hasSold;		
	}
	
	@Override
	public Map<String,Object> serialize(){
		Map<String,Object> m = super.serialize();
		m.put("price", price);
		m.put("seller", seller);
		m.put("isBulk", bulk);
		m.put("hasSold", hasSold);
		
		return m;
	}
	
	public static MarketItemStack deserialize(Map<String, Object> args){
		String seller = null;
		double price = 0;
		boolean bulk = false;
		boolean hasSold = false;
		
		if (args.containsKey("seller")){
			seller = (String) args.get("seller");
		}
		
		if (args.containsKey("price")){
			price = (Double) args.get("price");
		}
		
		if (args.containsKey("isBulk")){
			bulk = (Boolean) args.get("isBulk");
		}
		
		if (args.containsKey("hasSold")){
			hasSold = (Boolean) args.get("hasSold");
		}
		
		MarketItemStack m = new MarketItemStack(ItemStack.deserialize(args), seller, price, bulk, hasSold);

		return m;
		
	}
	
	@Override
	public boolean equals(Object item){
		
		System.out.println("Testing");
		
		if (!(item instanceof MarketItemStack)){
			return false;
		}
		
		MarketItemStack newItem = (MarketItemStack) item;
		
		if (! super.equals( newItem.toItemStack() )){
			return false;
		}
		
		
		
		if (! seller.equals(newItem.getSeller())){
			return false;
		}
		
		if (price != newItem.getPrice()){
			return false;
		}
		
		if (bulk != newItem.isBulk()){
			return false;
		}
		
		if (hasSold != newItem.hasSold()){
			return false;
		}
		
		return true;
	}
	
	
	public ItemStack toItemStack(){
		return new ItemStack(this);
	}
}
