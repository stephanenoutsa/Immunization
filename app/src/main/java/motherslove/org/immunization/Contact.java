package motherslove.org.immunization;



public class Contact {

    // Private variables
    int _contactid;
    String contactphone;
    String contactdob;
    String contactlanguage;
    int contactreceived;

    // Emptry constructor
    public Contact() {

    }

    // Constructor
    public Contact(int _contactid, String contactphone, String contactdob, String contactlanguage, int contactreceived) {
        this._contactid = _contactid;
        this.contactphone = contactphone;
        this.contactdob = contactdob;
        this.contactlanguage = contactlanguage;
        this.contactreceived = contactreceived;
    }

    // Constructor
    public Contact(String contactphone, String contactdob, String contactlanguage, int contactreceived) {
        this.contactphone = contactphone;
        this.contactdob = contactdob;
        this.contactlanguage = contactlanguage;
        this.contactreceived = contactreceived;
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

    public String getContactlanguage() {
        return contactlanguage;
    }

    public void setContactlanguage(String contactlanguage) {
        this.contactlanguage = contactlanguage;
    }

    public int getContactreceived() {
        return contactreceived;
    }

    public void setContactreceived(int contactreceived) {
        this.contactreceived = contactreceived;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

}
