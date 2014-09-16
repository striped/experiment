package org.kot.experiment;

import org.kot.experiment.proxy.CGLibProxyFactory;
import org.kot.experiment.proxy.DynamicProxyFactory;
import org.kot.experiment.proxy.HibernateProxyFactory;
import org.kot.experiment.proxy.JavassistProxyFactory;
import org.kot.experiment.proxy.SpringAOPProxyFactory;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 *

 "I said meow!" (jdk7)

 Benchmark                          Mode  Samples   Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30  10.157        0.215  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  10.914        0.368  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  11.460        0.208  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  18.187        0.694  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  28.782        0.353  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  39.028        0.770  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  63.782        1.359  ns/op

 Benchmark                          Mode  Samples   Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30   9.762        0.047  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  10.268        0.035  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  10.643        0.050  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  16.760        0.136  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  25.991        0.167  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  27.275        0.150  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  53.929        0.423  ns/op

 "I said meow!" * 10 (jdk1.7.0_65)
 Benchmark                          Mode  Samples    Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30  136.202        0.687  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  136.969        0.698  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  136.929        0.477  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  144.535        0.534  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  156.901        0.840  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  164.529        0.824  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  188.556        0.914  ns/op

 Benchmark                          Mode  Samples    Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30  137.933        0.417  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  138.760        0.508  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  139.164        0.389  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  147.382        0.655  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  157.001        0.828  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  158.625        0.759  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  184.689        0.988  ns/op

 CRC32("I said meow!" * 10)
 Benchmark                          Mode  Samples    Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30  326.772        1.873  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  327.405        1.858  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  330.734        2.083  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  344.653        2.776  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  357.309        2.478  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  358.514        1.893  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  391.610        2.017  ns/op

 Benchmark                          Mode  Samples    Score  Score error  Units
 o.k.e.CatBenchmark.t1_direct       avgt       30  207.325        1.435  ns/op
 o.k.e.CatBenchmark.t2_javassist    avgt       30  206.799        1.581  ns/op
 o.k.e.CatBenchmark.t3_cglib        avgt       30  208.403        1.069  ns/op
 o.k.e.CatBenchmark.t4_dynamic      avgt       30  217.890        1.144  ns/op
 o.k.e.CatBenchmark.t5_hibernate    avgt       30  233.030        2.577  ns/op
 o.k.e.CatBenchmark.t6_springOpt    avgt       30  231.929        1.956  ns/op
 o.k.e.CatBenchmark.t7_spring       avgt       30  259.991        2.291  ns/op


 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(3)
@State(Scope.Benchmark)
public class CatBenchmark {

	private Cat direct = new BlackCat();

	private Cat dynamic = new DynamicProxyFactory().<Cat>newProxy(new BlackCat());

	private Cat javassist = new JavassistProxyFactory().newProxy(new BlackCat());

	private Cat cglib = new CGLibProxyFactory().newProxy(new BlackCat());

	private Cat spring = new SpringAOPProxyFactory(false).newProxy(new BlackCat());

	private Cat springCGLib = new SpringAOPProxyFactory(true).newProxy(new BlackCat());

	private Cat hibernate = new HibernateProxyFactory().newProxy(new BlackCat());

	@Param({"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
	        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39"})
	private int token;

	@Benchmark
    public long t1_direct() {
		return direct.sayMeow(token);
    }

	@Benchmark
	public long t2_javassist() {
		return javassist.sayMeow(token);
	}

	@Benchmark
	public long t3_cglib() {
		return cglib.sayMeow(token);
	}

	@Benchmark
	public long t4_dynamic() {
		return dynamic.sayMeow(token);
	}

	@Benchmark
	public long t5_hibernate() {
		return hibernate.sayMeow(token);
	}

	@Benchmark
	public long t6_springOpt() {
		return springCGLib.sayMeow(token);
	}

	@Benchmark
	public long t7_spring() {
		return spring.sayMeow(token);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + CatBenchmark.class.getSimpleName() + ".*")
//				.verbosity(VerboseMode.EXTRA)
				.build();

	    new Runner(opt).run();
	}

}
