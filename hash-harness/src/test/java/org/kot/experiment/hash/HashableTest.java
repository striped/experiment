package org.kot.experiment.hash;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 14/09/2011 20:53 $
 * @created 14/09/2011 20:53 by striped
 * @todo add JavaDoc
 */
public abstract class HashableTest {
    Hashable object;

    @Test
    public void testValidity() {
        assertThat(object.name, notNullValue());
    }

    @Test
    public void testToString() {
        assertThat(object, hasToString(containsString(object.name)));
        assertThat(object, hasToString(containsString(Integer.toString(object.volume))));
        assertThat(object, hasToString(containsString(Double.toString(object.price))));
    }

    @Test
    public void testEquals() {
        assertThat(object, equalTo(object));
        assertThat(object, not(equalTo(null)));
        assertThat(object, not(equalTo(new Object())));
    }

    @Test
    public void testHashCode() {
        assertThat(object.hashCode(), is(expectedHash()));
    }

    HashableTest() {
    }

    int expectedHash() {
        int result = object.name.hashCode();
        result = 31 * result + object.volume;
        final long temp = Double.doubleToLongBits(object.price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
