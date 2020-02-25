package com.example.renamefile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.renamefile.model.Database;
import com.example.renamefile.model.Images;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements HomeAdapter.CallBack {
    SharedPreferences sharedPreferences;
    Database database;
    ArrayList<Images> arrayList;
    HomeAdapter homeAdapter;
    public static File BASE_URI;

    Images images = new Images();


    @BindView(R.id.button)
    Button btn_pick;

    @BindView(R.id.button2)
    Button btn_save;

    @BindView(R.id.rcv_item)
    RecyclerView rcv;


    @OnClick(R.id.button)
    public void onPick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
        startActivityForResult(intent, 100);
    }


    @OnClick(R.id.button2)
    public void onRename(View view) {
        Toast.makeText(this, "ahihi", Toast.LENGTH_SHORT).show();
//        String uri=sharedPreferences.getString("uri","");
//        Log.e("uri_image", uri.toString());
//        String currentName = uri.substring(uri.lastIndexOf("/", uri.length()));
//        currentName = currentName.substring(1);
//        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Namhihi");
//        File from = new File(directory, currentName);
//        File to = new File(directory, "xuannam".trim() + ".jpg");
//        from.renameTo(to);

//        File sd=Environment.getExternalStorageDirectory();
//// File (or directory) to be moved
//        String sourcePath="/DCIM/Camera/Getimage.jpg";
//        File file = new File(sd,sourcePath);
//// Destination directory
//        boolean success = file.renameTo(new File(sd, "Getimage.jpg"));


//        String newFolder = createRandomFolder();
//        if (newFolder != null && newFolder.length() != 0) {
//            String uri = (sharedPreferences.getString("uri", ""));
//            moveFile(newFolder, uri);
//        }
        File file=new File(String.valueOf(getExternalFilesDir(null)));
        String base=file.toString();


    }

    String TAG = "check_random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("data", MODE_PRIVATE);
        arrayList = new ArrayList<>();
        ButterKnife.bind(this);
        database = new Database(this, "hideapp.sqlite", null, 1);
        String sql = "create table if not exists Images(id integer primary key autoincrement, newUri varchar(200), newName varchar(200),originalUri varchar(200), originalName varchar(200))";
        database.jquery(sql);
        homeAdapter = new HomeAdapter(this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(homeAdapter);
        initList(database.getData("select * from Images"));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createFile();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);


            }
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.e("aass", "asr");
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Log.e("plepl", "ajojo");
            sharedPreferences.edit().putString("uri", FileUtils.getPath(this, data.getData())).commit();
            String uri = (FileUtils.getPath(MainActivity.this, data.getData()));
            insert(uri);
            database.insertInto(images);
            initList(database.getData("select * from Images"));



        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void insert(String uri){
        String newFolder = createRandomFolder(this);
        images.setNewName(newFolder);
        String newUri=getExternalFilesDir(null)+"/.phone" + "/" + newFolder;
        images.setNewUri(newUri);
        String oldName=uri.substring(uri.lastIndexOf("/"),uri.length());
        oldName.substring(1);
        images.setOriginalName(oldName);
        String oldUri=uri.substring(0,uri.lastIndexOf("/"));
        images.setOriginalUri(oldUri);
        moveFile(newFolder,uri,this);
    }

    public void initList(Cursor cursor) {
        arrayList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String newUri = cursor.getString(1);
            String newName = cursor.getString(2);
            String originalUri = cursor.getString(3);
            String originalName = cursor.getString(4);
            arrayList.add(new Images(id, newUri, newName, originalUri, originalName));
            Log.e("id_ahihi", String.valueOf(id));
        }
        homeAdapter.notifyDataSetChanged();

    }

    public void createFile() {
        File folder = new File(getExternalFilesDir(null)+"/.phone");
        Log.e(TAG, "createFile: "+folder.getAbsolutePath() );
        Boolean succes = true;
        if (!folder.exists()) {
            succes = folder.mkdir();
        }
        if (succes) {
            Toast.makeText(this, "ahihi", Toast.LENGTH_SHORT).show();
        }
    }


    public static String createRandomFolder(Context context) {
        final double min = 10000000;
        final double max = 1000000000;
        String random = String.valueOf((Math.random() * (max - min + 1) + min));
        String folder =random.substring(random.lastIndexOf(".") + 1, random.length());
        File file = new File(context.getExternalFilesDir(null)+"/.phone", folder);
        Boolean succes = true;
        if (!file.exists()) {
            Toast.makeText(context, "davap", Toast.LENGTH_SHORT).show();
            succes = file.mkdir();
            return folder;
        } else {
            createRandomFolder(context);
        }
        return null;

    }

    public static void moveFile(String newfolder, String uri,Context context) {
        File file = new File(uri);
        file.renameTo(new File(context.getExternalFilesDir(null)+"/.phone" + "/" + newfolder, newfolder));
    }

    public static void unMoveFile(Images images,Context context){
        File file=new File(images.getNewUri()+"/"+images.getNewName());
        File sd=context.getExternalFilesDir(null);
        String uri=images.getOriginalUri().substring(sd.toString().length());
        file.renameTo(new File(sd+uri,images.getOriginalName()));
    }

    @Override
    public void callback(int id, int position) {
        Images images=arrayList.get(position);
        File file=new File(images.getNewUri()+"/"+images.getNewName());
        File sd=Environment.getExternalStorageDirectory();
        String uri=images.getOriginalUri().substring(sd.toString().length());
        file.renameTo(new File(sd+uri,images.getOriginalName()));

        database.jquery("delete from Images where id='"+id+"'");
        initList(database.getData("select * from Images"));
    }

    public static void delFolder(Images images){
        File dir = new File(images.getNewUri());
        if (dir.isDirectory())
        {
            dir.delete();
        }
    }

}
