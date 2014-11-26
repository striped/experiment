package org.kot.experiment.xml;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 24/11/2014 20:20
 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(3)
public class TransformBenchmark {

	@State(Scope.Thread)
	public static class Data {
		URL resource = Thread.currentThread().getContextClassLoader().getResource("sample.xml");
	}

	@State(Scope.Benchmark)
	public static class TransformatorHolder {
		Transformator dom = new DomTransformatorImpl();

		Transformator xslt = new XsltTransformatorImpl();

	}

/*
	@Benchmark
	@Threads(1)
	public String t1_dom(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.dom.transform(data.resource.openStream());
	}
*/

	@Benchmark
	@Threads(1)
	public String t1_xslt(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.xslt.transform(data.resource.openStream());
	}

	@Benchmark
	@Threads(2)
    public String t2_dom(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.dom.transform(data.resource.openStream());
    }

	@Benchmark
	@Threads(2)
    public String t2_xslt(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.xslt.transform(data.resource.openStream());
    }

	@Benchmark
	@Threads(4)
    public String t4_dom(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.dom.transform(data.resource.openStream());
    }

	@Benchmark
	@Threads(4)
    public String t4_xslt(TransformatorHolder state, Data data) throws TransformerException, IOException {
		return state.xslt.transform(data.resource.openStream());
    }

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + TransformBenchmark.class.getSimpleName() + ".*")
				.build();

	    new Runner(opt).run();
	}
}
