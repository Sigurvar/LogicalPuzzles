package com.example.logicalpuzzles;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class PopupController {
    LayoutInflater inflater;
    Point size;
    private PopupWindow popup = null;

    public PopupController(AppCompatActivity a){
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Display display = a.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    public void createPopup(View view, View popupView){
        if(popup!=null){return;}
        int width = (int) (size.x*0.8);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popup = new PopupWindow(popupView, width, height);
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    public void closePopup(){
        if (popup!=null) {
            popup.dismiss();
            popup = null;
        }
    }
    public LayoutInflater getInflater(){
        return inflater;
    }
    public Point getPoint(){
        return size;
    }
    public boolean popupIsNull(){
        return popup==null;
    }

}
