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


public class  MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Database database;
    ArrayList<Images> arrayList;
    HomeAdapter homeAdapter;
    public static File BASE_URI;



    @BindView(R.id.button)
    Button btn_pick;

    @BindView(R.id.button2)
    Button btn_save;

    @BindView(R.id.rcv_item)
    RecyclerView rcv;


    @OnClick(R.id.button)
    public void onPick(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
        startActivityForResult(intent,100);
    }
    
    
    @OnClick(R.id.button2)
    public void onRename(View view){
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



        String newFolder=createRandomFolder(BASE_URI);
        if(newFolder!=null && newFolder.length()!=0){
            String uri= (sharedPreferences.getString("uri",""));
            moveFile(BASE_URI,newFolder,uri);
        }


    }

    String TAG="check_random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getSharedPreferences("data",MODE_PRIVATE);
        arrayList=new ArrayList<>();
        ButterKnife.bind(this);
        database=new Database(this,"hideapp.sqlite",null,1);
        String sql="create table if not exists Images(id integer primary key autoincrement, newUri varchar(200), newName varchar(200),originalUri varchar(200), originalName varchar(200))";
        database.jquery(sql);
        homeAdapter=new HomeAdapter(this,arrayList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(homeAdapter);
        initList(database.getData("select * from Images"));
        createFile();

        BASE_URI=Environment.getExternalStorageDirectory();







        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);


            }
        }
        if (requestCode==2){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,100);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.e("aass","asr");
        if(requestCode==100 && resultCode==RESULT_OK){
//            int count=data.getClipData().getItemCount();
//            for(int i=0;i<count;i++){
                Log.e("plepl","ajojo");
                sharedPreferences.edit().putString("uri",FileUtils.getPath(this,data.getData())).commit();
//                Uri uri=Uri.parse(FileUtils.getPath(MainActivity.this,data.getData()));
//                Log.e("uri_select",uri.toString());
//                database.jquery("insert into Images values(null,'"+uri.toString()+"')");
//                initList(database.getData("select * from Images"));

//            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] getByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bytearray=new ByteArrayOutputStream();
        int bufferSize=1024;
        byte[] buffer=new byte[bufferSize];
        int len=0;
        while((len=inputStream.read(buffer))!=-1){
            bytearray.write(buffer,0,len);
        }
        return bytearray.toByteArray();
    }
    public void initList(Cursor cursor){
        arrayList.clear();
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String newUri=cursor.getString(1);
            String newName=cursor.getString(2);
            String originalUri=cursor.getString(3);
            String originalName=cursor.getString(4);
            arrayList.add(new Images(id,newUri,newName,originalUri,originalName));
            Log.e("id_ahihi",String.valueOf(id));
        }
        homeAdapter.notifyDataSetChanged();

    }
    public void createFile(){
        File folder=new File(Environment.getExternalStorageDirectory()+"/Android/data/com.example.renamefile/phone");

        Boolean succes=true;
        if(!folder.exists()){
            succes=folder.mkdir();
            BASE_URI=new File(Environment.getExternalStorageDirectory(),"/Android/data/com.example.renamefile/phone");
        }
        if(succes){
            Toast.makeText(this, "ahihi", Toast.LENGTH_SHORT).show();
        }
    }
    public String createRandomFolder(File BASE_URI){
        final double min=10000000;
        final double max=1000000000;
        String random= String.valueOf((Math.random()*(max-min+1)+min));
        String folder=random.substring(random.lastIndexOf(".")+1,random.length());
        Log.d("kiemtra", folder);
        File file=new File(Environment.getExternalStorageDirectory()+"/Android/data/com.example.renamefile/phone",folder);
        Log.d("kiemtra", String.valueOf(file.toURI()));
        Boolean succes=true;
        if(!file.exists()){
            Toast.makeText(this, "davap", Toast.LENGTH_SHORT).show();
            succes=file.mkdir();
            return folder;
        }
        else{
            createRandomFolder(BASE_URI);
        }
        return null;

    }


    public static void moveFile(File BASE_URI,String newfolder, String uri){
        File file=new File(uri);
        file.renameTo(new File(Environment.getExternalStorageDirectory()+"/Android/data/com.example.renamefile/phone"+"/"+newfolder,newfolder));
    }


}
