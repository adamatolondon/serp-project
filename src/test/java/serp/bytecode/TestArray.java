package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the handling of array {@link BCClass}es.
 *
 * @author Abe White
 */
public class TestArray extends AbstractStateTest {
    private BCClass _bc2 = null;

    @BeforeEach
    public void setUp() {
        _bc = _project.loadClass(String[].class);
        _bc2 = _project.loadClass(int[][].class);
    }

    @Test
    public void testType() {
        assertEquals(String[].class.getName(), _bc.getName());
        assertEquals("java.lang", _bc.getPackageName());
        assertEquals("String[]", _bc.getClassName());
        assertEquals(String[].class, _bc.getType());

        try {
            _bc.setName("Foo[]");
            fail("Allowed set name");
        } catch (UnsupportedOperationException uoe) {
        }

        assertTrue(!_bc.isPrimitive());
        assertTrue(_bc.isArray());

        assertEquals(int[][].class.getName(), _bc2.getName());
        assertNull(_bc2.getPackageName());
        assertEquals("int[][]", _bc2.getClassName());
        assertEquals(int[][].class, _bc2.getType());
    }

    @Test
    public void testSuperclass() {
        assertEquals(Object.class.getName(), _bc.getSuperclassName());
        try {
            _bc.setSuperclass("Foo");
            fail("Allowed set superclass");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    @Test
    public void testComponent() {
        assertEquals(String.class.getName(), _bc.getComponentName());
        assertEquals(String.class, _bc.getComponentType());
        assertEquals(String.class, _bc.getComponentBC().getType());
        assertEquals(int[].class.getName(), _bc2.getComponentName());
        assertEquals(int[].class, _bc2.getComponentType());
        assertEquals(int[].class, _bc2.getComponentBC().getType());
    }
}
