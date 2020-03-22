package com.shpak.polyglotreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.lang.UCharacter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String BOOKS_FOLDER_NAME = "Polyglot Reader";
    private static final String BOOKS_FOLDER_ABSOLUTE_PATH = Environment.getExternalStorageDirectory() + "/" + BOOKS_FOLDER_NAME;
    private final int PERMISSION_REQUEST_CODE = 1;
    private static LocalisedFields localisedFields = new LocalisedFields(Locale.getDefault().getLanguage());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            FileOperations fileOperations = new FileOperations();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    fileOperations.createFolder(BOOKS_FOLDER_ABSOLUTE_PATH);
                    Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                         Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                         PERMISSION_REQUEST_CODE);
                }
            }
            else {
                fileOperations.createFolder(BOOKS_FOLDER_ABSOLUTE_PATH);
                Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            noInternetConnection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            FileOperations fileOperations = new FileOperations();
            fileOperations.createFolder(BOOKS_FOLDER_ABSOLUTE_PATH);
            firstRunInfo();
        }
        else {
            finish();
        }
    }

    private void noInternetConnection() {
        String systemLang = Locale.getDefault().getLanguage();
        new AlertDialog.Builder(this)
                .setTitle(LocalisedFields.NO_INTERNET_CONNECTION)
                .setMessage(LocalisedFields.NO_INTERNET_MESSAGE)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void firstRunInfo() {
            new AlertDialog.Builder(this)
                .setTitle(LocalisedFields.ATTENTION)
                .setMessage(LocalisedFields.FIRST_RUN_MESSAGE)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}
