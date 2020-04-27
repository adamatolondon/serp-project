package serp.bytecode.lowlevel;

import serp.bytecode.visitor.BCVisitor;

/**
 * A reference to an interface method.
 *
 * @author Abe White
 */
public class InterfaceMethodEntry extends ComplexEntry {
    /**
     * Default constructor.
     */
    public InterfaceMethodEntry() {
    }

    /**
     * Constructor.
     *
     * @see ComplexEntry#ComplexEntry(int,int)
     * 
     * @param classIndex the class index
     * @param nameAndTypeIndex name and type index
     */
    public InterfaceMethodEntry(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    public int getType() {
        return Entry.INTERFACEMETHOD;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterInterfaceMethodEntry(this);
        visit.exitInterfaceMethodEntry(this);
    }
}
