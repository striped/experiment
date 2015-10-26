package org.kot.experiment.table;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 40)
@Measurement(iterations = 10)
@Fork(value = 3, jvmArgsAppend = {
		"-server", "-disablesystemassertions", "-XX:+AggressiveOpts", "-XX:+AlwaysCompileLoopMethods", "-XX:-DontCompileHugeMethods"
})
@Threads(1)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ConstantVsLogarithmicBenchmark implements Bucket.Factory {

	private static final String[] ccys = {
			"AUD/USD",
			"CHF/USD",
			"EUR/USD",
			"GBP/USD",
			"JPY/USD",
			"NZD/USD",
			"UAH/USD",
			"SGD/JPY",
			"CAD/USD"
	};

	private static final byte[][] data = new byte[ccys.length][];

	static {
		for (int c = 0; c < ccys.length; c++) {
			data[c] = ccys[c].getBytes();
		}
	}

	@Param({"1", "3", "5", "9"})
	int size;

	PairTableMap mapTable;

	PairTableArray arrayTable;

	@Setup
	public void setup() {
		mapTable = new PairTableMap(Arrays.copyOf(ccys, size), ConstantVsLogarithmicBenchmark.this);
		arrayTable = new PairTableArray(Arrays.copyOf(ccys, size), ConstantVsLogarithmicBenchmark.this);
	}

	@Benchmark
	@OperationsPerInvocation(8)
	public void forMap() {
		for (int i = 0; i < 8; i++) {
			final String ccy = new String(data[i % size]);
			mapTable.consume(ccy, 1);
		}
	}

	@Benchmark
	@OperationsPerInvocation(8)
	public void forArray() {
		for (int i = 0; i < 8; i++) {
			final String ccy = new String(data[i % size]);
			arrayTable.consume(ccy, 1);
		}
	}

	@Override
	public Bucket newInstance(final String pair) {
		return new TestBucket();
	}

	static class TestBucket implements Bucket {

		@Override
		public void consume(final double rate) {
			//synchronized (this) {
				sink(rate);
			//}
		}

		@CompilerControl(CompilerControl.Mode.DONT_INLINE)
		private void sink(double rate) {
			//
		}
	}
}
