package serp.bytecode;

import java.io.*;
import java.util.*;

import serp.bytecode.visitor.*;

/**
 * Attribute describing all referenced classes that are not package
 * members. This includes all member interfaces and classes.
 *
 * @author Abe White
 */
public class InnerClasses extends Attribute {
    private List<InnerClass> _innerClasses = new LinkedList<>();

    InnerClasses(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    /**
     * Return all referenced inner classes, or empty array if none.
     * 
     * @return all referenced inner classes, or empty array if none
     */
    public InnerClass[] getInnerClasses() {
        return (InnerClass[]) _innerClasses.toArray
            (new InnerClass[_innerClasses.size()]);
    }

    /**
     * Return the inner class with the given name. If multiple inner classes
     * share the name, which is returned is undefined. Use null to retrieve
     * anonymous classes.
     * 
     * @param name the name to search for or null to retrieve anonymous classes
     * @return the inner class with the given name
     */
    public InnerClass getInnerClass(String name) {
        InnerClass[] inners = getInnerClasses();
        String inner;
        for (int i = 0; i < inners.length; i++) {
            inner = inners[i].getName();
            if ((inner == null && name == null) 
                || (inner != null && inner.equals(name)))
                return inners[i];
        }
        return null;
    }

    /**
     * Return all inner classes with the given name, or empty array if none.
     * Use null to retrieve anonymous classes.
     * 
     * @param name the name to search for or null to retrieve anonymous classes
     * @return all inner classes with the given name, or empty array if none
     */
    public InnerClass[] getInnerClasses(String name) {
        List<InnerClass> matches = new LinkedList<>();
        InnerClass[] inners = getInnerClasses();
        String inner;
        for (int i = 0; i < inners.length; i++) {
            inner = inners[i].getName();
            if ((inner == null && name == null) 
                || (inner != null && inner.equals(name)))
                matches.add(inners[i]);
        }
        return (InnerClass[]) matches.toArray(new InnerClass[matches.size()]);
    }

    /**
     * Set the inner class references for this class. This method is
     * useful when importing inner class references from another class.
     * 
     * @param inners the inner classes to add
     */
    public void setInnerClasses(InnerClass[] inners) {
        clear();
        if (inners != null)
            for (int i = 0; i < inners.length; i++)
                addInnerClass(inners[i]);
    }

    /**
     * Import an inner class from another entity, or make a copy of one
     * on this entity.
     *
     * @param inner the inner class to add
     * @return the newly added inner class
     */
    public InnerClass addInnerClass(InnerClass inner) {
        InnerClass newInner = addInnerClass(inner.getName(),
            inner.getTypeName(), inner.getDeclarerName());
        newInner.setAccessFlags(inner.getAccessFlags());
        return newInner;
    }

    /**
     * Add an inner class.
     * 
     * @return the newly added inner class
     */
    public InnerClass addInnerClass() {
        InnerClass inner = new InnerClass(this);
        _innerClasses.add(inner);
        return inner;
    }

    /**
     * Add an inner class.
     *
     * @param name the simple name of the class, or null if anonymous
     * @param type the full class name of the inner class
     * @param owner the declaring class, or null if not a member class
     * @return the newly added inner class
     */
    public InnerClass addInnerClass(String name, String type, String owner) {
        InnerClass inner = addInnerClass();
        inner.setName(name);
        inner.setType(type);
        inner.setDeclarer(owner);
        return inner;
    }

    /**
     * Add an inner class.
     *
     * @param name the simple name of the class, or null if anonymous
     * @param type the class of the inner class
     * @param owner the declaring class, or null if not a member class
     * @return the newly added inner class
     */
    public InnerClass addInnerClass(String name, Class<?> type, Class<?> owner) {
        String typeName = (type == null) ? null : type.getName();
        String ownerName = (owner == null) ? null : owner.getName();
        return addInnerClass(name, typeName, ownerName);
    }

    /**
     * Add an inner class.
     *
     * @param name the simple name of the class, or null if anonymous
     * @param type the class of the inner class
     * @param owner the declaring class, or null if not a member class
     * @return the newly added inner class
     */
    public InnerClass addInnerClass(String name, BCClass type, BCClass owner) {
        String typeName = (type == null) ? null : type.getName();
        String ownerName = (owner == null) ? null : owner.getName();
        return addInnerClass(name, typeName, ownerName);
    }

    /**
     * Clear all inner classes from this entity.
     */
    public void clear() {
        InnerClass inner;
        for (Iterator<InnerClass> itr = _innerClasses.iterator(); itr.hasNext();) {
            inner = itr.next();
            itr.remove();
            inner.invalidate();
        }
    }

    /**
     * Remove the inner class with the given name. Use null for anonymous
     * classes.
     *
     * @param name the inner class name to remove
     * @return true if an inner class was removed, false otherwise
     */
    public boolean removeInnerClass(String name) {
        return removeInnerClass(getInnerClass(name));
    }

	/**
	 * Remove the given inner class. After being removed, the given inner class is
	 * invalid, and the result of any operations on it are undefined.
	 *
	 * @param innerClass the inner class to remove
	 * @return true if the inner class was removed, false otherwise
	 */
    public boolean removeInnerClass(InnerClass innerClass) {
        if (innerClass == null || !_innerClasses.remove(innerClass))
            return false;
        innerClass.invalidate();
        return true;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterInnerClasses(this);
        InnerClass[] inners = getInnerClasses();
        for (int i = 0; i < inners.length; i++)
            inners[i].acceptVisit(visit);
        visit.exitInnerClasses(this);
    }

    int getLength() {
        return 2 + (8 * _innerClasses.size());
    }

    void read(Attribute other) {
        setInnerClasses(((InnerClasses) other).getInnerClasses());
    }

    void read(DataInput in, int length) throws IOException {
        clear();
        int numInnerClasses = in.readUnsignedShort();
        InnerClass innerClass;
        for (int i = 0; i < numInnerClasses; i++) {
            innerClass = addInnerClass();
            innerClass.read(in);
        }
    }

    void write(DataOutput out, int length) throws IOException {
        InnerClass[] inners = getInnerClasses();
        out.writeShort(inners.length);
        for (int i = 0; i < inners.length; i++)
            inners[i].write(out);
    }
}
