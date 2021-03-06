package serp.bytecode;

import java.io.*;

import serp.bytecode.lowlevel.*;
import serp.bytecode.visitor.*;
import serp.util.*;

/**
 * Any referenced class that is not a package member is represented by
 * this structure. This includes member classes and interfaces.
 *
 * @author Abe White
 */
public class InnerClass implements BCEntity, VisitAcceptor {
    private int _index = 0;
    private int _nameIndex = 0;
    private int _ownerIndex = 0;
    private int _access = Constants.ACCESS_PRIVATE;
    private InnerClasses _owner = null;

    InnerClass(InnerClasses owner) {
        _owner = owner;
    }

    /**
     * Inner classes are stored in an {@link InnerClasses} attribute.
     * 
     * @return the attribute owner
     */
    public InnerClasses getOwner() {
        return _owner;
    }

    void invalidate() {
        _owner = null;
    }

    /////////////////////
    // Access operations
    /////////////////////

    /**
     * Return the access flags of the inner class.
     * 
     * @return the access flags
     */
    public int getAccessFlags() {
        return _access;
    }

    /**
     * Set the access flags of the inner class.
     * 
     * @param accessFlags the access flags to set
     */
    public void setAccessFlags(int accessFlags) {
        _access = accessFlags;
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if public
     */
    public boolean isPublic() {
        return (getAccessFlags() & Constants.ACCESS_PUBLIC) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     */
    public void makePublic() {
        setAccessFlags(getAccessFlags() | Constants.ACCESS_PUBLIC);
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PRIVATE);
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PROTECTED);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if protected
     */
    public boolean isProtected() {
        return (getAccessFlags() & Constants.ACCESS_PROTECTED) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     */
    public void makeProtected() {
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PUBLIC);
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PRIVATE);
        setAccessFlags(getAccessFlags() | Constants.ACCESS_PROTECTED);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if private
     */
    public boolean isPrivate() {
        return (getAccessFlags() & Constants.ACCESS_PRIVATE) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     */
    public void makePrivate() {
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PUBLIC);
        setAccessFlags(getAccessFlags() | Constants.ACCESS_PRIVATE);
        setAccessFlags(getAccessFlags() & ~Constants.ACCESS_PROTECTED);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if final
     */
    public boolean isFinal() {
        return (getAccessFlags() & Constants.ACCESS_FINAL) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @param on the boolean flag
     */
    public void setFinal(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_FINAL);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_FINAL);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if static
     */
    public boolean isStatic() {
        return (getAccessFlags() & Constants.ACCESS_STATIC) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @param on the boolean flag
     */
    public void setStatic(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_STATIC);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_STATIC);
    }

    /**
     * Manipulate the class access flags.
     * 
     * @return true if interface
     */
    public boolean isInterface() {
        return (getAccessFlags() & Constants.ACCESS_INTERFACE) > 0;
    }

    /**
     * Manipulate the class access flags.
     * 
     * @param on the boolean flag
     */
    public void setInterface(boolean on) {
        if (on) {
            setAccessFlags(getAccessFlags() | Constants.ACCESS_INTERFACE);
            setAbstract(true);
        } else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_INTERFACE);
    }

    /**
     * Manipulate the class access flags.
     * 
     * @return true if abstract
     */
    public boolean isAbstract() {
        return (getAccessFlags() & Constants.ACCESS_ABSTRACT) > 0;
    }

    /**
     * Manipulate the class access flags.
     * 
     * @param on the boolean flag
     */
    public void setAbstract(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_INTERFACE);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_INTERFACE);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if synthetic
     */
    public boolean isSynthetic() {
        return (getAccessFlags() & Constants.ACCESS_SYNTHETIC) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @param on the boolean flag
     */
    public void setSynthetic(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_SYNTHETIC);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_SYNTHETIC);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if annotation
     */
    public boolean isAnnotation() {
        return (getAccessFlags() & Constants.ACCESS_ANNOTATION) > 0;
    }

    /**
     * Manipulate the inner class access flags.  Setting to true also makes this
     * an interface.
     * 
     * @param on the boolean flag
     */
    public void setAnnotation(boolean on) {
        if (on) {
            setAccessFlags(getAccessFlags() | Constants.ACCESS_ANNOTATION);
            setAccessFlags(getAccessFlags() | Constants.ACCESS_INTERFACE);
        } else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_ANNOTATION);
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @return true if enum
     */
    public boolean isEnum() {
        return (getAccessFlags() & Constants.ACCESS_ENUM) > 0;
    }

    /**
     * Manipulate the inner class access flags.
     * 
     * @param on the boolean flag
     */
    public void setEnum(boolean on) {
        if (on)
            setAccessFlags(getAccessFlags() | Constants.ACCESS_ENUM);
        else
            setAccessFlags(getAccessFlags() & ~Constants.ACCESS_ENUM);
    }

    ////////////////////////////////
    // Name, type, owner operations
    ////////////////////////////////

    /**
     * Return the {@link ConstantPool} index of the {@link UTF8Entry} that
     * describes the simple name this class is referred to in source, or
     * 0 for anonymous classes.
     * 
     * @return the {@link ConstantPool} index
     */
    public int getNameIndex() {
        return _nameIndex;
    }

    /**
     * Set the {@link ConstantPool} index of the {@link UTF8Entry} that
     * describes the simple name this class is referred to in source, or
     * 0 for anonymous classes.
     * 
     * @param nameIndex the {@link ConstantPool} index
     */
    public void setNameIndex(int nameIndex) {
        _nameIndex = nameIndex;
    }

    /**
     * Return the simple name of this inner class, or null if anonymous.
     * 
     * @return the simple name of this inner class, or null if anonymous
     */
    public String getName() {
        if (getNameIndex() == 0)
            return null;
        return ((UTF8Entry) getPool().getEntry(getNameIndex())).getValue();
    }

    /**
     * Set the simple name of this inner class.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        if (name == null)
            setNameIndex(0);
        else
            setNameIndex(getPool().findUTF8Entry(name, true));
    }

	/**
	 * Return the {@link ConstantPool} index of the {@link ClassEntry} that
	 * describes this class, or 0 if none.
	 * 
	 * @return the {@link ConstantPool} index of the {@link ClassEntry} that
	 *         describes this class, or 0 if none
	 */
    public int getTypeIndex() {
        return _index;
    }

    /**
     * Set the {@link ConstantPool} index of the {@link ClassEntry} that
     * describes this class.
     * 
     * @param index the {@link ConstantPool} index
     */
    public void setTypeIndex(int index) {
        _index = index;
    }

    /**
     * Return the full name of the inner class, or null if unset.
     * 
     * @return the full name of the inner class, or null if unset
     */
    public String getTypeName() {
        if (getTypeIndex() == 0)
            return null;
        ClassEntry entry = (ClassEntry) getPool().getEntry(getTypeIndex());
        return getProject().getNameCache().getExternalForm(entry.getNameEntry().
            getValue(), false);
    }

    /**
     * Return the type of the inner class.
     * If the type has not been set, this method will return null.
     * 
	 * @return the type of the inner class. If the type has not been set, this
	 *         method will return null
     */
    public Class<?> getType() {
        String type = getTypeName();
        if (type == null)
            return null;
        return Strings.toClass(type, getClassLoader());
    }

	/**
	 * Return the type of the inner class. If the type has not been set, this
	 * method will return null.
	 * 
	 * @return the type of the inner class. If the type has not been set, this
	 *         method will return null
	 */
    public BCClass getTypeBC() {
        String type = getTypeName();
        if (type == null)
            return null;
        return getProject().loadClass(type, getClassLoader());
    }

    /**
     * Set the type of this inner class.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        if (type == null)
            setTypeIndex(0);
        else {
            type = getProject().getNameCache().getInternalForm(type, false);
            setTypeIndex(getPool().findClassEntry(type, true));
        }
    }

    /**
     * Set the type of this inner class.
     * 
     * @param type the type to set
     */
    public void setType(Class<?> type) {
        if (type == null)
            setType((String) null);
        else
            setType(type.getName());
    }

    /**
     * Set the type of this inner class.
     * 
     * @param type the type to set
     */
    public void setType(BCClass type) {
        if (type == null)
            setType((String) null);
        else
            setType(type.getName());
    }

    /**
     * Return the {@link ConstantPool} index of the {@link ClassEntry} that
     * describes the declaring class, or 0 if this class is not a member class.
     * 
     * @return the {@link ConstantPool} index
     */
    public int getDeclarerIndex() {
        return _ownerIndex;
    }

    /**
     * Set the {@link ConstantPool} index of the {@link ClassEntry} that
     * describes the declaring class, or 0 if this class is not a member class.
     * 
     * @param ownerIndex the {@link ConstantPool} index
     */
    public void setDeclarerIndex(int ownerIndex) {
        _ownerIndex = ownerIndex;
    }

	/**
	 * Return the full name of the declaring class, or null if unset/not a member.
	 * 
	 * @return the full name of the declaring class, or null if unset/not a member
	 */
    public String getDeclarerName() {
        if (getDeclarerIndex() == 0)
            return null;
        ClassEntry entry = (ClassEntry) getPool().getEntry(getDeclarerIndex());
        return getProject().getNameCache().getExternalForm(entry.getNameEntry().
            getValue(), false);
    }

    /**
     * Return the type of the declaring class.
     * If the type has not been set or the class is not a member, this method
     * will return null.
     * 
     * @return the type of the declaring class
     */
    public Class<?> getDeclarerType() {
        String type = getDeclarerName();
        if (type == null)
            return null;
        return Strings.toClass(type, getClassLoader());
    }

    /**
     * Return the type for this instruction.
     * If the type has not been set or the class is not a member, this method
     * will return null.
     * 
     * @return the type of the declaring class
     */
    public BCClass getDeclarerBC() {
        String type = getDeclarerName();
        if (type == null)
            return null;
        return getProject().loadClass(type, getClassLoader());
    }

    /**
     * Set the type of this declaring class.
     * 
     * @param type the type of this declaring class
     */
    public void setDeclarer(String type) {
        if (type == null)
            setDeclarerIndex(0);
        else {
            type = getProject().getNameCache().getInternalForm(type, false);
            setDeclarerIndex(getPool().findClassEntry(type, true));
        }
    }

    /**
     * Set the type of this declaring class.
     * 
     * @param type the type of this declaring class
     */
    public void setDeclarer(Class<?> type) {
        if (type == null)
            setDeclarer((String) null);
        else
            setDeclarer(type.getName());
    }

    /**
     * Set the type of this declaring class.
     * 
     * @param type the type of this declaring class
     */
    public void setDeclarer(BCClass type) {
        if (type == null)
            setDeclarer((String) null);
        else
            setDeclarer(type.getName());
    }

    ///////////////////////////
    // BCEntity implementation
    ///////////////////////////

    public Project getProject() {
        return _owner.getProject();
    }

    public ConstantPool getPool() {
        return _owner.getPool();
    }

    public ClassLoader getClassLoader() {
        return _owner.getClassLoader();
    }

    public boolean isValid() {
        return _owner != null;
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterInnerClass(this);
        visit.exitInnerClass(this);
    }

    //////////////////
    // I/O operations
    //////////////////

    void read(DataInput in) throws IOException {
        setTypeIndex(in.readUnsignedShort());
        setDeclarerIndex(in.readUnsignedShort());
        setNameIndex(in.readUnsignedShort());
        setAccessFlags(in.readUnsignedShort());
    }

    void write(DataOutput out) throws IOException {
        out.writeShort(getTypeIndex());
        out.writeShort(getDeclarerIndex());
        out.writeShort(getNameIndex());
        out.writeShort(getAccessFlags());
    }
}
