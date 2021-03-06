package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link ConvertInstruction} type.
 *
 * @author Abe White
 */
public class TestConvertInstruction {
    private Code _code = new Code();

    /**
     * Test that the opcode is morphed correctly when the types are set.
     */
    @Test
    public void testOpcodeMorph() {
        ConvertInstruction ins = _code.convert();
        assertEquals(Constants.NOP, ins.getOpcode());

        ins.setFromType(int.class);
        assertEquals(Constants.NOP, ins.getOpcode());
        assertEquals(int.class, ins.getFromType());
        assertNull(ins.getType());

        ins.setType(int.class);
        assertEquals(Constants.NOP, ins.getOpcode());
        assertEquals(int.class, ins.getFromType());
        assertEquals(int.class, ins.getType());

        ins.setType(long.class);
        assertEquals(Constants.I2L, ins.getOpcode());
        assertEquals(int.class, ins.getFromType());
        assertEquals(long.class, ins.getType());

        ins.setType(float.class);
        assertEquals(Constants.I2F, ins.getOpcode());
        assertEquals(int.class, ins.getFromType());
        assertEquals(float.class, ins.getType());

        ins.setType(double.class);
        assertEquals(Constants.I2D, ins.getOpcode());
        assertEquals(int.class, ins.getFromType());
        assertEquals(double.class, ins.getType());

        ins.setFromType(long.class);
        assertEquals(Constants.L2D, ins.getOpcode());
        assertEquals(long.class, ins.getFromType());
        assertEquals(double.class, ins.getType());

        ins.setType(long.class);
        assertEquals(Constants.NOP, ins.getOpcode());
        assertEquals(long.class, ins.getFromType());
        assertEquals(long.class, ins.getType());

        ins.setType(int.class);
        assertEquals(Constants.L2I, ins.getOpcode());
        assertEquals(long.class, ins.getFromType());
        assertEquals(int.class, ins.getType());

        ins.setType(String.class);
        assertEquals(Constants.L2I, ins.getOpcode());

        ins.setType((Class) null);
        assertEquals(Constants.NOP, ins.getOpcode());

        ins.setType(float.class);
        assertEquals(Constants.L2F, ins.getOpcode());
    }

}
