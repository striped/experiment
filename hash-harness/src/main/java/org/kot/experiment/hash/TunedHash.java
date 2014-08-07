package org.kot.experiment.hash;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 13/09/2011 20:43 $
 * @created 13/09/2011 20:43 by striped
 * @todo add JavaDoc
 */
public class TunedHash extends Hashable {

    public TunedHash(final String name, final int volume, final double price) {
        super(name, volume, price);
    }

    @Override
    public final int hashCode() {
        int result = name.hashCode();
        result = (result << 5) - result + volume;
        long temp = Double.doubleToLongBits(price);
        result = (result << 5) - result + ((int) temp ^ (int) (temp >>> 32));
        return result;
    }
}
