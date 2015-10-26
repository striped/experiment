package org.kot.experiment.table;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 25/10/2015 15:22
 */
class PairTableArray implements PairTable {

	final String[] pairs;

	final Bucket[] buckets;

	final Comparator<String> comparator;

	PairTableArray(final String[] pairs, Bucket.Factory bucketFactory) {
		Arrays.sort(pairs);
		this.pairs = pairs;
		this.buckets = new Bucket[pairs.length];
		for (int p = 0; p < pairs.length; p++) {
			buckets[p] = bucketFactory.newInstance(pairs[p]);
		}
		this.comparator = new FastStringComparator(pairs);
	}

	@Override
	public void consume(String ccy, double rate) {
		final int idx = Arrays.binarySearch(pairs, ccy, comparator);
		buckets[idx].consume(rate);
	}

	static class FastStringComparator implements Comparator<String> {

		private final int depth;

		public FastStringComparator(final String[] pairs) {
			this.depth = findDepth(pairs);
		}

		@Override
		public int compare(final String o1, final String o2) {
			int result = o1.charAt(0) - o2.charAt(0);
			for (int i = 0; i <= depth && 0 == result; i++) {
				result = o1.charAt(i) - o2.charAt(i);
			}
			return result;
		}

		private int findDepth(final String[] pairs) {
			int depth = 0;
			for (int p = 1; p < pairs.length; p++) {
				if (depth >= pairs[p].length()) {
					throw new IllegalArgumentException("The " + pairs[p] + " has length less than needed for fast comparison");
				}
				int d = 0;
				for (; pairs[p].charAt(d) == pairs[p - 1].charAt(d); d++) {}
				if (depth < d) {
					depth = d;
				}
			}
			return depth;
		}
	}
}
