package org.kot.experiment.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 23/11/2014 19:19
 */
public class DomTransformatorImpl implements Transformator {

	private static final ThreadLocal<Holder> holder = new ThreadLocal<Holder>() {
		@Override
		protected Holder initialValue() {
			return new Holder();
		}
	};

	public void transform(final InputStream input, final Result output) throws IOException, TransformerException {
		try {
			final Document document = holder.get().builder().parse(new InputSource(input));
			removeRedundantElements(0, document.getFirstChild());
			removeEmptyElements(0, document.getFirstChild());
			holder.get().transformer().transform(new DOMSource(document), output);
		} catch (SAXException e) {
			throw new TransformerException(e);
		}
	}

	public String transform(final InputStream input) throws IOException, TransformerException {
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			transform(input, new StreamResult(output));
			return output.toString();
		}
	}

	static void removeRedundantElements(int level, final Node node) {
		if (node instanceof Text) {
			if (node.getNodeValue().startsWith("${")) {
				if (level > 3) {
					final Node element = node.getParentNode();
					element.getParentNode().removeChild(element);
				} else {
					node.setNodeValue("");
				}
			}
			return;
		}
		final NodeList children = node.getChildNodes();
		for (int i = children.getLength(); i-->0; ) {
			removeRedundantElements(level + 1, children.item(i));
		}
	}

	static void removeEmptyElements(int level, final Node node) {
		if (node instanceof Text) {
			if (level >= 3 && node.getNodeValue().trim().isEmpty()) {
				node.getParentNode().removeChild(node);
			}
			return;
		}
		NodeList children = node.getChildNodes();
		for (int i = children.getLength(); i-->0; ) {
			removeEmptyElements(level + 1, children.item(i));
		}
		if (level >= 3 && 0 == node.getChildNodes().getLength()) {
			node.getParentNode().removeChild(node);
		}
	}

	public static void main(String[] args) throws Exception {
		if (0 < args.length && "--help".equalsIgnoreCase(args[0])) {
			System.err.printf("Usage:%n\tjava %s [data]", DomTransformatorImpl.class.getName());
			return;
		}
		final String data = (0 < args.length)? args[0]: "sample.xml";
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		final DomTransformatorImpl app = new DomTransformatorImpl();

		final String ethalon = app.transform(loader.getResourceAsStream(data));
		System.out.println(ethalon);

		final int max = Runtime.getRuntime().availableProcessors();
		final ArrayList<Future<String>> tasks = new ArrayList<>(max);
		final ExecutorService executor = Executors.newFixedThreadPool(max);
		for (int i = 0; i < max; i++) {
			tasks.add(
					executor.submit(
							new Callable<String>() {
								@Override
								public String call() throws Exception {
									return app.transform(loader.getResourceAsStream(data));
								}
							}
					)
			);
		}
		executor.shutdown();

		for (Future<String> task : tasks) {
			final String actual = task.get();
			if (!ethalon.equals(actual)) {
				System.err.println(actual);
			}
		}
	}

	static class Holder {
		private final Transformer transformer;

		private final DocumentBuilder builder;

		Holder() {
			try {
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			} catch (ParserConfigurationException | TransformerConfigurationException e) {
				throw new RuntimeException(e);
			}
		}

		public Transformer transformer() {
			return transformer;
		}

		public DocumentBuilder builder() {
			return builder;
		}
	}

}
