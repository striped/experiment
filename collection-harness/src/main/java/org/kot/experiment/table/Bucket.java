package org.kot.experiment.table;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 25/10/2015 15:25
 */
public interface Bucket {

	void consume(double rate);

	interface Factory {

		Bucket newInstance(String pair);
	}
}
