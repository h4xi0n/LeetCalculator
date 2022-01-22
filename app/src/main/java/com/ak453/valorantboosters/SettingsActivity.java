package com.ak453.valorantboosters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ak453.valorantboosters.utils.PrefManager;
import com.ak453.valorantboosters.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
private static final int PICKFILE_RESULT_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void updateDiscordUrl(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Discord URL: ");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new PrefManager(getApplicationContext()).setDiscordUrl(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateRate(View view) {
        if(checkWriteExternalPermission()) {
            Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            chooseFile.setType("text/plain");
            chooseFile = Intent.createChooser(chooseFile, "Locate rateText.txt");
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
        } else {
            ArrayList<String> arrPerm = new ArrayList<>();
            arrPerm.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            Uri uri = data != null ? data.getData() : null;
            if(uri != null) {
                try {
                    String fileData = readTextFile(uri);
                    String checkvalue = Util.getStringFromJSON(fileData,"theleetstoreRates");
                    if(checkvalue != null && checkvalue.equals("theleetstore")) {
                        JSONObject dataObject = new JSONObject(fileData);
                        JSONArray ratesArray = dataObject.getJSONArray("ratearray");
                        new PrefManager(getApplicationContext()).setRateArray(ratesArray.toString());
                        new PrefManager(getApplicationContext()).setServiceCharges(
                                Util.getStringFromJSON(fileData,"servicechargeA"),
                                Util.getStringFromJSON(fileData,"servicechargeB"),
                                Util.getStringFromJSON(fileData,"servicechargeC"),
                                Util.getStringFromJSON(fileData,"servicechargeD"));
                        Toast.makeText(this, "Rates Updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Wrong File, Please choose rateText.txt", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Wrong File, Please choose rateText.txt", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid File", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBack(View view) {
        view.setEnabled(false);
        Util.goTo(this,MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        Util.goTo(this,MainActivity.class);
    }

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private String readTextFile(Uri uri)
    {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
        }
        catch (IOException e) {e.printStackTrace();}
        return builder.toString();
    }
}