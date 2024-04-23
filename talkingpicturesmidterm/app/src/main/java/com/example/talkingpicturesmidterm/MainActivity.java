package com.example.talkingpicturesmidterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SimpleAdapter.ViewBinder,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
        DialogCustomFragment.StopTalking, TextToSpeech.OnInitListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int EDIT_ACTIVITY = 3;
    private static final int CHECK_TTS = 2;
    private static final String DATABASE_NAME = "PictureRoom.db";

    private PictureDB pictureDB;
    private List<PictureEntity> dbEntities;
    private Cursor imageCursor;
    private MediaPlayer myPlayer;
    private TextToSpeech speech;
    public Uri dialogImageUri;
    private long idOfImageBeingEdited;
    private boolean ttsReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE})) {
            goOnCreating(true);
        }
    }

    /**
     * Initializes the content view, plays a random song, loads the
     * images from the database and initializes the text-to-speech
     * activity.
     *
     * @param havePermissions permission to read external storage
     */
    public void goOnCreating (boolean havePermissions) {

        Log.i("IN MAIN", "goOnCreating()");

        Intent ttsIntent;

        if(havePermissions) {
            setContentView(R.layout.activity_main);
        }

        startRandomSong();

        pictureDB = Room.databaseBuilder(getApplicationContext(),
                PictureDB.class, DATABASE_NAME).allowMainThreadQueries().build();

        getImages();

        if(imageCursor != null && imageCursor.getCount() > 0) {
            updateDBFromMediaStore();
        }

        dbEntities = (List)fetchAllImages();
        fillList();
        ttsReady = false;

        ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent,CHECK_TTS);
    }

    /**
     * Looks through all the photos on the phone, and adds them to the database
     */
    public void updateDBFromMediaStore() {

        Log.i("IN MAIN", "updateDBFromMediaStore()");

        PictureEntity imageData;
        int imageMediaId;

        do {
            imageMediaId = imageCursor.getInt(
                    imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            Log.i("imageMediaId", String.valueOf(imageMediaId));
            Log.i("IN Main", "updateDBFromMediaStore(): in do-while loop");
            if(pictureDB.daoAccess().getImageByImageMediaId(imageMediaId) == null){
                imageData = new PictureEntity();
                imageData.setImageID(imageMediaId);
                imageData.setImageDescription(getString(R.string.no_description_text));
                pictureDB.daoAccess().addImage(imageData);
            }
        } while (imageCursor.moveToNext());
    }

    /**
     * Fills the ListView using a SimpleAdapter and sets the ViewBinder,
     * the LongClickListener, and the ClickListener.
     */
    public void fillList() {

        Log.i("IN Main", "fillList()");


        String[] displayFields = {
                "imageID",
                "description"
        };
        int[] displayViews = {
                R.id.preview_image,
                R.id.preview_text
        };
        ListView theList;
        SimpleAdapter listAdapter;
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        HashMap<String, Object> hashMap;
        dbEntities = pictureDB.daoAccess().fetchAllImages();

        for(PictureEntity oneEntity : dbEntities) {
            hashMap = new HashMap<>();
            hashMap.put("imageID", MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                    oneEntity.getImageID(), MediaStore.Images.Thumbnails.MICRO_KIND, null));
            hashMap.put("description", oneEntity.getImageDescription());
            arrayList.add(hashMap);
        }

        Log.i("IN MAIN", "OUT OF FOR LOOP");

        theList = findViewById(R.id.list_item);

        listAdapter = new SimpleAdapter(this, arrayList
                , R.layout.list_view, displayFields, displayViews);
        listAdapter.setViewBinder(this);
        theList.setAdapter(listAdapter);
        theList.setOnItemLongClickListener(this);
        theList.setOnItemClickListener(this);
    }

    /**
     * Sets the views for the List
     *
     * @param view the view to bind the data to
     * @param data the data to bind to the view
     * @param asText either the result of data.toString() or an empty String
     *              but it is never null
     * @return 	true if the data was bound to the view, false otherwise
     */
    @Override
    public boolean setViewValue(View view, Object data, String asText) {

        switch(view.getId()) {
            case R.id.preview_image:
                ((ImageView)view).setImageBitmap((Bitmap)data);
                return true;
            case R.id.preview_text:
                if (data != null) {
                    ((TextView) view).setText((String) data);
                } else {
                    ((TextView) view).setText(getResources().getString(
                            R.string.no_description_text));
                }
                return true;
            default:
                Log.i("ERROR", "IN onViewValue default case");
                break;
        }
        return false;
    }

    /**
     * Puts all entities in dbEntities into an ArrayList of Hashmaps.
     *
     * @return ArrayList of Hashmaps with the image ID and description
     */
    public ArrayList<HashMap<String, Object>> fetchAllImages() {

        Log.i("IN MAIN", "fetchAllImages()");

        HashMap<String, Object> oneImage;
        ArrayList<HashMap<String, Object>> itemsList;

        dbEntities = pictureDB.daoAccess().fetchAllImages();
        itemsList = new ArrayList<>();

        for(PictureEntity oneItem : dbEntities) {
            oneImage = new HashMap<>();
            Log.i("IMAGE ID", String.valueOf(oneItem.getImageID()));
            oneImage.put("imageID", oneItem.getImageID());
            oneImage.put("description", oneItem.getImageDescription());
            itemsList.add(oneImage);
        }
        return itemsList;
    }

    /**
     * Query's Media Store images, and moves imageCursor to first image
     */
    private void getImages() {

        Log.i("IN MAIN", "getImages()");

        String[] queryFields = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };
        imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,queryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        imageCursor.moveToFirst();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long rowId) {

        Log.i("IN Main", "onItemClick()");

        int selectedImageId;
        DialogCustomFragment dialog;
        Bundle bundleToFragment;

        imageCursor.moveToPosition(position);

        Log.i("POSITION", String.valueOf(position));

        PictureEntity selectedEntity = dbEntities.get(position);
        selectedImageId = (int) selectedEntity.getImageID();

        imageCursor.moveToFirst();
        int count = imageCursor.getCount();
        Log.i("COUNT", String.valueOf(count));
        int i = 1;

        while(count > i && imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID)) != selectedImageId) {
            Log.i("IN POSITIION", String.valueOf(i));
            imageCursor.moveToNext();
            ++i;
        }

        if(imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID)) == selectedImageId) {
            dialogImageUri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            Log.i("IMAGE URI", dialogImageUri.toString());
            File filePath = new File(dialogImageUri.getPath());

            Log.i("FILE PATH", filePath.toString());

            Log.i("FILE PATH EXIST", String.valueOf(filePath.exists()));

            if(dialogImageUri != null) {
                myPlayer.pause();
                bundleToFragment = new Bundle();
                bundleToFragment.putString("image_uri", dialogImageUri.toString());
                dialog = new DialogCustomFragment();
                dialog.setArguments(bundleToFragment);
                dialog.show(getSupportFragmentManager(), "my_fragment");
                if (ttsReady) {
                    speech.speak(selectedEntity.getImageDescription(), TextToSpeech.QUEUE_ADD, null,
                            "WHAT_I_SAID");
                }
            }
        } else {
            Log.i("IN Main:", "onItemClick() else case");
            Toast.makeText(this,
                    getResources().getString(R.string.deleted_image),
                    Toast.LENGTH_SHORT).show();
            pictureDB.daoAccess().deleteImage(selectedEntity);
            dbEntities = (List) fetchAllImages();
            fillList();
        }
    }

    /**
     * Gets the image Uri of entity at position selected in list, and, if the
     * image exist, the EditActivity.class is started. If the image is not
     * found then the entity that was selected is deleted and the database is
     * updated.
     *
     * @param parent The AbsListView where the click happened
     * @param view The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param rowId The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                   long rowId) {

        Log.i("IN Main", "onItemLongClick()");

        Intent intent;
        int selectedImageId;
        Uri imageUri;

        PictureEntity selectedEntity = dbEntities.get(position);
        selectedImageId = (int) selectedEntity.getImageID();

        imageCursor.moveToFirst();
        int count = imageCursor.getCount();
        Log.i("COUNT", String.valueOf(count));
        int i = 1;

        while(count > i && imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID)) != selectedImageId) {
            Log.i("IN POSITIION", String.valueOf(i));
            imageCursor.moveToNext();
            ++i;
        }

        if(imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID)) == selectedImageId) {
            imageUri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            idOfImageBeingEdited = selectedImageId;
            myPlayer.pause();
            intent = new Intent(this, EditActivity.class);
            intent.putExtra("imageUri", imageUri.toString());
            intent.putExtra("imageDescription", selectedEntity.getImageDescription());
            startActivityForResult(intent, EDIT_ACTIVITY);
        } else {
            Log.i("IN Main:", "onItemLongClick() else case");
            Toast.makeText(this,
                    getResources().getString(R.string.deleted_image),
                    Toast.LENGTH_SHORT).show();
            pictureDB.daoAccess().deleteImage(selectedEntity);
            dbEntities = (List) fetchAllImages();
            fillList();
        }
        return true;
    }

    @Override
    public void stopTalking() {
        Log.i("IN Main", "stopTalking()");
        speech.stop();
        myPlayer.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        String newDescription;
        PictureEntity selectedEntity;
        Intent installTTSIntent;

        myPlayer.start();

        switch(requestCode) {
            case EDIT_ACTIVITY:
                if(resultCode==RESULT_OK) {

                    Log.i("IN Main", "onActivityResult(): case EDIT_ACTIVITY: RESULT_OK");

                    newDescription = data.getStringExtra("newDescription");
                    selectedEntity = pictureDB.daoAccess().getImageByImageMediaId(idOfImageBeingEdited);
                    selectedEntity.setImageDescription(newDescription);
                    pictureDB.daoAccess().updateImage(selectedEntity);
                    dbEntities = (List) fetchAllImages();
                    fillList();
                } else {
                    Log.i("IN Main", "onActivityResult(): case EDIT_ACTIVITY: RESULT_CANCELLED");
                }
                break;
            case CHECK_TTS:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    Log.i("IN Main", "onActivityResult(): case CHECK_TTS: INSTALLED");
                    speech = new TextToSpeech(this,this);
                } else {
                    Log.i("IN Main", "onActivityResult(): case CHECK_TTS: NOT INSTALLED");
                    installTTSIntent = new Intent();
                    installTTSIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
            default:
                Log.i("IN onActivityResult()", " default case " + requestCode);
                break;
        }
    }

    public void startRandomSong() {

        Log.i("IN MAIN", "startRandomSong()");

        String[] queryAudio = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };
        Cursor audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,queryAudio,null,null,
                MediaStore.Audio.Media.TITLE + " ASC");

        int audioCount = 0;
        int audioDataIndex = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int randomAudio;
        String audioFileName;
        if (audioCursor.moveToFirst()) {
            audioCount = audioCursor.getCount();
        }

        Log.i("IN MAIN", "setRandomAudio(): AUDIO COUNT " + (audioCount));

        randomAudio = (int)(audioCount*Math.random());

        Log.i("IN MAIN", "setRandomAudio(): RANDOM AUDIO INDEX " + (randomAudio));

        if(audioCursor.moveToPosition(randomAudio) && audioCursor != null) {

            Log.i("IN MAIN", "startRandomAudio(): if statement");
            audioFileName = audioCursor.getString(audioDataIndex);
            myPlayer = new MediaPlayer();
            myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                myPlayer.setDataSource(audioFileName);
                myPlayer.prepare();
                myPlayer.start();
            } catch (IOException e) {
                Log.i("ERROR", e.getMessage());
            }
        } else {
            Log.i("IN MAIN", "startRandomAudio(): else statement");
            myPlayer = MediaPlayer.create(this, R.raw.default_mp3);
            myPlayer.setOnErrorListener(this);
            myPlayer.start();
        }
        Log.i("IN Main", "setRandomAudio(): audioCursor.close()");
        audioCursor.close();
    }

    public boolean getPermission (String[] whatPermissions) {

        Log.i("IN MAIN", "getPermission()");

        int index;
        boolean haveAllPermissions;

        haveAllPermissions = true;
        for (index =0; index < whatPermissions.length;index++) {
            if (ContextCompat.checkSelfPermission(this,whatPermissions[index]) !=
                    PackageManager.PERMISSION_GRANTED) {
                haveAllPermissions = false;
            }
        }
        if (haveAllPermissions) {
            return(true);
        } else {
            ActivityCompat.requestPermissions(this, whatPermissions,
                    MY_PERMISSIONS_REQUEST);
            return (false);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        Log.i("IN MAIN", "onRequestPermissionsResult()");

        int index;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (index = 0;index < grantResults.length;index++) {
                        if (grantResults[index] !=
                                PackageManager.PERMISSION_GRANTED) {
                            goOnCreating(false);
                            return;
                        }
                    }
                    goOnCreating(true);
                } else {
                    goOnCreating(false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Starts MediaPlayer if necessary
     */
    @Override
    public void onResume() {
        Log.i("IN Main", "onResume()");
        super.onResume();
        if(myPlayer != null) {
            myPlayer.start();
        }
    }

    /**
     * Pauses MediaPlayer and TextToSpeech if necessary
     */
    @Override
    public void onPause() {
        Log.i("IN Main", "onPause()");
        super.onPause();
        if(speech != null) {
            speech.stop();
        }
        if(myPlayer != null) {
            myPlayer.pause();
        }
    }

    /**
     * Closes database, shuts down TextToSpeech, releases MediaPlayer,
     * and closes Cursor if necessary
     */
    @Override
    public void onDestroy() {
        Log.i("IN Main", "onDestroy()");
        super.onDestroy();
        if(pictureDB != null) {
            pictureDB.close();
        }
        if(speech != null) {
            speech.shutdown();
        }
        if(myPlayer != null) {
            myPlayer.release();
        }
        if(imageCursor != null) {
            imageCursor.close();
        }
    }

    /**
     * Sets global variable to true if TextToSpeech was successful
     *
     * @param status tells if initialization was successful
     */
    @Override
    public void onInit(int status) {
        Log.i("IN Main", "onInit()");
        if (status == TextToSpeech.SUCCESS) {
            ttsReady = true;
            Toast.makeText(this,
                    getResources().getString(R.string.toast_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"TTS could not be initialized",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i("IN MAIN", "onPrepared()");
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.i("IN MAIN", "onError()");
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.i("IN MAIN", "onError(): after if statement");
        myPlayer = null;
        return(true);
    }
}