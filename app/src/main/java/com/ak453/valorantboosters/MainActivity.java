package com.ak453.valorantboosters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ak453.valorantboosters.utils.PrefManager;
import com.ak453.valorantboosters.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private Spinner rank_start, rank_end;
private ArrayList<String> ranks;
private ArrayList<Integer> rates;
private int total_amount = 0;
private ArrayAdapter<String> adapter;
private CheckBox commissionCheck,discordUrlCheck;
private EditText results;
private String totalAmount;
private boolean flag = true;
int commission = 25;
private String discordUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rank_start = findViewById(R.id.rank_start);
        rank_end = findViewById(R.id.rank_end);
        results = findViewById(R.id.result);
        commissionCheck = findViewById(R.id.com_check);
        discordUrlCheck = findViewById(R.id.discordUrl);
        commissionCheck.setChecked(true);
        discordUrlCheck.setChecked(new PrefManager(getApplicationContext()).getDiscordCheck());
        try {
            rates = Util.getListFromJSONArray(new JSONArray(new PrefManager(getApplicationContext()).getRateArray()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(rates != null && rates.isEmpty()) {
            rates = new ArrayList() {
                {
                    add(100);
                    add(100);
                    add(175);
                    add(175);
                    add(175);
                    add(200);
                    add(200);
                    add(200);
                    add(450);
                    add(450);
                    add(450);
                    add(750);
                    add(750);
                    add(750);
                    add(1500);
                    add(1500);
                    add(1500);
                    add(3500);
                    add(10000);
                }
            };
        }
        commissionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = commissionCheck.isChecked();
            }
        });
        discordUrlCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrefManager(getApplicationContext()).setDiscordCheck(discordUrlCheck.isChecked());
            }
        });
        initDetails();
    }

    private void initDetails() {

        ranks = new ArrayList<>();
        ranks.add("IRON 1");
        ranks.add("IRON 2");
        ranks.add("IRON 3");
        ranks.add("BRONZE 1");
        ranks.add("BRONZE 2");
        ranks.add("BRONZE 3");
        ranks.add("SILVER 1");
        ranks.add("SILVER 2");
        ranks.add("SILVER 3");
        ranks.add("GOLD 1");
        ranks.add("GOLD 2");
        ranks.add("GOLD 3");
        ranks.add("PLATINUM 1");
        ranks.add("PLATINUM 2");
        ranks.add("PLATINUM 3");
        ranks.add("DIAMOND 1");
        ranks.add("DIAMOND 2");
        ranks.add("DIAMOND 3");
        ranks.add("IMMORTAL");
        ranks.add("RADIANT");

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                ranks);
        rank_start.setAdapter(adapter);
        rank_end.setAdapter(adapter);
    }

    public void calculate(View view) {
        try {
            totalAmount = "@here\nHere's the rate breakdown: \n";
            int start_position = rank_start.getSelectedItemPosition();
            int end_position = rank_end.getSelectedItemPosition();
            if(start_position<end_position) {
                for (int i = start_position; i < end_position; i++) {
                    total_amount = total_amount + rates.get(i);
                    totalAmount = totalAmount.concat("\n"+adapter.getItem(i)+" > "+adapter.getItem(i+1)+" = "+rates.get(i)+"Rs.");
                }
                if(flag) {
                    if (total_amount > 2000) {
                        commission = new PrefManager(getApplicationContext()).getServiceChargeD();
                    } else if (total_amount > 1000) {
                        commission = new PrefManager(getApplicationContext()).getServiceChargeC();
                    } else if (total_amount > 500) {
                        commission = new PrefManager(getApplicationContext()).getServiceChargeB();
                    } else {
                        commission = new PrefManager(getApplicationContext()).getServiceChargeA();
                    }
                    totalAmount = totalAmount.concat("\n\nService charges: " + commission + "Rs.");
                    total_amount = total_amount + commission;
                }
                if(new PrefManager(getApplicationContext()).getDiscordCheck()) {
                    discordUrl = "\n\nWe use our discord for faster customer service.\nPlease join us at: \n\n " + new PrefManager(getApplicationContext()).getDisordUrl() + " \n\n(Link Also in our bio)\nYou can view our vouches via #\uD83E\uDD42▸vouch-for-us .\nYou can make payment via #\uD83D\uDCB8▸payments . \n\nA booster will be assigned by our @Support or @Manager after you make a payment.";
                } else {
                    discordUrl = "";
                }
                totalAmount = totalAmount.concat("\n\nTotal price = "+String.valueOf(total_amount)+"Rs.\n\nFollow us on instagram: @theleetstore"+discordUrl+"\n\nThank you for choosing us.");
                results.setText(totalAmount);
                total_amount = 0;
                totalAmount = "";
            } else {
                Toast.makeText(this, "Starting rank lesser than Target rank", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void copy(View view) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if(!results.getText().toString().equals("")) {
            ClipData clipData = ClipData.newPlainText("rate",results.getText());
            cm.setPrimaryClip(clipData);
            Toast.makeText(this, "Rate copied to clipboard.", Toast.LENGTH_SHORT).show();
        }
        //sendDataToSheet();
    }

    public void copyDiscordUrl(View view) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if(!new PrefManager(getApplicationContext()).getDisordUrl().equals("")) {
            ClipData clipData = ClipData.newPlainText("discordUrl",new PrefManager(getApplicationContext()).getDisordUrl());
            cm.setPrimaryClip(clipData);
            Toast.makeText(this, "Discord URL copied to clipboard.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSettings(View view) {
        view.setEnabled(false);
        Util.goTo(this,SettingsActivity.class);
    }
}