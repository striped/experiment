package org.kot.experiment.range;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 22/11/2015 19:15
 */
@RunWith(Parameterized.class)
public class IntervalLookupBenchmarkTest {

	private final int size;

	private final List<Interval<String>> list;

	private final IntervalTree<String> tree;

	@Parameterized.Parameters(name = "{index}: Check {0}")
	public static Iterable<Object[]> args() {
		return Arrays.asList(new Object[][] {
				{4},
				{16},
				{32},
				{64},
				{128},
				{256}
		});
	}

	public IntervalLookupBenchmarkTest(int size) {
		this.size = size;
		final int wide = 1000 / size;
		list = new ArrayList<>(size);
		int start = 0;
		for (; start < 1000; start += wide) {
			list.add(new Interval<>(String.format("%05d", start), String.format("%05d", start + wide)));
		}
		tree = new IntervalTree<>(list);

	}

	@Test
	public void testList() {
		for (int i = 0; i < 1000; i++) {
			final List<Interval<String>> result = IntervalLookupBenchmark.lookup(list, String.format("%05d", i));
			assertThat(result, Matchers.<Interval<String>>iterableWithSize(1));
		}
	}

	@Test
	public void testTree() {
		for (int i = 0; i < 1000; i++) {
			final List<Interval<String>> result = tree.intersect(String.format("%05d", i));
			assertThat(result, Matchers.<Interval<String>>iterableWithSize(1));
		}
	}
}
