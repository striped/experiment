package org.kot.experiment.hash;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 13/09/2011 20:33 $
 * @created 13/09/2011 20:33 by striped
 * @todo add JavaDoc
 */
public class SimpleHash extends Hashable {

    public SimpleHash(final String name, final int volume, final double price) {
        super(name, volume, price);
    }

    @Override
    public final int hashCode() {
        int result = name.hashCode();
        result = 31 * result + volume;
        final long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
