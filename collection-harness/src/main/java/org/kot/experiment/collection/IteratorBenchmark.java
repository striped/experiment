package org.kot.experiment.collection;

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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 30, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3)
@Fork(value = 3, jvmArgsAppend = {"-server", "-disablesystemassertions", "-XX:+AggressiveOpts", "-XX:+AlwaysCompileLoopMethods", "-XX:-DontCompileHugeMethods",
                                  "-XX:CompileThreshold=1000", "-XX:Tier3CompileThreshold=2000", "-XX:Tier4CompileThreshold=3000"})
@BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IteratorBenchmark {

	@Param({"1", "4", "8", "16", "64"})
	int size;

	Map<String, String> map;


	@Setup
	public void setup() {
		map = new HashMap<>(size, 1f);
		for (int c = 0; c < size; c++) {
			map.put(String.valueOf(c), String.valueOf(c));
		}
	}

	@Benchmark
	public void iterateOverEntry(Blackhole hole) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			hole.consume(entry);
		}
	}

	@Benchmark
	public void iterateOverKey(Blackhole hole) {
		for (String key : map.keySet()) {
			hole.consume(map.get(key));
		}
	}

}
