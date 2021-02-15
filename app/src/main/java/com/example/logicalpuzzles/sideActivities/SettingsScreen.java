package com.example.logicalpuzzles.sideActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.logicalpuzzles.PopupController;
import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.StatsController;

public class SettingsScreen extends AppCompatActivity {
    PopupController pc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        pc = new PopupController(this);
    }

    public void resetProgress(View v){
        View popupView = pc.getInflater().inflate(R.layout.popup_are_you_sure, null);
        pc.createPopup(v, popupView);
    }
    public void deleteProgress(View v){
        StatsController sc = StatsController.getInstance(this);
        sc.resetAllStats();
        closePopup(v);
        View popupView = pc.getInflater().inflate(R.layout.popup_hint, null);
        ((TextView)popupView.findViewById(R.id.info)).setText(getString(R.string.progress_deleted));
        pc.createPopup(v, popupView);
    }
    public void closePopup(View v){
        pc.closePopup();
    }

}
