package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link ArrayLoadInstruction} type.
 *
 * @author Abe White
 */
public class TestArrayLoadInstruction {
    private Code _code = new Code();

    /**
     * Test that the instruction initializes correctly when generated.
     */
    @Test
    public void testIniitalize() {
        assertEquals(Constants.NOP, _code.xaload().getOpcode());
        assertEquals(Constants.IALOAD, _code.iaload().getOpcode());
        assertEquals(Constants.LALOAD, _code.laload().getOpcode());
        assertEquals(Constants.FALOAD, _code.faload().getOpcode());
        assertEquals(Constants.DALOAD, _code.daload().getOpcode());
        assertEquals(Constants.AALOAD, _code.aaload().getOpcode());
        assertEquals(Constants.BALOAD, _code.baload().getOpcode());
        assertEquals(Constants.CALOAD, _code.caload().getOpcode());
        assertEquals(Constants.SALOAD, _code.saload().getOpcode());
    }

    /**
     * Test the the instruction returns its type correctly.
     */
    @Test
    public void testGetType() {
        assertNull(_code.xaload().getType());
        assertEquals(int.class, _code.iaload().getType());
        assertEquals(long.class, _code.laload().getType());
        assertEquals(float.class, _code.faload().getType());
        assertEquals(double.class, _code.daload().getType());
        assertEquals(Object.class, _code.aaload().getType());
        assertEquals(byte.class, _code.baload().getType());
        assertEquals(char.class, _code.caload().getType());
        assertEquals(short.class, _code.saload().getType());
    }

    /**
     * Test that the opcode morphs correctly with type changes.
     */
    @Test
    public void testOpcodeMorph() {
        ArrayLoadInstruction ins = _code.xaload();
        assertEquals(Constants.NOP, ins.getOpcode());
        assertEquals(Constants.NOP, ins.setType((String) null).getOpcode());
        assertEquals(Constants.NOP, ins.setType((BCClass) null).getOpcode());
        assertEquals(Constants.NOP, ins.setType((Class) null).getOpcode());

        assertEquals(Constants.IALOAD, ins.setType(int.class).getOpcode());
        assertEquals(Constants.NOP, ins.setType((String) null).getOpcode());
        assertEquals(Constants.LALOAD, ins.setType(long.class).getOpcode());
        assertEquals(Constants.FALOAD, ins.setType(float.class).getOpcode());
        assertEquals(Constants.DALOAD, ins.setType(double.class).getOpcode());
        assertEquals(Constants.AALOAD, ins.setType(Object.class).getOpcode());
        assertEquals(Constants.BALOAD, ins.setType(byte.class).getOpcode());
        assertEquals(Constants.CALOAD, ins.setType(char.class).getOpcode());
        assertEquals(Constants.SALOAD, ins.setType(short.class).getOpcode());
        assertEquals(Constants.IALOAD, ins.setType(void.class).getOpcode());
        assertEquals(Constants.AALOAD, ins.setType(String.class).getOpcode());
        assertEquals(Constants.IALOAD, ins.setType(boolean.class).getOpcode());
    }

}
