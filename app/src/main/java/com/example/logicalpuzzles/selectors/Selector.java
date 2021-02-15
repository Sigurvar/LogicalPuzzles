package com.example.logicalpuzzles.selectors;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.StatsController;
import com.example.logicalpuzzles.statistics.Stats;
import com.google.android.material.snackbar.Snackbar;


public abstract class Selector extends AppCompatActivity {

    StatsController sc;
    TableRow.LayoutParams rowParams;
    TableLayout table;
    Snackbar mySnackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        sc = StatsController.getInstance(this);
        table = findViewById(R.id.tableLayout);
        mySnackbar = Snackbar.make(table, R.string.locked, Snackbar.LENGTH_LONG);

    }
    @Override
    protected void onResume(){
        super.onResume();
        table.removeAllViews();
        generateCards();
    }
    abstract void generateCards();
    public void gameModeClick(View view, Intent intent ){
        intent.putExtra("mode", view.getTag().toString());
        startActivity(intent);
    }
    void setRowParams(double relHeight, int margin){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (int) (size.y*relHeight);
        rowParams = new TableRow.LayoutParams(0, height);
        rowParams.setMargins(margin, margin,margin,margin);
    }
    void setDisplayText(String text){
        TextView tv = findViewById(R.id.textViewDisplay);
        tv.setText(text);
    }
    public void displayBlockedErrorMessage(View view) {
        mySnackbar.show();
    }
    LinearLayout setProgress(Stats stats, int totalCompleted, int backgroundResource){
        int max, progress;
        LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.gamemode_card_layout, null);
        if (backgroundResource>0){ll.setBackgroundResource(R.drawable.standard_button);}

        if (stats.getUnlockCriteria() > totalCompleted) {
            ((ImageView) ll.findViewById(R.id.iv)).setImageResource(R.drawable.ic_lock);
            ll.setOnClickListener(this::displayBlockedErrorMessage);
            max=stats.getUnlockCriteria();
            progress = totalCompleted;
        }
        else {
            handleUnLocked(ll, stats.getName());
            max=stats.getNumberOfLevels();
            progress = stats.getNumberOfCompletedLevels();
            if (max==progress){ll.setBackgroundResource(R.drawable.level_completed);}
        }

        TextView tv = ll.findViewById(R.id.text_view);
        ProgressBar pb = ll.findViewById(R.id.progress_bar);

        pb.setMax(max);
        pb.setProgress(progress);
        tv.setText(progress + "/" + max);
        ll.setLayoutParams(rowParams);
        ll.setTag(stats.getName());

        return ll;
    }
    abstract void handleUnLocked(LinearLayout ll, String name);
}
