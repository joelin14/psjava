package org.psjava.geometry.data;

import org.psjava.javautil.EqualityTester;
import org.psjava.javautil.StrictEqualityTester;
import org.psjava.math.ThomasWangHash;

public class Angle implements EqualityTester<Angle> {
	
	public static Angle create(double radian) {
		return new Angle(radian);
	}

	private final double radian;
	
	private Angle(double a) {
		this.radian = a;
	}
	
	public double radian() {
		return radian;
	}

	@Override
	public String toString() {
		return "A"+radian ;
	}
	
	@Override
	public boolean equals(Object o) {
		return StrictEqualityTester.areEqual(this, o, this);
	}

	@Override
	public boolean areEqual(Angle o1, Angle o2) {
		return o1.radian == o2.radian;
	}
	
	@Override
	public int hashCode() {
		return ThomasWangHash.hash64bit(Double.doubleToLongBits(radian));
	}

}
