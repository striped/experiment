package org.kot.experiment.hash;

/**
 * Short description.
 * <p/>
 * Detail description.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 14/09/2011 21:49 $
 * @created 14/09/2011 21:49 by striped
 * @todo add JavaDoc
 */
public class FastHashTest extends HashableTest {
    public FastHashTest() {
        object = new TunedHash("Code", 100, 1.0d);
    }
}
