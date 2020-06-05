package com.example.mynotes;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AddNoteActivity extends AppCompatActivity{
    Realm realm;
    public EditText Note_Title;
    public EditText Note_Content;
    boolean fromeAdapter = false;
    int id;
    RealmResults<Note> results;
    AlertDialog.Builder builder;
    ImageView imageView;
    private static final int Pick_Image=100;
    private static final int image_captur_code=1001;
    Uri imageUri;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        //init realm database
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();

        navigation= findViewById(R.id.NavBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //start program
        Note_Title = findViewById(R.id.Note_Title);
        Note_Content = findViewById(R.id.Note_Content);
        imageView=findViewById(R.id.img);



        //save the note
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            fromeAdapter = true;
            id = intent.getIntExtra("id", 0);
            results = realm.where(Note.class).equalTo("id", id).findAll();
            for (Note note : results) {
                Note_Title.setText(note.getNoteTitle());
                Note_Content.setText(note.getNoteContent());
                imageView.setId(id);
            }
        }

        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        builder = new AlertDialog.Builder(this);

    }



    //option menue settings


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem saveItem = menu.findItem(R.id.Save_Btn);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Note_Title.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please Enter The Title", Toast.LENGTH_LONG).show();
                } else if (Note_Content.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter The Content", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                    if (fromeAdapter) {
                        for (Note note : results) {
                            note.setId(getNextKey());
                            realm.beginTransaction();
                            note.setNoteTitle(Note_Title.getText().toString());
                            note.setNoteContent(Note_Content.getText().toString());
                            note.setImageId(id);
                            realm.commitTransaction();
                        }
                    } else {
                        Note note = new Note();
                        note.setId(getNextKey());
                        note.setNoteTitle(Note_Title.getText().toString());
                        note.setNoteContent(Note_Content.getText().toString());
                        note.setImageId(id);
                        realm.beginTransaction();
                        realm.copyToRealm(note);
                        realm.commitTransaction();
                    }
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //option menu settings
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                builder.setMessage("Do you want to save your changes ?");
                builder.setCancelable(true);
                builder.setNegativeButton("DONT SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Note_Title.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "please Enter The Title", Toast.LENGTH_LONG).show();

                        } else if (Note_Content.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Enter The Content", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                            startActivity(intent);
                            if (fromeAdapter) {
                                for (Note note : results) {
                                    realm.beginTransaction();
                                    note.setNoteTitle(Note_Title.getText().toString());
                                    note.setNoteContent(Note_Content.getText().toString());
                                    note.setImageId(id);

                                    realm.commitTransaction();
                                }
                            } else {
                                Note note = new Note();
                                note.setId(getNextKey());
                                note.setNoteTitle(Note_Title.getText().toString());
                                note.setNoteContent(Note_Content.getText().toString());
                                note.setImageId(id);
                                realm.beginTransaction();
                                realm.copyToRealm(note);
                                realm.commitTransaction();
                            }
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:

                return super.onOptionsItemSelected(menuItem);
        }
    }


    public int getNextKey() {
        try {
            return realm.where(Note.class).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    void openGallary(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),Pick_Image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == Pick_Image) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    imageView.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    void openCamera(){

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pickter");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From The Camera");
        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,image_captur_code);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            imageView.setImageURI(imageUri);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.setreminder:
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    return true;

                case R.id.cohoosimage:
                    openGallary();

                case R.id.takephoto:
                    openCamera();



            }

            return true;
        }

    };

}
