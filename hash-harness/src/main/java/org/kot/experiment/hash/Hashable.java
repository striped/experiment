package org.kot.experiment.hash;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 13/09/2011 19:48 $
 * @created 13/09/2011 19:48 by striped
 * @todo add JavaDoc
 */
abstract class Hashable {
    final String name;
    int volume;
    double price;

    public Hashable(final String name, final int volume, final double price) {
        assert name != null: "Name should be specified";
        this.name = name;
        this.volume = volume;
        this.price = price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hashable)) {
            return false;
        }
        final Hashable that = (Hashable) o;
        return name.equals(that.name) && volume == that.volume && 0 == Double.compare(price, that.price);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(name);
        result.append('@').append(price).append('{').append(volume).append('}');
        return result.toString();
    }
}
