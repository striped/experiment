package org.kot.experiment.hash;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 13/09/2011 20:51 $
 * @created 13/09/2011 20:51 by striped
 * @todo add JavaDoc
 */
public class FastHash extends Hashable {
    private final transient int hash;

    public FastHash(final String name, final int volume, final double price) {
        super(name, volume, price);
        hash = calcHash();
    }

    @Override
    public final int hashCode() {
        return hash;
    }

    private int calcHash() {
        int result = name.hashCode();
        result = 31 * result + volume;
        final long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
