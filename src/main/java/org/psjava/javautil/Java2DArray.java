package org.psjava.javautil;

public class Java2DArray {

	@SuppressWarnings("unchecked")
	public static <W> W[][] create(Class<?> clazz, int n, int m) {
		return (W[][]) java.lang.reflect.Array.newInstance(clazz, n, m);
	}

}
