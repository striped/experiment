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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3)
@Fork(value = 3, jvmArgsAppend = {"-server", "-disablesystemassertions", "-XX:+AggressiveOpts",
                                  "-XX:CompileThreshold=1000", "-XX:Tier3CompileThreshold=2000", "-XX:Tier4CompileThreshold=3000"})
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class IntervalLookupBenchmark {

	@Param({"4", "16", "32", "64", "128", "256"})
	private int size;

	private List<Interval<String>> list;

	private IntervalTree<String> tree;

	private List<String> points;


	@Setup
	public void setup() {
		final int wide = 1000 / size;
		list = new ArrayList<>(size);
		for (int start = 0; start < 1000; start += wide) {
			list.add(new Interval<>(String.format("%05d", start), String.format("%05d", start + wide)));
		}
		tree = new IntervalTree<>(list);
		points = new ArrayList<>();
		for (int i =0; i < 1000; i++) {
			points.add(String.format("%05d", i));
		}
	}

	@Benchmark
	public void list(Blackhole hole) {
		for (String point : points) {
			final List<Interval<String>> result = lookup(list, point);
			hole.consume(result);
		}
	}

	@Benchmark
	public void tree(Blackhole hole) {
		for (String point : points) {
			final List<Interval<String>> result = tree.intersect(point);
			hole.consume(result);
		}
	}


	static List<Interval<String>> lookup(final List<Interval<String>> list, final String point) {
		for (Interval<String> i : list) {
			if (0 >= i.from().compareTo(point) && 0 < i.to().compareTo(point)) {
				return Collections.singletonList(i);
			}
		}
		return Collections.emptyList();
	}
}
