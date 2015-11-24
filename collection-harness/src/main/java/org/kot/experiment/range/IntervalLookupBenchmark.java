package org.kot.experiment.range;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3)
@Fork(value = 3, jvmArgsAppend = {"-server", "-disablesystemassertions", "-XX:+AggressiveOpts",
                                  "-XX:CompileThreshold=1000", "-XX:Tier3CompileThreshold=2000", "-XX:Tier4CompileThreshold=3000"})
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class IntervalLookupBenchmark {

	@Param({"4", "16", "32", "64", "128", "256"})
	private int size;

	private List<Interval<String>> stringList;

	private List<Interval<Integer>> intList;

	private IntervalTree<String> stringTree;

	private IntervalTree<Integer> intTree;

	private List<String> points;


	@Setup
	public void setup() {
		final int wide = 1000 / size;
		stringList = new ArrayList<>(size);
		intList = new ArrayList<>(size);
		for (int start = 0; start < 1000; start += wide) {
			stringList.add(new Interval<>(String.format("%05d", start), String.format("%05d", start + wide)));
			intList.add(new Interval<>(start, start + wide));
		}
		stringTree = new IntervalTree<>(stringList);
		intTree = new IntervalTree<>(intList);
		points = new ArrayList<>();
		for (int i =0; i < 1000; i++) {
			points.add(String.format("%05d", i));
		}
	}

	@Benchmark
	public void stringList(Blackhole hole) {
		for (String point : points) {
			final List<Interval<String>> result = lookup(stringList, point);
			hole.consume(result);
		}
	}

	@Benchmark
	public void stringTree(Blackhole hole) {
		for (String point : points) {
			final List<Interval<String>> result = stringTree.intersect(point);
			hole.consume(result);
		}
	}

	@Benchmark
	public void intList(Blackhole hole) {
		for (String point : points) {
			int num = parseInt(point);
			final List<Interval<Integer>> result = lookup(intList, num);
			hole.consume(result);
		}
	}

	@Benchmark
	public void intTree(Blackhole hole) {
		for (String point : points) {
			int num = parseInt(point);
			final List<Interval<Integer>> result = intTree.intersect(num);
			hole.consume(result);
		}
	}

	static int parseInt(final String point) {
		int result = 0;
		for (int i = 0; i < point.length(); i++) {
			result *= 10;
			result += Character.digit(point.charAt(i), 10);
		}
		return result;
	}


	static <T extends Comparable<T>> List<Interval<T>> lookup(final List<Interval<T>> list, final T point) {
		for (Interval<T> i : list) {
			if (0 >= i.from().compareTo(point) && 0 < i.to().compareTo(point)) {
				return Collections.singletonList(i);
			}
		}
		return Collections.emptyList();
	}
}
