package com.example.logicalpuzzles.gamemodes.equationpuzzle;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.logicalpuzzles.GameController;
import com.example.logicalpuzzles.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EquationPuzzle extends GameController {

    private String level;
    ArrayList<ArrayList<String>> levelAsMatrix = new ArrayList<>();
    private int columns;
    private TableLayout tl;
    private String numbers;
    private ArrayList<TextView> textViews;
    private ArrayList<Integer> ans;
    private TextView currActiveInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_equation_puzzle);
        super.onCreate(savedInstanceState);
        tl = findViewById(R.id.table_layout_equation);
        setLevelInfo();
        setMap();
    }
    @Override
    public void resetMap(View v){
        if (pc.popupIsNull()) {
            for (TextView textView : textViews) {
                textView.setText(" ");
                textView.setTextColor(getColor(R.color.input_text_color));
            }
            unMarkField();
        }
    }
    @Override
    protected void setLevelInfo() {
        currActiveInput=null;
        numbers="";

        levelAsMatrix = new ArrayList<>();
        textViews = new ArrayList<>();
        ans = parser.getObjectInfo("answer", levelID);
        level = parser.getString("level", levelID);
        columns = parser.getInt("columns", levelID);
    }

    @Override
    protected void setMap() {
        tl.removeAllViews();
        TableRow tr = null;
        double d = 1/(double)columns;
        setParams(d-0.05, 0.06, 0);
        int added = 0;
        int i=0;
        ArrayList<String> inner = null;
        while (i<level.length()){
            if (added%columns==0){
                tr = new TableRow(this);
                tl.addView(tr);
                inner = new ArrayList<>();
                levelAsMatrix.add(inner);
            }
            TextView v = (TextView) pc.getInflater().inflate( R.layout.equation_puzzle_box, null);
            if (Character.isDigit(level.charAt(i))) {
                StringBuilder num = new StringBuilder("" + level.charAt(i));
                while (i+1<level.length() && Character.isDigit(level.charAt(i+1))){
                    num.append(level.charAt(i + 1));
                    i++;
                }
                v.setText(String.valueOf(num));
                inner.add(String.valueOf(num));
            }
            else if (level.charAt(i)=='_'){
                v.setBackgroundColor(Color.BLACK);
                inner.add(null);
            }
            else if (level.charAt(i)==' '){
                v.setOnClickListener(this::boxClick);
                v.setTextColor(getColor(R.color.input_text_color));
                v.setText(" ");
                textViews.add(v);
                inner.add("user_in");
            }
            else {
                v.setText(String.valueOf(level.charAt(i)));
                inner.add(String.valueOf(level.charAt(i)));
            }
            i++;
            added++;
            v.setLayoutParams(params);
            tr.addView(v);
        }
    }

    @Override
    protected boolean isCorrect() {
        ArrayList<Integer> user_ans;
        try {
            user_ans = (ArrayList<Integer>) textViews.stream().map(v -> Integer.parseInt(String.valueOf(v.getText()))).collect(Collectors.toList());
        }catch (NumberFormatException e){return false;}
        int input =0;
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        levelAsMatrix.forEach(v->temp.add(new ArrayList<>(v)));
        for (ArrayList<String> matrix : temp){
            for (int i = 0;i<matrix.size();i++){
                if (matrix.get(i) != null && matrix.get(i).equals("user_in")){
                    matrix.set(i, user_ans.get(input).toString());
                    input++;
                }
            }
        }
        ArrayList<String> operators = new ArrayList<>(Arrays.asList("+","-","*", "=","/"));
        for(int i = 0;i<temp.size();i++){
            ArrayList<String> matrix = temp.get(i);
            for(int j=0;j<matrix.size();j++){
                if(isNumber(matrix.get(j))){
                    int cur = j;
                    if((j==0 || !operators.contains(matrix.get(j-1))) && j+1!=columns && operators.contains(matrix.get(j+1))){
                        int num = Integer.parseInt(matrix.get(j));
                        int num1 = 0;
                        while(cur+1!=columns && matrix.get(cur+1)!=null){
                            switch (matrix.get(cur+1)){
                                case "+":
                                    num += Integer.parseInt(matrix.get(cur+2));
                                    break;
                                case "-":
                                    num -= Integer.parseInt(matrix.get(cur+2));
                                    break;
                                case "*":
                                    num *= Integer.parseInt(matrix.get(cur+2));
                                    break;
                                case "/":
                                    num /= Integer.parseInt(matrix.get(cur+2));
                                    break;
                                case "=":
                                    num1= num;
                                    num=Integer.parseInt(matrix.get(cur+2));
                                    break;
                            }
                            cur+=2;
                        }
                        if(num != num1)return false;
                    }
                    if((i==0 || !operators.contains(temp.get(i-1).get(j))) && i+1!=temp.size() && operators.contains(temp.get(i+1).get(j))){
                        cur = i;
                        int num = Integer.parseInt(matrix.get(j));
                        int num1 = 0;
                        while(cur+1!=temp.size() && temp.get(cur+1).get(j)!=null){
                            switch (temp.get(cur+1).get(j)){
                                case "+":
                                    num += Integer.parseInt(temp.get(cur+2).get(j));
                                    break;
                                case "-":
                                    num -= Integer.parseInt(temp.get(cur+2).get(j));
                                    break;
                                case "*":
                                    num *= Integer.parseInt(temp.get(cur+2).get(j));
                                    break;
                                case "/":
                                    num /= Integer.parseInt(temp.get(cur+2).get(j));
                                    break;
                                case "=":
                                    num1= num;
                                    num=Integer.parseInt(temp.get(cur+2).get(j));
                                    break;
                            }
                            cur+=2;
                        }
                        if(num != num1)return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean isNumber(String num){
        if (num==null) return false;
        try{
            Integer.parseInt(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 4);
        View hint = pc.createHintButton(0, getString(R.string.fill_one_number), 1);
        hint.setOnClickListener(this::fillInOneNumber);
        ((ViewGroup)v).addView(hint);
        return v;
    }

    @Override
    public void completeLevel() {
        for (int i=0;i<textViews.size();i++) {
            textViews.get(i).setText(String.valueOf(ans.get(i)));
            textViews.get(i).setTextColor(Color.GREEN);
        }
    }
    private void fillInOneNumber(View v){
        if (canAffordHint(v)) {
            for (int i = 0; i < textViews.size(); i++) {
                if (textViews.get(i).getText() == " ") {
                    textViews.get(i).setText(String.valueOf(ans.get(i)));
                    textViews.get(i).setTextColor(Color.GREEN);
                    closePopup(v);
                    return;
                }
            }
            Snackbar.make(v, R.string.pyramid_all_boxes_filled, Snackbar.LENGTH_LONG).show();
        }else displayNotEnoughCoinsErrorMessage(v);
    }
    private void boxClick(View view) {
        numbers ="";
        unMarkField();
        markField(view);
    }

    void markField(View view){
        if (((TextView)view).getCurrentTextColor()==Color.GREEN)return;
        currActiveInput = (TextView) view;
        currActiveInput.setBackgroundResource(R.drawable.box_with_border_active);
    }
    public void number_click(View view) {
        int maxLength = 3;
        if (currActiveInput !=null && numbers.length()< maxLength && currActiveInput.getCurrentTextColor()!=Color.GREEN){
            numbers += (((Button)view).getText().toString());
            currActiveInput.setText(numbers);
        }
    }
    void unMarkField(){
        if (currActiveInput !=null){
            currActiveInput.setBackgroundResource(R.drawable.box_with_border);
            currActiveInput = null;
        }
    }
    public void clearBox(View view){
        if (currActiveInput !=null){
            currActiveInput.setText(" ");
            numbers="";
        }
    }
}
