package org.psjava.ds.map.hashmap;

import org.psjava.ds.map.MutableMap;
import org.psjava.ds.map.MutableMapFactory;

public class OpenAddressingHashTableFactory {

	private static final HashProber PROBING = LinearProbing.create();

	private static final MutableMapFactory INSTANCE = new MutableMapFactory() {
		@Override
		public <K, V> MutableMap<K, V> create() {
			return new OpenAddressingHashTable<K, V>(PROBING, 1);
		}
	};

	public static MutableMapFactory getInstance() {
		return INSTANCE;
	}

}
