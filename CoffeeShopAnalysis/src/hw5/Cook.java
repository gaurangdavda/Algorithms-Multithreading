package hw5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import hw5.Customer.CustomerType;

/**
 * @author Gaurang Davda
 */
/**
 * Cooks are simulation actors that have at least one field, a name. When
 * running, a cook attempts to retrieve outstanding orders placed by Eaters and
 * process them.
 */
public class Cook implements Runnable {
	private final String name;
	private Customer currentCustomer;
	public List<Food> completedFood = new LinkedList<Food>();

	/**
	 * You can feel free modify this constructor. It must take at least the
	 * name, but may take other parameters if you would find adding them useful.
	 *
	 * @param: the
	 *             name of the cook
	 */
	public Cook(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows. The cook tries to retrieve orders placed
	 * by Customers. For each order, a List<Food>, the cook submits each Food
	 * item in the List to an appropriate Machine, by calling makeFood(). Once
	 * all machines have produced the desired Food, the order is complete, and
	 * the Customer is notified. The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some other thread
	 * calls the interrupt() method on it, which could raise
	 * InterruptedException if the cook is blocking), then it terminates.
	 */
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {
			while (true) {
				// YOUR CODE GOES HERE...
				synchronized (Simulation.listOfOrders) {

					while (Simulation.listOfOrders.isEmpty()) {
						Simulation.listOfOrders.wait();
					}
					currentCustomer = selectPriorityCustomer(Simulation.listOfOrders);
					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, currentCustomer.getOrder(),
							currentCustomer.getOrderNum()));
					Simulation.listOfOrders.notifyAll();
				}
				// sends food to specific machine
				for (int i = 0; i < currentCustomer.getOrder().size(); i++) {
					Food currentFood = currentCustomer.getOrder().get(i);
					if (currentFood.equals(FoodType.burger)) {
						synchronized (Simulation.grill.foodList) {
							while (!(Simulation.grill.foodList.size() < Simulation.grill.capacity)) {
								Simulation.grill.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.burger,
									currentCustomer.getOrderNum()));
							Simulation.grill.makeFood(this, currentCustomer.getOrderNum());
							Simulation.grill.foodList.notifyAll();

						}

					} else if (currentFood.equals(FoodType.fries)) {
						synchronized (Simulation.burner.foodList) {
							while (!(Simulation.burner.foodList.size() < Simulation.burner.capacity)) {
								Simulation.burner.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.fries,
									currentCustomer.getOrderNum()));
							Simulation.burner.makeFood(this, currentCustomer.getOrderNum());
							Simulation.burner.foodList.notifyAll();

						}

					} else {
						synchronized (Simulation.braun.foodList) {
							while (!(Simulation.braun.foodList.size() < Simulation.braun.capacity)) {
								Simulation.braun.foodList.wait();
							}
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.coffee,
									currentCustomer.getOrderNum()));
							Simulation.braun.makeFood(this, currentCustomer.getOrderNum());
							Simulation.braun.foodList.notifyAll();

						}
					}
				}
				synchronized (completedFood) {
					while (!(completedFood.size() == currentCustomer.getOrder().size())) {
						completedFood.wait();
						completedFood.notifyAll();
					}
				}
				Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, currentCustomer.getOrderNum()));

				synchronized (Simulation.finishedOrder) {
					Simulation.finishedOrder.put(currentCustomer, true);
					Simulation.finishedOrder.notifyAll();
				}
				completedFood = new LinkedList<Food>();

			}
		} catch (InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}

	private Customer selectPriorityCustomer(Queue<Customer> customerList) {
		for (Customer cust : customerList) {
			if (cust.getPriority().equals(CustomerType.Gold)) {
				Customer goldCustomer = customerList.remove();
				return goldCustomer;
			}
		}
		Customer regularCustomer = customerList.remove();
		return regularCustomer;
	}
}