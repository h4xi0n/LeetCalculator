package com.ak453.valorantboosters;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.ak453.valorantboosters.utils.PrefManager;
import com.ak453.valorantboosters.utils.Util;

public class DaProtectionActivity extends AppCompatActivity {
    private static final String entry = "7331";
    private int count = 0;
    private long startMillis=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_protection);
        if(new PrefManager(getApplicationContext()).getPassword().equals(entry)) {
            Util.goTo(DaProtectionActivity.this,MainActivity.class);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_UP) {
            long time= System.currentTimeMillis();
            if (startMillis==0 || (time-startMillis> 5000) ) {
                startMillis=time;
                count=1;
            } else { count++; }
            if (count==10) {
                showPasswordDialog();
            }
            return true;
        }
        return false;
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password: ");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String password = input.getText().toString();
            if(password.equals(entry)) {
                new PrefManager(getApplicationContext()).setPassword(password);
                Util.goTo(DaProtectionActivity.this,MainActivity.class);
            } else {
                Toast.makeText(this, "HAHA,try again!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

}
