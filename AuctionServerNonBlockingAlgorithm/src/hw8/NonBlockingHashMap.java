/**
 * 
 */
package hw8;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Gaurang Davda
 * @param <K>
 * @param <V>
 *
 */
public class NonBlockingHashMap<K, V> {

	private AtomicReference<HashMap<K, V>> map = new AtomicReference<HashMap<K, V>>(new HashMap<K, V>());

	public boolean containsKey(K key) {
		HashMap<K, V> oldMap;
		oldMap = map.get();
		return oldMap.containsKey(key);
	}

	public V get(K key) {
		HashMap<K, V> oldMap;
		oldMap = map.get();
		return oldMap.get(key);
	}

	public Set<K> keySet() {
		HashMap<K, V> oldMap;
		oldMap = map.get();
		return oldMap.keySet();
	}

	public Collection<V> values() {
		HashMap<K, V> oldMap;
		oldMap = map.get();
		return oldMap.values();
	}

	public V remove(K key) {
		HashMap<K, V> oldMap;
		oldMap = map.get();
		return oldMap.remove(key);
	}

	public void put(K key, V value) {
		HashMap<K, V> oldMap;
		HashMap<K, V> newMap;
		do {
			oldMap = map.get();
			newMap = new HashMap<K, V>(oldMap);
			newMap.put(key, value);
		} while (!map.compareAndSet(oldMap, newMap));
	}

}
