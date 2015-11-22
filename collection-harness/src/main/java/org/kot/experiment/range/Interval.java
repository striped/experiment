package org.kot.experiment.range;

/**
 * Interval representation.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 22/11/2015 15:48
 * @param <P> The point representation on imagine dimension
 */
public class Interval<P extends Comparable<P>> implements Comparable<Interval<P>> {

	private final P from;

	private final P to;

	/**
	 * Construct interval [{@code from}..{@code to})
	 * @param from The interval point it starts from, inclusive
	 * @param to The point where interval finish, exclusive
	 */
	public Interval(final P from, final P to) {
		assert null != from: "Interval start must be defined";
		assert null != to: "Interval end must be defined";

		this.from = from;
		this.to = to;
	}

	/**
	 * Start point of interval
	 * @return the start of interval, inclusive
	 */
	public P from() {
		return from;
	}

	/**
	 * Point where interval finished
	 * @return interval end point, exclusive
	 */
	public P to() {
		return to;
	}

	/**
	 * Accomplish natural order sorting for interval collection employing natural order of start and end points of this interval relative another.
	 * @param other The another interval compare with
	 * @return returns {@code negative int} if this interval start before or finish earlier, {@code 0} - if they are the same and {@code positive int} - otherwise.
	 */
	@Override
	public int compareTo(final Interval<P> other) {
		int res = from.compareTo(other.from);
		return 0 != res? res: to.compareTo(other.to);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Interval) {
			return false;
		}
		final Interval<P> interval = (Interval<P>) o;
		return from.equals(interval.from) && to.equals(interval.to);
	}

	@Override
	public int hashCode() {
		int result = from.hashCode();
		result = 31 * result + to.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "[" + from + ".." + to + ')';
	}
}
