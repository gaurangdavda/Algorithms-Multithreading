package hw3;

/**
 *  @author gaurang
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionServer {
	/**
	 * Singleton: the following code makes the server a Singleton. You should
	 * not edit the code in the following noted section.
	 * 
	 * For test purposes, we made the constructor protected.
	 */
	/* Singleton: Begin code that you SHOULD NOT CHANGE! */
	protected AuctionServer() {

		// Code to calculate number of items sold every 100ms
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				oldCount = count;
				count = 0;
				synchronized (instanceLock) {
					for (int listingId : itemsAndIDs.keySet()) {
						if (!itemsAndIDs.get(listingId).biddingOpen() && !itemUnbid(listingId)) {
							count++;
						}
					}
					System.out.println("Items Sold in  " + i + " ms is " + (count - oldCount));
				}
				i = i + 100;
			}
		};
		long delay = 0;
		long intevalPeriod = 1 * 100;
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);

	}

	private static AuctionServer instance = new AuctionServer();

	public static AuctionServer getInstance() {
		return instance;
	}

	/* Singleton: End code that you SHOULD NOT CHANGE! */

	/*
	 * Statistic variables and server constants: Begin code you should likely
	 * leave alone.
	 */

	/**
	 * Server statistic variables and access methods:
	 */
	private int soldItemsCount = 0;
	private int revenue = 0;

	public int soldItemsCount() {
		return this.soldItemsCount;
	}

	public int revenue() {
		revenue = 0;
		for (int listingId : itemsAndIDs.keySet()) {
			if (!itemsAndIDs.get(listingId).biddingOpen() && highestBids.containsKey(listingId)) {
				revenue = revenue + highestBids.get(listingId);
			}
		}

		return revenue;
	}

	public int getSoldItemCount() {
		int count = 0;
		for (int i : itemsAndIDs.keySet()) {
			if (!itemsAndIDs.get(i).biddingOpen() && !itemUnbid(i)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Server restriction constants:
	 */
	public static final int maxBidCount = 10; // The maximum number of bids at
												// any given time for a buyer.
	public static final int maxSellerItems = 20; // The maximum number of items
													// that a seller can submit
													// at any given time.
	public static final int serverCapacity = 80; // The maximum number of active
													// items at a given time.

	/*
	 * Statistic variables and server constants: End code you should likely
	 * leave alone.
	 */

	/**
	 * Some variables we think will be of potential use as you implement the
	 * server...
	 */

	// List of items currently up for bidding (will eventually remove things
	// that have expired).
	private List<Item> itemsUpForBidding = new ArrayList<Item>();

	// The last value used as a listing ID. We'll assume the first thing added
	// gets a listing ID of 0.
	private int lastListingID = -1;

	// List of item IDs and actual items. This is a running list with everything
	// ever added to the auction.
	private HashMap<Integer, Item> itemsAndIDs = new HashMap<Integer, Item>();

	// List of itemIDs and the highest bid for each item. This is a running list
	// with everything ever added to the auction.
	private HashMap<Integer, Integer> highestBids = new HashMap<Integer, Integer>();

	// List of itemIDs and the person who made the highest bid for each item.
	// This is a running list with everything ever bid upon.
	private HashMap<Integer, String> highestBidders = new HashMap<Integer, String>();

	// List of sellers and how many items they have currently up for bidding.
	private HashMap<String, Integer> itemsPerSeller = new HashMap<String, Integer>();

	// List of buyers and how many items on which they are currently bidding.
	private HashMap<String, Integer> itemsPerBuyer = new HashMap<String, Integer>();

	private Map<String, Integer> sellerBlacklistMap = new HashMap<String, Integer>();

	private List<String> blacklistedSeller = new ArrayList<String>();

	private Map<String, Integer> sellerItemExpirationMap = new HashMap<String, Integer>();

	private List<String> blacklistedBidder = new ArrayList<String>();

	// Object used for instance synchronization if you need to do it at some+
	// point
	// since as a good practice we don't use synchronized (this) if we are doing
	// internal
	// synchronization.
	//
	private Object instanceLock = new Object();

	/*
	 * The code from this point forward can and should be changed to correctly
	 * and safely implement the methods as needed to create a working
	 * multi-threaded server for the system. If you need to add Object instances
	 * here to use for locking, place a comment with them saying what they
	 * represent. Note that if they just represent one structure then you should
	 * probably be using that structure's intrinsic lock.
	 */

	/**
	 * Attempt to submit an <code>Item</code> to the auction
	 * 
	 * @param sellerName
	 *            Name of the <code>Seller</code>
	 * @param itemName
	 *            Name of the <code>Item</code>
	 * @param lowestBiddingPrice
	 *            Opening price
	 * @param biddingDurationMs
	 *            Bidding duration in milliseconds
	 * @return A positive, unique listing ID if the <code>Item</code> listed
	 *         successfully, otherwise -1
	 */
	public int submitItem(String sellerName, String itemName, int lowestBiddingPrice, int biddingDurationMs) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// Make sure there's room in the auction site.
		// If the seller is a new one, add them to the list of sellers.
		// If the seller has too many items up for bidding, don't let them add
		// this one.
		// Don't forget to increment the number of things the seller has
		// currently listed.
		// null check for item
		synchronized (instanceLock) {
			if (itemsUpForBidding.size() < serverCapacity) {
				if (validateSeller(sellerName, lowestBiddingPrice)) {
					if (!itemsPerSeller.containsKey(sellerName)) {
						submitSellerItem(sellerName, itemName, lowestBiddingPrice, biddingDurationMs, 1);
					} else {
						if (itemsPerSeller.get(sellerName) < maxSellerItems) {
							if (validateUnBidItems(sellerName)) {
								int newCount = itemsPerSeller.get(sellerName) + 1;
								submitSellerItem(sellerName, itemName, lowestBiddingPrice, biddingDurationMs, newCount);
							} else {
								blacklistSeller(sellerName);
							}
						} else {
							return -1;
						}
					}
					return lastListingID;
				}
			}
			return -1;
		}
	}

	private void submitSellerItem(String sellerName, String itemName, int lowestBiddingPrice, int biddingDurationMs,
			int count) {
		lastListingID++;
		Item newItem = new Item(sellerName, itemName, lastListingID, lowestBiddingPrice, biddingDurationMs);
		itemsUpForBidding.add(newItem);
		itemsPerSeller.put(sellerName, count);
		itemsAndIDs.put(lastListingID, newItem);
	}

	private boolean validateUnBidItems(String sellerName) {
		for (Item i : itemsAndIDs.values()) {
			if (i.seller().equals(sellerName)) {
				if (!i.biddingOpen() && itemUnbid(i.listingID())) {
					if (sellerItemExpirationMap.containsKey(sellerName)) {
						int count = sellerItemExpirationMap.get(sellerName);
						if (count == 3) {
							blacklistSeller(sellerName);
							return false;
						} else {
							sellerItemExpirationMap.put(sellerName, count++);
							return true;
						}
					} else {
						sellerItemExpirationMap.put(sellerName, 1);
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	private void blacklistSeller(String sellerName) {
		List<Item> toBeRemoved = new ArrayList<Item>();
		for (Item i : itemsUpForBidding) {
			if (i.seller().equals(sellerName) && i.biddingOpen()) {
				toBeRemoved.add(i);
			}
		}
		for (Item i : toBeRemoved) {
			itemsUpForBidding.remove(i);
			itemsAndIDs.remove(i.listingID());
		}
		itemsPerSeller.remove(sellerName);
		blacklistedSeller.add(sellerName);
		System.out.println("Seller " + sellerName + " added to blacklist.");
	}

	private boolean validateSeller(String sellerName, int lowestBiddingPrice) {

		if (!blacklistedSeller.contains(sellerName)) {
			if (lowestBiddingPrice < 10) {
				if (sellerBlacklistMap.containsKey(sellerName)) {
					int count = sellerBlacklistMap.get(sellerName);
					if (count == 2) {
						blacklistSeller(sellerName);
						return false;
					} else {
						sellerBlacklistMap.put(sellerName, count + 1);
						return true;
					}
				} else {
					sellerBlacklistMap.put(sellerName, 1);
					return true;
				}
			} else {
				sellerBlacklistMap.put(sellerName, 0);
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all <code>Items</code> active in the auction
	 * 
	 * @return A copy of the <code>List</code> of <code>Items</code>
	 */
	public List<Item> getItems() {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// Don't forget that whatever you return is now outside of your control.
		synchronized (instanceLock) {
			List<Item> copy = new ArrayList<Item>();
			for (Item i : itemsUpForBidding)
				if (i.biddingOpen())
					copy.add(i);
			return copy;
		}
	}

	/**
	 * Attempt to submit a bid for an <code>Item</code>
	 * 
	 * @param bidderName
	 *            Name of the <code>Bidder</code>
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @param biddingAmount
	 *            Total amount to bid
	 * @return True if successfully bid, false otherwise
	 */
	public boolean submitBid(String bidderName, int listingID, int biddingAmount) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// See if the item exists.
		// See if it can be bid upon.
		// See if this bidder has too many items in their bidding list.
		// Get current bidding info.
		// See if they already hold the highest bid.
		// See if the new bid isn't better than the existing/opening bid floor.
		// Decrement the former winning bidder's count
		// Put your bid in place
		synchronized (instanceLock) {
			Item item = itemsAndIDs.get(listingID);
			if (!itemsPerBuyer.containsKey(bidderName) || itemsPerBuyer.get(bidderName) < maxBidCount) {
				if (item != null && itemsUpForBidding.contains(item) && item.biddingOpen() && validateBidder(bidderName)
						&& (!highestBidders.containsKey(listingID) || biddingAmount > highestBids.get(listingID))) {
					String oldBidder = highestBidders.get(listingID);
					if (oldBidder != null && itemsPerBuyer.containsKey(oldBidder)) {
						itemsPerBuyer.put(oldBidder, itemsPerBuyer.get(oldBidder) - 1);
					}
					highestBidders.put(listingID, bidderName);
					highestBids.put(listingID, biddingAmount);
					if (itemsPerBuyer.containsKey(bidderName)) {
						itemsPerBuyer.put(bidderName, itemsPerBuyer.get(bidderName) + 1);
					} else {
						itemsPerBuyer.put(bidderName, 1);
					}
					return true;
				} else {
					if (!validateBidder(bidderName)) {
						System.out.println(bidderName + " is Blacklisted");
					}
				}
			} else {
				if (!blacklistedBidder.contains(bidderName)) {
					blacklistedBidder.add(bidderName);
				}
				return false;
			}
			return false;
		}

	}

	private boolean validateBidder(String bidderName) {
		if (blacklistedBidder.contains(bidderName)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Check the status of a <code>Bidder</code>'s bid on an <code>Item</code>
	 * 
	 * @param bidderName
	 *            Name of <code>Bidder</code>
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return 1 (success) if bid is over and this <code>Bidder</code> has
	 *         won<br>
	 *         2 (open) if this <code>Item</code> is still up for auction<br>
	 *         3 (failed) If this <code>Bidder</code> did not win or the
	 *         <code>Item</code> does not exist
	 */
	public int checkBidStatus(String bidderName, int listingID) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// If the bidding is closed, clean up for that item.
		// Remove item from the list of things up for bidding.
		// Decrease the count of items being bid on by the winning bidder if
		// there was any...
		// Update the number of open bids for this seller
		synchronized (instanceLock) {
			if (itemsAndIDs.containsKey(listingID)) {
				if (itemsAndIDs.get(listingID).biddingOpen()) {
					validateUnBidItems(itemsAndIDs.get(listingID).seller());
					return 2;
				} else {
					Item item = itemsAndIDs.get(listingID);
					validateUnBidItems(item.seller());
					if (itemsUpForBidding.contains(item)) {
						itemsUpForBidding.remove(item);
					}
					if (highestBidders.get(listingID).equals(bidderName)) {
						return 1;
					} else {
						return 3;
					}
				}
			} else {
				return 3;
			}
		}
	}

	/**
	 * Check the current bid for an <code>Item</code>
	 * 
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return The highest bid so far or the opening price if no bid has been
	 *         made, -1 if no <code>Item</code> exists
	 */
	public int itemPrice(int listingID) {
		// TODO: IMPLEMENT CODE HERE
		synchronized (instanceLock) {
			if (highestBids.containsKey(listingID)) {
				return highestBids.get(listingID);
			} else {
				for (Item i : getItems())
					if (i.listingID() == listingID)
						return i.lowestBiddingPrice();
			}
			return -1;
		}
	}

	/**
	 * Check whether an <code>Item</code> has been bid upon yet
	 * 
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return True if there is no bid or the <code>Item</code> does not exist,
	 *         false otherwise
	 */
	public Boolean itemUnbid(int listingID) {
		// TODO: IMPLEMENT CODE HERE
		synchronized (instanceLock) {
			if (!highestBids.containsKey(listingID))
				return true;
			return false;
		}
	}

	private Timer timer = new Timer();
	int i = 100;
	int count = 0;
	int oldCount = 0;

	public Timer getTimer() {
		return timer;
	}

	public int getUnSoldItemCount() {
		int count = 0;
		for (int i : itemsAndIDs.keySet()) {
			if (itemsAndIDs.get(i).biddingOpen() == false && itemUnbid(i) == true) {
				count++;
			}
		}
		return count;
	}

	public int totalItemsListed() {
		return lastListingID + 1;
	}
}
