package motherslove.org.immunization;



public class Contact {

    // Private variables
    int _contactid;
    String contactphone;
    String contactdob;

    // Emptry constructor
    public Contact() {

    }

    // Constructor
    public Contact(int _contactid, String contactphone, String contactdob) {
        this._contactid = _contactid;
        this.contactphone = contactphone;
        this.contactdob = contactdob;
    }

    // Constructor
    public Contact(String contactphone, String contactdob) {
        this.contactphone = contactphone;
        this.contactdob = contactdob;
    }

    // Getter and Setter methods
    public int get_contactid() {
        return _contactid;
    }

    public void set_contactid(int _contactid) {
        this._contactid = _contactid;
    }

    public String getContactdob() {
        return contactdob;
    }

    public void setContactdob(String contactdob) {
        this.contactdob = contactdob;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

}
