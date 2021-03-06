package serp.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link StoreInstruction} type.
 *
 * @author Abe White
 */
public class TestStoreInstruction {
    private Code _code = new Code();

    /**
     * Test that the instruction intitializes correctly when generated.
     */
    @Test
    public void testInitialize() {
        assertEquals(Constants.NOP, _code.xstore().getOpcode());
        assertNull(_code.xstore().getType());
        assertEquals(Constants.NOP, _code.istore().getOpcode());
        assertEquals(int.class, _code.istore().getType());
        assertEquals(Constants.NOP, _code.lstore().getOpcode());
        assertEquals(long.class, _code.lstore().getType());
        assertEquals(Constants.NOP, _code.fstore().getOpcode());
        assertEquals(float.class, _code.fstore().getType());
        assertEquals(Constants.NOP, _code.dstore().getOpcode());
        assertEquals(double.class, _code.dstore().getType());
        assertEquals(Constants.NOP, _code.astore().getOpcode());
        assertEquals(Object.class, _code.astore().getType());
    }

    /**
     * Test that the instruction returns its type correctly.
     */
    @Test
    public void testGetType() {
        StoreInstruction ins = _code.xstore();
        assertNull(ins.getType());
        assertEquals(-1, ins.getLocal());

        ins = _code.istore();
        assertEquals(int.class, ins.getType());
        assertEquals(int.class, ins.setLocal(1).getType());
        assertEquals(int.class, ins.setLocal(2).getType());
        assertEquals(int.class, ins.setLocal(3).getType());
        assertEquals(int.class, ins.setLocal(100).getType());

        ins = _code.lstore();
        assertEquals(long.class, ins.getType());
        assertEquals(long.class, ins.setLocal(1).getType());
        assertEquals(long.class, ins.setLocal(2).getType());
        assertEquals(long.class, ins.setLocal(3).getType());
        assertEquals(long.class, ins.setLocal(100).getType());

        ins = _code.fstore();
        assertEquals(float.class, ins.getType());
        assertEquals(float.class, ins.setLocal(1).getType());
        assertEquals(float.class, ins.setLocal(2).getType());
        assertEquals(float.class, ins.setLocal(3).getType());
        assertEquals(float.class, ins.setLocal(100).getType());

        ins = _code.dstore();
        assertEquals(double.class, ins.getType());
        assertEquals(double.class, ins.setLocal(1).getType());
        assertEquals(double.class, ins.setLocal(2).getType());
        assertEquals(double.class, ins.setLocal(3).getType());
        assertEquals(double.class, ins.setLocal(100).getType());

        ins = _code.astore();
        assertEquals(Object.class, ins.getType());
        assertEquals(Object.class, ins.setLocal(1).getType());
        assertEquals(Object.class, ins.setLocal(2).getType());
        assertEquals(Object.class, ins.setLocal(3).getType());
        assertEquals(Object.class, ins.setLocal(100).getType());
    }

    /**
     * Test that the opcode is morphed correctly when the type and local
     * of the instruction are changed.
     */
    @Test
    public void testOpcodeMorph() {
        StoreInstruction ins = _code.xstore();

        assertEquals(Constants.NOP, ins.getOpcode());
        assertEquals(Constants.NOP, ins.setType(int.class).getOpcode());
        assertEquals(Constants.ISTORE, ins.setLocal(10).getOpcode());
        assertEquals(Constants.ISTORE, ins.setType(boolean.class).getOpcode());
        assertEquals(Constants.ISTORE, ins.setType(byte.class).getOpcode());
        assertEquals(Constants.ISTORE, ins.setType(char.class).getOpcode());
        assertEquals(Constants.ISTORE, ins.setType(short.class).getOpcode());
        assertEquals(Constants.ISTORE0, ins.setLocal(0).getOpcode());
        assertEquals(0, ins.getLocal());
        assertEquals(Constants.ISTORE1, ins.setLocal(1).getOpcode());
        assertEquals(1, ins.getLocal());
        assertEquals(Constants.ISTORE2, ins.setLocal(2).getOpcode());
        assertEquals(2, ins.getLocal());
        assertEquals(Constants.ISTORE3, ins.setLocal(3).getOpcode());
        assertEquals(3, ins.getLocal());
        assertEquals(Constants.ISTORE, ins.setLocal(4).getOpcode());
        assertEquals(4, ins.getLocal());

        assertEquals(Constants.LSTORE, ins.setType(long.class).getOpcode());
        assertEquals(Constants.LSTORE0, ins.setLocal(0).getOpcode());
        assertEquals(0, ins.getLocal());
        assertEquals(Constants.LSTORE1, ins.setLocal(1).getOpcode());
        assertEquals(1, ins.getLocal());
        assertEquals(Constants.LSTORE2, ins.setLocal(2).getOpcode());
        assertEquals(2, ins.getLocal());
        assertEquals(Constants.LSTORE3, ins.setLocal(3).getOpcode());
        assertEquals(3, ins.getLocal());
        assertEquals(Constants.LSTORE, ins.setLocal(4).getOpcode());
        assertEquals(4, ins.getLocal());

        assertEquals(Constants.FSTORE, ins.setType(float.class).getOpcode());
        assertEquals(Constants.FSTORE0, ins.setLocal(0).getOpcode());
        assertEquals(0, ins.getLocal());
        assertEquals(Constants.FSTORE1, ins.setLocal(1).getOpcode());
        assertEquals(1, ins.getLocal());
        assertEquals(Constants.FSTORE2, ins.setLocal(2).getOpcode());
        assertEquals(2, ins.getLocal());
        assertEquals(Constants.FSTORE3, ins.setLocal(3).getOpcode());
        assertEquals(3, ins.getLocal());
        assertEquals(Constants.FSTORE, ins.setLocal(4).getOpcode());
        assertEquals(4, ins.getLocal());

        assertEquals(Constants.DSTORE, ins.setType(double.class).getOpcode());
        assertEquals(Constants.DSTORE0, ins.setLocal(0).getOpcode());
        assertEquals(0, ins.getLocal());
        assertEquals(Constants.DSTORE1, ins.setLocal(1).getOpcode());
        assertEquals(1, ins.getLocal());
        assertEquals(Constants.DSTORE2, ins.setLocal(2).getOpcode());
        assertEquals(2, ins.getLocal());
        assertEquals(Constants.DSTORE3, ins.setLocal(3).getOpcode());
        assertEquals(3, ins.getLocal());
        assertEquals(Constants.DSTORE, ins.setLocal(4).getOpcode());
        assertEquals(4, ins.getLocal());

        assertEquals(Constants.ASTORE, ins.setType(Object.class).getOpcode());
        assertEquals(Constants.ASTORE, ins.setType(String.class).getOpcode());
        assertEquals(Constants.ASTORE0, ins.setLocal(0).getOpcode());
        assertEquals(0, ins.getLocal());
        assertEquals(Constants.ASTORE1, ins.setLocal(1).getOpcode());
        assertEquals(1, ins.getLocal());
        assertEquals(Constants.ASTORE2, ins.setLocal(2).getOpcode());
        assertEquals(2, ins.getLocal());
        assertEquals(Constants.ASTORE3, ins.setLocal(3).getOpcode());
        assertEquals(3, ins.getLocal());
        assertEquals(Constants.ASTORE, ins.setLocal(4).getOpcode());
        assertEquals(4, ins.getLocal());
    }

}
