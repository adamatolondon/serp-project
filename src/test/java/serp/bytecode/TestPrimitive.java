package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the handling of primitive {@link BCClass}es.
 *
 * @author Abe White
 */
public class TestPrimitive extends AbstractStateTest {
//    public TestPrimitive(String test) {
//        super(test);
//    }

    @BeforeEach
    public void setUp() {
        _bc = _project.loadClass(int.class);
    }

    @Test
    public void testType() {
        assertEquals("int", _bc.getName());
        assertNull(_bc.getPackageName());
        assertEquals("int", _bc.getClassName());
        assertEquals(int.class, _bc.getType());

        try {
            _bc.setName("long");
            fail("Allowed set name");
        } catch (UnsupportedOperationException uoe) {
        }

        assertTrue(_bc.isPrimitive());
        assertTrue(!_bc.isArray());
    }

    @Test
    public void testSuperclass() {
        assertNull(_bc.getSuperclassName());
        try {
            _bc.setSuperclass("long");
            fail("Allowed set superclass");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    @Test
    public void testComponent() {
        assertNull(_bc.getComponentName());
    }

//    public static Test suite() {
//        return new TestSuite(TestPrimitive.class);
//    }
//
//    public static void main(String[] args) {
//        TestRunner.run(suite());
//    }
}
