package org.kot.experiment.range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * Special structure for fast intersection interval with point.
 *
 * Storing intervals
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 22/11/2015 14:49
 */
public class IntervalTree<P extends Comparable<P>> {

	private final TreeMap<P, List<Interval<P>>> map;

	@SafeVarargs
	public IntervalTree(Interval<P>... intervals) {
		map = new TreeMap<>();
		for (Interval<P> i : intervals) {
			add(i);
		}
		for (List<Interval<P>> section : map.values()) {
			Collections.sort(section);
		}
	}

	public IntervalTree(Collection<Interval<P>> intervals) {
		map = new TreeMap<>();
		for (Interval<P> i : intervals) {
			add(i);
		}
		for (List<Interval<P>> section : map.values()) {
			Collections.sort(section);
		}
	}

	public List<Interval<P>> intersect(final P point) {
		final Map.Entry<P, List<Interval<P>>> entry = map.floorEntry(point);
		if (null == entry) {
			return Collections.emptyList();
		}
		return entry.getValue();
	}

	public Collection<Interval<P>> all() {
		final Set<Interval<P>> result = new HashSet<>();
		for (List<Interval<P>> is : map.values()) {
			result.addAll(is);
		}
		return result;
	}

	void add(final Interval<P> range) {
		assert null != range: "Value must be defined";

		final P from = range.from();
		final P to = range.to();
		final List<Interval<P>> old = look(to);
		map.put(from, look(from));
		final NavigableMap<P, List<Interval<P>>> between = map.subMap(from, true, to, false);
		for (P point : between.keySet()) {
			map.get(point).add(range);
		}
		map.put(to, old);
	}

	List<Interval<P>> look(final P point) {
		final Map.Entry<P, List<Interval<P>>> entry = map.floorEntry(point);
		if (null == entry) {
			return new ArrayList<>();
		}
		return new ArrayList<>(entry.getValue());
	}

}
