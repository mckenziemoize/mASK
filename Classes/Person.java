package mASK;

/**
 * Superclass for both Student and Professor
 * 
 * @author Mckenzie Moize
 */
abstract class Person {
    // <editor-fold desc="Data Fields">
    /**
     * The desired username of the Person
     */
    protected final String ID;
    
    /**
     * The real name of the Person
     */
    protected final String NAME;
    
    /**
     * The session the Person is currently connected to
     */
    protected Session session;
    // </editor-fold>
    
    // <editor-fold desc="Constructors">
    /**
     * The constructor for a Person
     * 
     * @param name the real name of the Person
     * @param id the username of the Person
     */
    public Person(String name, String id) {
        NAME = name; 
        ID = id;
    }
    // </editor-fold>

    // <editor-fold desc="Getters">
    /**
     * @return the username of the Person
     */
    public String getId() {
        return ID;
    }

    /**
     * @return the name of the Person
     */
    public String getName() {
        return NAME;
    }
    // </editor-fold>
}