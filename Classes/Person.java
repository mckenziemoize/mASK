/**
 * Superclass for both Student and Professor
 * 
 * @author Mckenzie Moize
 */
abstract class Person {
    /**
     * The real name of the Person
     */
    protected final String NAME;
    
    /**
     * The desired username of the Person
     */
    protected final String ID;
    
    /**
     * The session the Person is currently connected to
     */
    protected Session session;
    
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

    /**
     * @return the name of the Person
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return the username of the Person
     */
    public String getId() {
        return ID;
    }

    /**
     * @return the Session the Person is connected to
     */
    public Session getSession() {
        return session;
    }
}