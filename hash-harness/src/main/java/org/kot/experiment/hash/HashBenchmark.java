package org.kot.experiment.hash;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 05/08/2014 21:58

Benchmark                          Mode  Samples  Score  Score error  Units
o.k.e.h.HashBenchmark.t1_simple    avgt       30  3.082        0.059  ns/op
o.k.e.h.HashBenchmark.t2_tuned     avgt       30  3.272        0.075  ns/op
o.k.e.h.HashBenchmark.t3_fast      avgt       30  0.922        0.028  ns/op

Benchmark                          Mode  Samples  Score  Score error  Units
o.k.e.h.HashBenchmark.t1_simple    avgt       30  3.070        0.064  ns/op
o.k.e.h.HashBenchmark.t2_tuned     avgt       30  3.252        0.041  ns/op
o.k.e.h.HashBenchmark.t3_fast      avgt       30  0.928        0.028  ns/op

 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(3)
@State(Scope.Benchmark)
public class HashBenchmark {

	private SimpleHash simpleHash = new SimpleHash("NAME", 1000, 1.0d);

	private TunedHash tunedHash = new TunedHash("NAME", 1000, 1.0d);

	private FastHash fastHash = new FastHash("NAME", 1000, 1.0d);

	@Benchmark
	public long t1_simple() {
		return simpleHash.hashCode();
	}

	@Benchmark
	public long t2_tuned() {
		return tunedHash.hashCode();
	}

	@Benchmark
	public long t3_fast() {
		return fastHash.hashCode();
	}

}
