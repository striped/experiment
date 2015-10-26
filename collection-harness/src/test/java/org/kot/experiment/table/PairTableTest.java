package org.kot.experiment.table;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 25/10/2015 15:48
 */
@RunWith(Parameterized.class)
public class PairTableTest {

	private final String[] pairs;

	private final Map<String, Integer> depthMap;

	public PairTableTest(final List<String> pairs) {
		this.pairs = pairs.toArray(new String[pairs.size()]);
		this.depthMap = new HashMap<>(pairs.size(), 1f);
	}

	@Parameters(name = "{index}: Check {0}")
	public static Iterable<Object[]> arguments() {
		return Arrays.asList(new Object[][] {
				{Arrays.asList("EUR/USD", "JPY/USD", "GBP/USD", "USD/CHF", "AUD/USD", "CAD/USD", "NZD/USD")},
				{Arrays.asList("EUR/USD", "JPY/USD", "GBP/USD", "USD/CHF", "AUD/USD", "CAD/USD", "NZD/USD", "CNH/USD")},
				{Arrays.asList("EUR/USD", "JPY/USD", "GBP/USD", "USD/CHF", "AUD/USD", "CAD/USD", "NZD/USD", "CNH/USD", "CND/USD")},
				{Arrays.asList("EUR/USD", "JPY/USD", "GBP/USD", "USD/CHF", "AUD/USD", "CAD/USD", "NZD/USD", "CNH/USD", "USD/CAD")}
		});
	}

	@Test
	public void testMap() {
		final PairTable table = new PairTableMap(pairs, new FakeBucketFactory());
		for (String pair : pairs) {
			table.consume(pair, 1);
		}

		for (int depth : depthMap.values()) {
			assertThat(depth, is(1));
		}
	}

	@Test
	public void testArray() {
		final PairTable table = new PairTableArray(pairs, new FakeBucketFactory());
		for (String pair : pairs) {
			table.consume(pair, 1);
		}

		for (int depth : depthMap.values()) {
			assertThat(depth, is(1));
		}
	}

	class FakeBucketFactory implements Bucket.Factory {

		@Override
		public Bucket newInstance(final String pair) {
			depthMap.put(pair, 0);
			return new Bucket() {
				@Override
				public void consume(final double rate) {
					final int value = depthMap.get(pair);
					depthMap.put(pair, value + 1);
				}
			};
		}
	}
}
