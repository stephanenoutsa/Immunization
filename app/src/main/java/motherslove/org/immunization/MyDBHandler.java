package motherslove.org.immunization;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    Context context;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_COLUMN_ID = "_contactid";
    public static final String CONTACT_COLUMN_PHONE = "contactphone";
    public static final String CONTACT_COLUMN_DOB = "contactdob";
    public static final String CONTACT_COLUMN_LANGUAGE = "contactlanguage";
    public static final String CONTACT_COLUMN_RECEIVED = "contactreceived";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + "(" +
                CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ", " +
                CONTACT_COLUMN_PHONE + " TEXT " + ", " +
                CONTACT_COLUMN_DOB + " DATE" + ", " +
                CONTACT_COLUMN_LANGUAGE + " TEXT" + ", " +
                CONTACT_COLUMN_RECEIVED + " INTEGER" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Add new contact to CONTACTS table
    public void addContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_COLUMN_PHONE, String.valueOf(contact.getContactphone()));
        values.put(CONTACT_COLUMN_DOB, String.valueOf(contact.getContactdob()));
        values.put(CONTACT_COLUMN_LANGUAGE, String.valueOf(contact.getContactlanguage()));
        values.put(CONTACT_COLUMN_RECEIVED, contact.getContactreceived());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Get single contact from the CONTACTS table
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_CONTACTS, new String[] {CONTACT_COLUMN_ID, CONTACT_COLUMN_PHONE,
                        CONTACT_COLUMN_DOB, CONTACT_COLUMN_LANGUAGE, CONTACT_COLUMN_RECEIVED}, CONTACT_COLUMN_ID
                        + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if(c != null)
            c.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2),
                c.getString(3), Integer.parseInt(c.getString(4)));

        db.close();

        return contact;
    }

    // Get all contacts from the CONTACTS table
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CONTACTS + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        // Loop through all rows and add to list
        /*if(c.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_contactid(Integer.parseInt(c.getString(0)));
                contact.setContactphone(c.getString(1));
                contact.setContactdob(c.getString(2));

                contactList.add(contact);
            } while (c.moveToNext());
        }*/
        while(!c.isAfterLast()) {
            Contact contact = new Contact();
            contact.set_contactid(Integer.parseInt(c.getString(0)));
            contact.setContactphone(c.getString(1));
            contact.setContactdob(c.getString(2));
            contact.setContactlanguage(c.getString(3));
            contact.setContactreceived(Integer.parseInt(c.getString(4)));

            contactList.add(contact);

            c.moveToNext();
        }

        return contactList;
    }

    // Get contacts count
    public int getContactsCount() {
        String query = "SELECT * FROM " + TABLE_CONTACTS + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        try {
            return c.getCount();
        } finally {
            c.close();
            db.close();
        }
    }

    // Update single contact in the CONTACTS table
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_COLUMN_PHONE, contact.getContactphone());
        values.put(CONTACT_COLUMN_DOB, contact.getContactdob());
        values.put(CONTACT_COLUMN_LANGUAGE, String.valueOf(contact.getContactlanguage()));
        values.put(CONTACT_COLUMN_RECEIVED, contact.getContactreceived());

        return db.update(TABLE_CONTACTS, values, CONTACT_COLUMN_ID + "=?",
                new String[] {String.valueOf(contact.get_contactid())});
    }

    // Delete a contact from the CONTACTS table
    public void deleteContact(String contactphone) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONTACTS + " WHERE " + CONTACT_COLUMN_PHONE + " = " + contactphone + ";");
    }

}
