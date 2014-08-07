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
 Benchmark                                 Mode   Samples         Mean   Mean error    Units
 o.k.t.CatBenchmark.cglibDelegator         avgt        15        2.175        0.043    ns/op
 o.k.t.CatBenchmark.direct                 avgt        15        1.070        0.001    ns/op
 o.k.t.CatBenchmark.dynamic                avgt        15        5.802        0.031    ns/op
 o.k.t.CatBenchmark.javassistDelegator     avgt        15        1.627        0.017    ns/op
 o.k.t.CatBenchmark.javassistPoor          avgt        15        6.721        0.046    ns/op

 Benchmark                             Mode   Samples         Mean   Mean error    Units
 o.k.t.p.CatBenchmark.t1_direct        avgt        30        1.169        0.002    ns/op
 o.k.t.p.CatBenchmark.t2_javassist     avgt        30        1.503        0.039    ns/op
 o.k.t.p.CatBenchmark.t3_cglib         avgt        30        2.050        0.039    ns/op
 o.k.t.p.CatBenchmark.t4_dynamic       avgt        30       15.067        0.119    ns/op
 o.k.t.p.CatBenchmark.t5_hibernate     avgt        30       17.628        0.261    ns/op
 o.k.t.p.CatBenchmark.t6_springOpt     avgt        30       28.649        0.252    ns/op
 o.k.t.p.CatBenchmark.t7_spring        avgt        30       53.470        0.618    ns/op

 Benchmark                             Mode   Samples        Score  Score error    Units
 o.k.t.p.CatBenchmark.t1_direct        avgt        30        9.762        0.034    ns/op
 o.k.t.p.CatBenchmark.t2_javassist     avgt        30       10.282        0.037    ns/op
 o.k.t.p.CatBenchmark.t3_cglib         avgt        30       10.660        0.039    ns/op
 o.k.t.p.CatBenchmark.t4_dynamic       avgt        30       16.790        0.103    ns/op
 o.k.t.p.CatBenchmark.t5_hibernate     avgt        30       26.038        0.169    ns/op
 o.k.t.p.CatBenchmark.t6_springOpt     avgt        30       28.708        0.394    ns/op
 o.k.t.p.CatBenchmark.t7_spring        avgt        30       54.363        0.396    ns/op

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

    @Benchmark
    public long t1_direct() {
        return direct.sayMeow();
    }

	@Benchmark
	public long t2_javassist() {
		return javassist.sayMeow();
	}

	@Benchmark
	public long t3_cglib() {
		return cglib.sayMeow();
	}

	@Benchmark
	public long t4_dynamic() {
		return dynamic.sayMeow();
	}

	@Benchmark
	public long t5_hibernate() {
		return hibernate.sayMeow();
	}

	@Benchmark
	public long t6_springOpt() {
		return springCGLib.sayMeow();
	}

	@Benchmark
	public long t7_spring() {
		return spring.sayMeow();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + CatBenchmark.class.getSimpleName() + ".*")
//				.verbosity(VerboseMode.EXTRA)
				.build();

	    new Runner(opt).run();
	}

}
