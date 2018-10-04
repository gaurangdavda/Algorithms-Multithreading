package hw4;

import java.util.List;

/**
 * @Invariant As soon as the customer enters the coffee shop, a table is assigned to it until it eats the food
 * @Invariant Every customer has a unique name
 * @Invariant Every customer can have only 1 order and it has a unique order number
 */
/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the coffee shop (only successful if the
 * coffee shop has a free table), place its order, and then leave the 
 * coffee shop when the order is complete.
 */
public class Customer implements Runnable {
	//JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;    
	
	private static int runningCounter = 0;
	// enum for customer priority
	public enum CustomerType{
		Gold,
		Regular;
	}
	// customer priority variable
	private final CustomerType customerType;
	
	/**
	 * @Precondition name should not be null
	 * @Precondition order list should have at least 1 element
	 * @Precondition custType or customer priority should not be null
	 */
	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order, CustomerType custType) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		customerType = custType;
	}

	public String toString() {
		return name;
	}

	/**
	 * @Synchronized block by locking a table
	 * 
	 * @Postcondition customer completes eating the food and leaves the coffee shop
	 */
	/** 
	 * This method defines what an Customer does: The customer attempts to
	 * enter the coffee shop (only successful when the coffee shop has a
	 * free table), place its order, and then leave the coffee shop
	 * when the order is complete.
	 */
	public void run() {
		//YOUR CODE GOES HERE...
		
		
	}
}