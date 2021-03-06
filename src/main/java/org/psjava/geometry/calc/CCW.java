package org.psjava.geometry.calc;

import org.psjava.geometry.data.Point2D;
import org.psjava.math.SumVarargs;
import org.psjava.math.ns.MultipliableNumberSystem;

public class CCW {

	@SuppressWarnings("unchecked")
	public static <T> T ccw(MultipliableNumberSystem<T> ns, Point2D<T> p1, Point2D<T> p2, Point2D<T> p3) {
		T a1 = ns.multiply(p1.x(), p2.y());
		T a2 = ns.multiply(p2.x(), p3.y());
		T a3 = ns.multiply(p3.x(), p1.y());
		T m1 = ns.multiply(p1.x(), p3.y());
		T m2 = ns.multiply(p2.x(), p1.y());
		T m3 = ns.multiply(p3.x(), p2.y());
		T a = SumVarargs.calc(ns, a1, a2, a3);
		T s = SumVarargs.calc(ns, m1, m2, m3);
		return ns.subtract(a, s);
	}

}
