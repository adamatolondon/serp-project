package serp.bytecode;

import serp.bytecode.visitor.*;

/**
 * Code blocks compiled from source have local variable tables mapping
 * locals used in opcodes to their names and descriptions.
 *
 * @author Abe White
 */
public class LocalVariableTable extends LocalTable {
    LocalVariableTable(int nameIndex, Attributes owner) {
        super(nameIndex, owner);
    }

    /**
     * Return all the locals of this method.
     * 
     * @return all the locals of this method
     */
    public LocalVariable[] getLocalVariables() {
        return (LocalVariable[]) getLocals();
    }

    /**
     * Return the local with the given locals index, or null if none.
     * 
     * @param local the local index
     * @return the local with the given locals index, or null if none
     */
    public LocalVariable getLocalVariable(int local) {
        return (LocalVariable) getLocal(local);
    }

    /**
     * Return the local with the given name, or null if none. If multiple
     * locals have the given name, which is returned is undefined.
     * 
     * @param name the local name to search for
     * @return the local with the given name, or null if none
     */
    public LocalVariable getLocalVariable(String name) {
        return (LocalVariable) getLocal(name);
    }

    /**
     * Return all locals with the given name, or empty array if none.
     * 
     * @param name the local name to search for
     * @return all locals with the given name, or empty array if none
     */
    public LocalVariable[] getLocalVariables(String name) {
        return (LocalVariable[]) getLocals(name);
    }

	/**
	 * Import a local from another method/class. Note that the program counter and
	 * length from the given local is copied directly, and thus will be incorrect
	 * unless this method is the same as the one the local is copied from, or the pc
	 * and length are reset.
	 * 
	 * @param local the local data to add
	 * @return the newly added local
	 */
    public LocalVariable addLocalVariable(LocalVariable local) {
        return (LocalVariable) addLocal(local);
    }

    /**
     * Add a local to this table.
     * 
     * @return the newly added local
     */
    public LocalVariable addLocalVariable() {
        return (LocalVariable) addLocal();
    }

    /**
     * Add a local to this table.
     * 
     * @param name the local name
     * @param type the local type
     * @return the newly added local
     */
    public LocalVariable addLocalVariable(String name, String type) {
        return (LocalVariable) addLocal(name, type);
    }

    /**
     * Add a local to this table.
     * 
     * @param name the local name
     * @param type the local type
     * @return the newly added local
     */
    public LocalVariable addLocalVariable(String name, Class<?> type) {
        String typeName = (type == null) ? null : type.getName();
        return addLocalVariable(name, typeName);
    }

    /**
     * Add a local to this table.
     * 
     * @param name the local name
     * @param type the local type
     * @return the newly added local
     */
    public LocalVariable addLocalVariable(String name, BCClass type) {
        String typeName = (type == null) ? null : type.getName();
        return addLocalVariable(name, typeName);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterLocalVariableTable(this);
        LocalVariable[] locals = (LocalVariable[]) getLocals();
        for (int i = 0; i < locals.length; i++)
            locals[i].acceptVisit(visit);
        visit.exitLocalVariableTable(this);
    }

    protected Local newLocal() {
        return new LocalVariable(this);
    }

    protected Local[] newLocalArray(int size) {
        return new LocalVariable[size];
    }
}
