package org.kot.experiment.range;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

/**
 * Test suite for testing the interval tree.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 18/11/2015 21:01
 */
@RunWith(Parameterized.class)
public class IntervalTreeTest {

	private static IntervalTree<Integer> object;

	private final Interval<Integer> range;

	private final Integer point;

	@Parameters(name = "{index}: Check that point ({1}) belongs to {0}")
	public static Iterable<Object[]> args() {
		object = new IntervalTree<>(
				new Interval<>(1, 100),
				new Interval<>(1, 12),
				new Interval<>(6, 51),
				new Interval<>(45, 55),
				new Interval<>(90, 110),
				new Interval<>(200, 250),
				new Interval<>(250, 300)
		);

		final List<Object[]> args = new ArrayList<>();
		for (Interval<Integer> i : object.all()) {
			args.add(new Object[] {i, i.from()});
			args.add(new Object[] {i, (i.from() + i.to()) / 2});
			args.add(new Object[] {i, i.to() - 1});
		}
		return args;
	}

	public IntervalTreeTest(Interval<Integer> range, Integer point) {
		this.range = range;
		this.point = point;
	}

	@Test
	public void test() {
		final List<Interval<Integer>> list = object.intersect(point);
		assertThat(list, hasItem(range));
	}

}
