package org.kot.experiment.range;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private final List<Interval<String>> stringList;

	private final List<Interval<Integer>> intList;

	private final IntervalTree<String> stringTree;

	private final IntervalTree<Integer> intTree;

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
		stringList = new ArrayList<>(size);
		intList = new ArrayList<>(size);
		int start = 0;
		for (; start < 1000; start += wide) {
			stringList.add(new Interval<>(String.format("%05d", start), String.format("%05d", start + wide)));
			intList.add(new Interval<>(start, start + wide));
		}
		stringTree = new IntervalTree<>(stringList);
		intTree = new IntervalTree<>(intList);

	}

	@Test
	public void testList() {
		for (int i = 0; i < 1000; i++) {
			final List<Interval<String>> result = IntervalLookupBenchmark.lookup(stringList, String.format("%05d", i));
			assertThat(result, Matchers.<Interval<String>>iterableWithSize(1));
		}
	}

	@Test
	public void testTree() {
		for (int i = 0; i < 1000; i++) {
			final List<Interval<String>> result = stringTree.intersect(String.format("%05d", i));
			assertThat(result, Matchers.<Interval<String>>iterableWithSize(1));
		}
	}

	@Test
	public void testIntList() {
		for (int i = 0; i < 1000; i++) {
			int num = IntervalLookupBenchmark.parseInt(String.format("%05d", i));
			final List<Interval<Integer>> result = IntervalLookupBenchmark.lookup(intList, num);
			assertThat(result, Matchers.<Interval<Integer>>iterableWithSize(1));
		}
	}

	@Test
	public void testIntTree() {
		for (int i = 0; i < 1000; i++) {
			int num = IntervalLookupBenchmark.parseInt(String.format("%05d", i));
			final List<Interval<Integer>> result = intTree.intersect(num);
			assertThat(result, Matchers.<Interval<Integer>>iterableWithSize(1));
		}
	}
}
