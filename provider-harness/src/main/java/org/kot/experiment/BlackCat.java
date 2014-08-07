package org.kot.experiment;

/**
 * Implementation of object to be targeted by proxy.
 */
public class BlackCat implements Cat {

	final String value = "I said meow!";

	/**
	 * Returns recalculated each time hash code of {@code meow} word
	 * @return The hash code of {@code meow} word
	 */
	@Override
	public int sayMeow() {
		int hash = 0;
		for (int i = 0; i < value.length(); i++) {
			hash = 31 * hash + value.charAt(i);
		}
		return hash;
	}
}
