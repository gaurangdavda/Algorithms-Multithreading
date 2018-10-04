package hw2;

/**
 *  @author gaurang
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuctionServer {
	/**
	 * Singleton: the following code makes the server a Singleton. You should
	 * not edit the code in the following noted section.
	 * 
	 * For test purposes, we made the constructor protected.
	 */

	/* Singleton: Begin code that you SHOULD NOT CHANGE! */
	protected AuctionServer() {
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
		return this.revenue;
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

	// Object used for instance synchronization if you need to do it at some
	// point
	// since as a good practice we don't use synchronized (this) if we are doing
	// internal
	// synchronization.
	//
	// private Object instanceLock = new Object();

	/*
	 * The code from this point forward can and should be changed to correctly
	 * and safely implement the methods as needed to create a working
	 * multi-threaded server for the system. If you need to add Object instances
	 * here to use for locking, place a comment with them saying what they
	 * represent. Note that if they just represent one structure then you should
	 * probably be using that structure's intrinsic lock.
	 */

	/*
	  INVARIENTS : i) Item must not be expired.
	  			  ii) Item price must not be less than 10$ 
	  PRE-CONDITION : i) sellerItems should be less then max sellers 
	  				 ii) sellectName should not be in blacklist
	  				iii) itemsUpForBidding should be less then serverCapacity
	  POST-CONDITION : i) The item is submitted successfully and returns unique listingID
	  EXCEPTION : i) It throws IllegalArgumentException for the Pre-condition i) and ii) 					
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
		/*
		--------------------------------------------------------
		PSEUDOCODE
		--------------------------------------------------------
		****LOCK THE WHOLE SUBMIT ITEM METHOD****
		1) Proceed if number of items up for bidding is less than the server capacity.
		2) If the seller is not listed on the server, add the Seller.
		3) If the number of items per seller is less than maximum allowed seller items, proceed ahead
		4) If the lowest bidding price is above 10$, remove the seller name from the blacklist, if it exists, else proceed ahead
		5) If seller name is not present in the blacklist, add it with count as 1, else proceed ahead
		6) If the count is less than 2, increment the count, else proceed ahead.
		7) Add the seller to the banned sellers list and remove all of its items from the server
		8) Create a unique listing ID if the seller is not in the banned sellers list.
		8) If the seller is already listed on the server, Increment the number of items for the seller by 1
		9) Add the item to the itemsUpForBidding list
		-----------------------------------------------------------
		*/
		return -1;
	}

	/*
	  INVARIENTS : i) Check whether item is active
	  PRE-CONDITION : i) itemsUpForBidding should not be null
	  POST-CONDITION : i) The items in the list is returned
	  EXCEPTION : i) It throws NullPointerException for the Pre-condition i) 
	 */
	/**
	 * Get all <code>Items</code> active in the auction
	 * 
	 * @return A copy of the <code>List</code> of <code>Items</code>
	 */
	public List<Item> getItems() {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// Don't forget that whatever you return is now outside of your control.
		/*
		  -----------------------------------------------------------------
		  PSEUDOCODE
		  -----------------------------------------------------------------
		  1) LOCK THE ITEMS UP FOR BIDDING LIST so that no other thread can add into it until released.
		  2) Iterate through the 'itemsUpForBidding' list.
		  3) Add each of the active items in 'itemsUpForBidding' to a new list
		  4) Return the new list.
		  5) RELEASE THE LOCK ON ITEMS UP FOR BIDDING LIST.
		  -------------------------------------------------------------------
		*/ 
		return null;
	}

	/*
	  INVARIENTS : i) Bidding amount must be positive
	  POST-CONDITION : ii) The item is successfully bidded
	 */
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
/*
		---------------------------------------------------------------
		PSEUDOCODE
		---------------------------------------------------------------
		****LOCK THE SUBMIT BID METHOD****
		1) IF item is not null AND item is present in the itemsUpForBidding list AND item is open for bidding 
		 AND (bidderName is not present in itemsPerBuyer OR if the items count in itemsPerBuyer is less than maxBidCount)
		AND (highestBidders don't have listingID OR bidderAmount is greater than the highestBids), proceed ahead
		2) Decrement the item count from the previous bidder, if itemsPerBuyer has previous bidder's name
		3) Modify the bidding amount in the highestBids map
		4) Modify the bidderName in HighestBidders map
		5) If bidder name is not present in itemsPerBuyer, add it, else proceed ahead
		6) Increment the item count for the bidder and return true
		---------------------------------------------------------------
		*/
		return false;
	}

	/*
	  INVARIENTS  : 	i) Item must be present in the server
	  PRE-CONDITION :  i) listingID should be valid
	  POST-CONDITION : i) Checks the status and returns 1 (success),2 (open), 3(failure)
	  EXCEPTION :       i) It throws IllegalArgumantException for the Pre-condition i) 
	*/
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
		/*
		----------------------------------------------------------------------
		PSEUDOCODE
		----------------------------------------------------------------------
		****LOCK THE CHECK BID STATUS METHOD****
		1) Extract the item from the items and ids map using the listing id.
		2) If item is null, return '3' else proceed
		3) If bidding for the item is open, return '2' else proceed
		4) If the item is present in the 'itemsUpForBidding' list, remove it.
		5) Decrement the item count for that seller.
		6) If 'highestBidders' does not contain the listing id, return '3' else proceed
		7) If the name in the 'highestBidders' map equals the bidder name, return '1' else return '3'.
		----------------------------------------------------------------------
		*/
		return -1;
	}

	/*
	  INVARIENTS : i) Item must be present in the server
	  PRE-CONDITION : i) listingID should be valid
	  POST-CONDITION : i) Returns item price
	  EXCEPTION : i) It throws IllegalArgumantException for the Pre-condition i 
	*/
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
		/*
		  ------------------------------------------------
		  PSEUDOCODE
		  ------------------------------------------------
		  1) LOCK THE HIGHEST BIDS MAP. 
		  2) Check if the item is present for the listing id. Return -1 if not.
		  3) Check if the item is open for bidding. Proceed ahead only if true.
		  4) Check for the highest bid of that item from the 'highestBids' map and return it.
		  5) If there is no highest bid present, return the opening bid of the item.
		  6) UNLOCK THE HIGHEST BIDS MAP.
		  ------------------------------------------------
		 */
		return -1;
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
		/*
		---------------------------------------------------
		PSEUDOCODE
		---------------------------------------------------
		1) LOCK THE HIGHEST BIDS MAP.
		2) If the listing id is present in the 'highestBids' map, return false else return true.
		3) RELEASE THE LOCK ON HIGHEST BIDS MAP.
		----------------------------------------------------
		*/
		return false;
	}

}
