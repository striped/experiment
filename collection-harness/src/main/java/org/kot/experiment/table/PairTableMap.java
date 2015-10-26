package org.kot.experiment.table;

import java.util.HashMap;
import java.util.Map;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 25/10/2015 16:30
 */
class PairTableMap implements PairTable {

	private final Map<String, Bucket> map;

	public PairTableMap(final String[] pairs, Bucket.Factory bucketFactory) {
		map = new HashMap<>(pairs.length, 1f);
		for (String pair : pairs) {
			map.put(pair, bucketFactory.newInstance(pair));
		}

	}

	@Override
	public void consume(final String ccy, final double rate) {
		map.get(ccy).consume(rate);
	}
}
