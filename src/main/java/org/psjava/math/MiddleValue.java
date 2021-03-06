package org.psjava.math;

import org.psjava.math.ns.MultipliableNumberSystem;

public class MiddleValue {

	public static <T> T calc(MultipliableNumberSystem<T> ns, T v1, T v2, T rateOf2) {
		T rateOf1 = ns.subtract(ns.getOne(), rateOf2);
		return ns.add(ns.multiply(v1, rateOf1), ns.multiply(v2, rateOf2));
	}

}
