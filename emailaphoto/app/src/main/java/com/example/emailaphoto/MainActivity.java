package com.example.emailaphoto;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CONTACT_REQUEST = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;

    private Uri selectedImageUri;
    private Uri selectedContactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }


    private void sendEmail() {
        if (selectedImageUri != null && selectedContactUri != null) {
            // Retrieve the contact's name and email address
            String contactName = getContactName(selectedContactUri);
            String contactEmail = getContactEmail(selectedContactUri);

            Log.d("EmailAPhoto", "Contact Name: " + contactName);
            Log.d("EmailAPhoto", "Contact Email: " + contactEmail);

            if (Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");

                // Modify the body text to include the contact's name
                String bodyText = "Sending this to " + contactName;
                emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);

                // Explicitly set the recipient's email address
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contactEmail});

                // Attach the selected image URI
                emailIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);

                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            } else {
                Log.e("EmailAPhoto", "Invalid email address");
            }
        } else {
            Log.e("EmailAPhoto", "Selected image URI or contact URI is null");
        }
    }


    private String getContactEmail(Uri contactUri) {
        String contactEmail = "";
        String[] projection = new String[]{ContactsContract.Contacts._ID};

        try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int contactIDIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactID = cursor.getString(contactIDIndex);

                // Use contact ID to retrieve email address
                contactEmail = getEmailFromContactID(contactID);
            }
        }

        return contactEmail;
    }

    private String getEmailFromContactID(String contactID) {
        String email = "";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        String[] selectionArgs = new String[]{contactID};

        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                email = cursor.getString(emailColumnIndex);
            }
        }

        return email;
    }

    private String getContactName(Uri contactUri) {
        String contactName = "";
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

        try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                contactName = cursor.getString(nameColumnIndex);
            }
        }

        return contactName;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    selectedImageUri = data.getData();
                    openContacts(); // Automatically move to contacts after selecting an image
                    break;
                case PICK_CONTACT_REQUEST:
                    selectedContactUri = data.getData();
                    sendEmail(); // Automatically move to email after selecting a contact
                    break;
            }
        }
    }
    private void openContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Permission already granted, proceed with opening contacts
            performOpenContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening contacts
                performOpenContacts();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Cannot access contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performOpenContacts() {
        Intent contactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactsIntent, PICK_CONTACT_REQUEST);
    }
}








