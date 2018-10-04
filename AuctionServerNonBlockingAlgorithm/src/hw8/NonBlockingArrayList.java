/**
 * 
 */
package hw8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Gaurang Davda
 *
 */
public class NonBlockingArrayList<E> {

	private AtomicReference<List<E>> list = new AtomicReference<List<E>>(new ArrayList<E>());

	public void add(E item) {
		List<E> oldlist;
		List<E> newlist;
		do {
			oldlist = list.get();
			newlist = new ArrayList<E>(oldlist);
			newlist.add(item);
		} while (!list.compareAndSet(oldlist, newlist));

	}

	public List<E> getList() {
		List<E> result = list.get();
		return result;
	}

	public Boolean contains(E item) {
		List<E> oldlist = list.get();
		return oldlist.contains(item);
	}

	public void remove(E item) {
		List<E> oldlist;
		List<E> newlist;
		do {
			oldlist = list.get();
			newlist = new ArrayList<E>(oldlist);
			newlist.remove(item);
		} while (!list.compareAndSet(oldlist, newlist));
	}

	public int size() {
		List<E> oldlist = list.get();
		return oldlist.size();
	}
}
