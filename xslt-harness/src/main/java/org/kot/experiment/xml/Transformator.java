package org.kot.experiment.xml;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 23/11/2014 22:21
 */
public interface Transformator {

	void transform(InputStream input, Result output) throws TransformerException, IOException;

	String transform(InputStream input) throws TransformerException, IOException;
}
