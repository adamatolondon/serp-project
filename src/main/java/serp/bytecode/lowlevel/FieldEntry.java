package serp.bytecode.lowlevel;

import serp.bytecode.visitor.BCVisitor;

/**
 * A reference to a class field.
 *
 * @author Abe White
 */
public class FieldEntry extends ComplexEntry {
    /**
     * Default constructor.
     */
    public FieldEntry() {
    }

    /**
     * Constructor.
     *
     * @see ComplexEntry#ComplexEntry(int,int)
     * 
     * @param classIndex class index
     * @param nameAndTypeIndex name and type index
     */
    public FieldEntry(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    public int getType() {
        return Entry.FIELD;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterFieldEntry(this);
        visit.exitFieldEntry(this);
    }
}
