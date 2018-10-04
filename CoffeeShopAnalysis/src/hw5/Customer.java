package hw5;

import java.util.List;

/**
 * @author Gaurang Davda
 */
/**
 * Customers are simulation actors that have two fields: a name, and a list of
 * Food items that constitute the Customer's order. When running, an customer
 * attempts to enter the coffee shop (only successful if the coffee shop has a
 * free table), place its order, and then leave the coffee shop when the order
 * is complete.
 */
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;

	private static int runningCounter = 0;
	private CustomerType priority;

	/**
	 * You can feel free modify this constructor. It must take at least the name
	 * and order but may take other parameters if you would find adding them
	 * useful.
	 */
	public Customer(String name, List<Food> order, CustomerType priority) {
		this.name = name;
		this.order = order;
		this.orderNum = runningCounter++;
		this.priority = priority;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method defines what an Customer does: The customer attempts to enter
	 * the coffee shop (only successful when the coffee shop has a free table),
	 * place its order, and then leave the coffee shop when the order is
	 * complete.
	 */
	public void run() {
		// YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
		validateCustomerEntry();
		placeAnOrder();
		addOrder();
		completeOrder();
		eatOrder();
		leaveCoffeeShop();
	}

	private void validateCustomerEntry() {
		synchronized (Simulation.currentCapacity) {
			while (!(Simulation.currentCapacity.size() < Simulation.events.get(0).simParams[2])) {
				try {
					Simulation.currentCapacity.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Simulation.currentCapacity.add(this);
			Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
			Simulation.currentCapacity.notifyAll();
		}
	}

	private void placeAnOrder() {
		synchronized (Simulation.listOfOrders) {
			Simulation.listOfOrders.add(this);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, this.order, this.orderNum));
			Simulation.listOfOrders.notifyAll();
		}
	}

	private void addOrder() {
		synchronized (Simulation.finishedOrder) {
			Simulation.finishedOrder.put(this, false);
		}
	}

	private void completeOrder() {
		synchronized (Simulation.finishedOrder) {
			while (!(Simulation.finishedOrder.get(this))) {
				try {
					Simulation.finishedOrder.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
			Simulation.finishedOrder.notifyAll();
		}

	}

	private void eatOrder() {
		synchronized (Simulation.currentCapacity) {
			try {
				Simulation.currentCapacity.wait(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void leaveCoffeeShop() {
		synchronized (Simulation.currentCapacity) {
			Simulation.currentCapacity.remove(this);
			Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
			Simulation.currentCapacity.notifyAll();
		}

	}

	public enum CustomerType {
		Gold, Regular;
	}

	public CustomerType getPriority() {
		return priority;
	}

	public List<Food> getOrder() {
		return order;
	}

	public int getOrderNum() {
		return orderNum;
	}

}