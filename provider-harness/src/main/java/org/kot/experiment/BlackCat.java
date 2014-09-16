package org.kot.experiment;

import org.openjdk.jmh.infra.Blackhole;

/**
 * Implementation of object to be targeted by proxy.
 */
public class BlackCat implements Cat {

	/**
	 * Returns recalculated each time hash code of {@code meow} word
	 * @return The hash code of {@code meow} word
	 * @param i
	 */
	@Override
	public int sayMeow(final int i) {
		Blackhole.consumeCPU(i);
		return i;
	}
}
