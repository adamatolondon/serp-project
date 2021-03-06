package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link ConstantInstruction} type.
 *
 * @author Abe White
 */
public class TestConstantInstruction {
    private ConstantInstruction _const = new Code().constant();

    /**
     * Test that the type instruction returns its type correctly.
     */
    @Test
    public void testGetType() {
        assertNull(_const.getType());
        assertEquals(int.class, _const.setValue(0).getType());
        assertEquals(int.class, _const.setValue(2 << 3).getType());
        assertEquals(int.class, _const.setValue(2 << 7).getType());
        assertEquals(int.class, _const.setValue(2 << 15).getType());
        assertEquals(long.class, _const.setValue(0L).getType());
        assertEquals(long.class, _const.setValue(1000L).getType());
        assertEquals(float.class, _const.setValue(0F).getType());
        assertEquals(float.class, _const.setValue(1000F).getType());
        assertEquals(double.class, _const.setValue(0D).getType());
        assertEquals(double.class, _const.setValue(1000D).getType());
        assertEquals(Object.class, _const.setValue((Object) null).getType());
        assertEquals(String.class, _const.setValue("foo").getType());
        assertEquals(int.class, _const.setValue(true).getType());
        assertEquals(int.class, _const.setValue((short) 0).getType());
        assertEquals(int.class, _const.setValue('a').getType());
        assertEquals(Class.class, _const.setValue(String.class).getType());
    }

    /**
     * Test that the value is stored correctly.
     */
    @Test
    public void testGetValue() {
        assertNull(_const.getValue());
        assertEquals(0, _const.setValue(0).getIntValue());
        assertEquals(-1, _const.setValue(-1).getIntValue());
        assertEquals(2 << 3, _const.setValue(2 << 3).getIntValue());
        assertEquals(2 << 7, _const.setValue(2 << 7).getIntValue());
        assertEquals(2 << 15, _const.setValue(2 << 15).getIntValue());
        assertEquals(0L, _const.setValue(0L).getLongValue());
        assertEquals(1000L, _const.setValue(1000L).getLongValue());
        assertEquals(0F, _const.setValue(0F).getFloatValue(), .001);
        assertEquals(1000F, _const.setValue(1000F).getFloatValue(), .001);
        assertEquals(0D, _const.setValue(0D).getDoubleValue(), .001);
        assertEquals(1000D, _const.setValue(1000D).getDoubleValue(), .001);
        assertNull(_const.setValue((Object) null).getValue());
        assertEquals("foo", _const.setValue("foo").getStringValue());
        assertEquals(1, _const.setValue(true).getIntValue());
        assertEquals(0, _const.setValue((short) 0).getIntValue());
        assertEquals((int) 'a', _const.setValue('a').getIntValue());
        assertEquals(String.class.getName(),
            _const.setValue(String.class).getClassNameValue());
    }

    /**
     * Test the the opcode is morphed correctly when the value is set.
     */
    @Test
    public void testOpcodeMorph() {
        assertEquals(Constants.NOP, _const.getOpcode());
        assertEquals(Constants.ICONSTM1, _const.setValue(-1).getOpcode());
        assertEquals(Constants.ICONST0, _const.setValue(0).getOpcode());
        assertEquals(Constants.ICONST1, _const.setValue(1).getOpcode());
        assertEquals(Constants.ICONST2, _const.setValue(2).getOpcode());
        assertEquals(Constants.ICONST3, _const.setValue(3).getOpcode());
        assertEquals(Constants.ICONST4, _const.setValue(4).getOpcode());
        assertEquals(Constants.ICONST5, _const.setValue(5).getOpcode());
        assertEquals(Constants.BIPUSH, _const.setValue(2 << 3).getOpcode());
        assertEquals(Constants.SIPUSH, _const.setValue(2 << 7).getOpcode());
        assertEquals(Constants.LDC, _const.setValue(2 << 15).getOpcode());
        assertEquals(Constants.LCONST0, _const.setValue(0L).getOpcode());
        assertEquals(Constants.LCONST1, _const.setValue(1L).getOpcode());
        assertEquals(Constants.LDC2W, _const.setValue(1000L).getOpcode());
        assertEquals(Constants.FCONST2, _const.setValue(2F).getOpcode());
        assertEquals(Constants.FCONST1, _const.setValue(1F).getOpcode());
        assertEquals(Constants.FCONST0, _const.setValue(0F).getOpcode());
        assertEquals(Constants.LDC, _const.setValue(1000F).getOpcode());
        assertEquals(Constants.DCONST0, _const.setValue(0D).getOpcode());
        assertEquals(Constants.DCONST1, _const.setValue(1D).getOpcode());
        assertEquals(Constants.LDC2W, _const.setValue(2D).getOpcode());
        assertEquals(Constants.LDC2W, _const.setValue(1000D).getOpcode());
        assertEquals(Constants.LDC, _const.setValue("foo").getOpcode());
        assertEquals(Constants.ICONST1, _const.setValue(true).getOpcode());
        assertEquals(Constants.BIPUSH, _const.setValue('a').getOpcode());
        assertEquals(Constants.ICONST0, _const.setValue((short) 0).getOpcode());
        assertEquals(Constants.ACONSTNULL,
            _const.setValue((Object) null).getOpcode());
        assertEquals(Constants.LDCW, _const.setValue(String.class).getOpcode());
    }

}
