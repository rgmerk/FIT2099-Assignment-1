/*
 * Change log 
 * 2017-01-20:	Paragraph tags to the java doc (asel)
 */
package edu.monash.fit2099.simulator.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Bidirectional multimap.  Maps keys (K) onto values (V).  The keys must be unique but the 
 * values need not be.  Both the Apache BidiMap and the Guava Bimap require unique keys and
 * values. 
 * <p>
 * Internally, this is done by maintaining two HashMaps: one mapping K onto V, the other
 * mapping V onto Lists of K.  This means that insertion and deletion code needs to be aware that
 * an extra step may be needed to maintain consistency between the two private maps.
 * <p>
 * Implements the Map<K,V> interface, largely by acting as a wrapper for the forward
 * version of the map.
 * 
 * @author 	Robyn
 *
 * @param 	<K> key type
 * @param 	<V> value type
 * 
 * @date 	1 March 2013
 */

// TODO: add some exception handling for the love of heaven!

public class BiMultiMap<K,V> implements Map<K,V> {
	private Map<K,V> kToV;
	private Map<V, List<K>> vToK;
	
	/**
	 * Default constructor.  Instantiates an empty <code>BiMultiMap</code>.
	 * 
	 */
	public BiMultiMap() {
		kToV = new HashMap<K,V>();
		vToK = new HashMap<V, List<K>>();
	}
	
	/**
	 * Adds a new K -> V association to the <code>BiMultiMap</code>.
	 * <p>
	 * If newKey already exists in the <code>BiMultiMap</code>, mapping (say) onto oldVal,
	 * then the reverse mapping oldVal -> newKey is removed and replaced by the 
	 * reverse mapping newVal -> newKey.
	 * 
	 * @param newKey key of the new association
	 * @param newVal value of the new association
	 */
	public V put(K newKey, V newVal) {
		if (kToV.containsKey(newKey))
		{
			// remove current reverse association
			V oldVal = kToV.get(newKey);
			List<K> oldAssoc = vToK.get(oldVal);
			oldAssoc.remove(newKey);
			
			// handle removal of last mapping to this value
			if (oldAssoc.isEmpty())
				vToK.remove(oldVal);
		}
		kToV.put(newKey, newVal);
		
		// insert reverse mapping
		if (vToK.containsKey(newVal))
			vToK.get(newVal).add(newKey);
		else {
			// value already exists, so add new key to its list of inverses
			ArrayList<K> newList = new ArrayList<K>();
			newList.add(newKey);
			vToK.put(newVal, newList);
		}
		
		return newVal;
	}
	
	/**
	 * Returns the key that maps onto a particular value.
	 * <p>
	 * @param 	target the value to look up
	 * @return 	the key that maps onto target, if such a mapping exists, otherwise null
	 */
	public ArrayList<K> reverseLookup(V target) {
		if (vToK.containsKey(target)) {
			// ArrayList doesn't implement Cloneable.
			// Note: this is a new list, but it contains the same references as the old list.
			// Client code can still munge the Ks and Vs themselves, but it can't munge the BiMultiMap.
			ArrayList<K> newList = new ArrayList<K>();
			for (K k: vToK.get(target)) {
				newList.add(k);
			}
			return newList;
		}
		return null;
	}

	@Override
	/**
	 * @see Map.clear()
	 */
	public void clear() {
		kToV.clear();
		vToK.clear();
		
	}

	@Override
	/**
	 * @see Map.containsValue()
	 */
	public boolean containsValue(Object value) {
		return vToK.containsKey(value);
	}

	@Override
	/**
	 * @see Map.entrySet()
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return kToV.entrySet();
	}

	@Override
	/**
	 * @see Map.get()
	 */
	public V get(Object key) {
		return kToV.get(key);
	}

	@Override
	/**
	 * @see Map.isEmpty()
	 */
	public boolean isEmpty() {
		return kToV.isEmpty();
	}

	@Override
	/**
	 * @see Map.keySet()
	 */
	public Set<K> keySet() {
		return kToV.keySet();
	}

	@Override
	/**
	 * @see Map.putAll()
	 */
	public void putAll(Map<? extends K, ? extends V> m) {
		for (K key : m.keySet()) {
			this.put(key, m.get(key));
		}
	}

	@Override
	/**
	 * @see Map.remove()
	 */
	public V remove(Object key) {
		V val = kToV.remove(key);
		vToK.get(val).remove(key);
		return val;
	}

	@Override
	/**
	 * @see Map.size()
	 */
	public int size() {
		return kToV.size();
	}

	@Override
	/**
	 * @see Map.values()
	 */
	public Collection<V> values() {
		return vToK.keySet();
	}

	@Override
	/**
	 * @see Map.containsKey()
	 */
	public boolean containsKey(Object key) {
		return kToV.containsKey(key);
	}
}
