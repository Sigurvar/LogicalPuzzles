package com.example.logicalpuzzles.gamemodes.sudokuPuzzle;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.logicalpuzzles.FileController;
import com.example.logicalpuzzles.JSONParsers.JSONParserForSaveFiles;
import com.example.logicalpuzzles.R;
import com.example.logicalpuzzles.GameController;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuPuzzle extends GameController {
    private ArrayList<Integer> oneToNine;
    private String c;
    private List<Integer> hintsPos;
    private ArrayList<ArrayList<SudokuBox>> sudokuBoxes = new ArrayList<>();
    private SudokuBox activeInput;
    private Resources res;
    private HashMap<Integer, SudokuBox> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sudoku_puzzle);
        super.onCreate(savedInstanceState);
        c = this.getApplicationContext().getPackageName();
        res = this.getApplicationContext().getResources();
        oneToNine = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        generateMap();
        setMap();
    }
    @Override
    public void onPause() {
        super.onPause();
        saveLevel();
    }
    private void saveLevel(){
        if(sudokuBoxes.stream().flatMap(Collection::stream).anyMatch(sb->sb.getValue()!=0)){
            String level = sudokuBoxes.stream().flatMap(Collection::stream).filter(SudokuBox::isChangeable).map(sb->String.valueOf(sb.getValue())).collect(Collectors.joining(""));
            JSONParserForSaveFiles js = new JSONParserForSaveFiles("");
            String jsonText = js.getSudokuAsJSONObject(level, hintsPos).toString();
            String fileName = subGameMode +"_"+ levelID;
            new FileController(this).saveToFile(fileName, jsonText);
        }
    }
    @Override
    protected boolean isCorrect() {
        ArrayList<Integer> row = new ArrayList<>(Collections.nCopies(9, 0));
        ArrayList<Integer> column = new ArrayList<>(Collections.nCopies(9, 0));
        ArrayList<Integer> box = new ArrayList<>(Collections.nCopies(9, 0));
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                row.set(j,sudokuBoxes.get(i).get(j).getValue());
                column.set(j,sudokuBoxes.get(j).get(i).getValue());
                box.set(j,sudokuBoxes.get((j/3)+(i/3)*3).get(j%3+i%3*3).getValue());
            }
            if (!hasOneToNine(row))return false;
            if (!hasOneToNine(column))return false;
            if (!hasOneToNine(box))return false;
        }
        FileController fc = new FileController(this);
       fc.deleteFile(subGameMode +"_"+ levelID);
        return true;
    }
    @Override
    public void resetMap(View view){
        if(pc.popupIsNull())sudokuBoxes.stream().flatMap(Collection::stream).forEach(SudokuBox::reset);
    }
    @Override
    protected View addHints(View v) {
        pc.setHintCost(v, 9);
        View hint = pc.createHintButton(0, getString(R.string.fill_one_number), 1);
        hint.setOnClickListener(this::fillInOneNumber);
        ((ViewGroup)v).addView(hint);
        return v;
    }

    private void fillInOneNumber(View view) {
        if(canAffordHint(view)) {
            ArrayList<SudokuBox> emptyBoxes = (ArrayList<SudokuBox>) sudokuBoxes.stream().flatMap(Collection::stream).filter(SudokuBox::isEmpty).collect(Collectors.toList());
            if (emptyBoxes.size() != 0) {
                SudokuBox sb = emptyBoxes.get((int) (Math.random() * emptyBoxes.size() - 1));
                sb.fillWithAns();
                int pos = sb.row * 9 + sb.column;
                hintsPos.add(pos);
                closePopup(view);
            } else {
                Snackbar.make(view, R.string.pyramid_all_boxes_filled, Snackbar.LENGTH_LONG).show();
            }
        }else displayNotEnoughCoinsErrorMessage(view);
    }

    @Override
    public void completeLevel() {
        sudokuBoxes.stream().flatMap(Collection::stream).filter(SudokuBox::isChangeable).forEach(sudokuBox -> sudokuBox.fillWithAns());
    }

    private boolean hasOneToNine(List<Integer> list){
        Collections.sort(list);
        return list.equals(oneToNine);
    }

    void generateMap(){
        RelativeLayout rl;
        SudokuBox sb;
        LayoutInflater inflater = pc.getInflater();
        ArrayList<SudokuBox> bb;
        Point p = pc.getPoint();
        for(int i=0;i<9;i++){
            bb = new ArrayList<>();
            for(int j=0;j<9;j++){
                rl = getRelativeLayout(i,j);
                sb = new SudokuBox(rl, this, inflater,p.x/11,i,j);
                map.put(rl.getId(), sb);
                bb.add(sb);
            }
            sudokuBoxes.add(bb);
        }
    }
    @Override
    protected void setMap() {
        FileController fc = new FileController(this);
        String json = fc.readFromSysFile(subGameMode +"_"+ levelID);

        String savedLevel = null;
        if(!json.equals("")){
            JSONParserForSaveFiles p = new JSONParserForSaveFiles(json);
            savedLevel = p.getString("level");
            hintsPos = p.getList("hintPos");
        }
        else{
            hintsPos = new ArrayList<>();
        }
        String answer= parser.getString("ans", levelID);
        String level = parser.getString("level", levelID);
        int anses = 0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                int num= Integer.parseInt(String.valueOf(level.charAt(i*9+j)));
                sudokuBoxes.get(i).get(j).newLevel(num);
                if (num ==0){
                    sudokuBoxes.get(i).get(j).setAns(Integer.parseInt(String.valueOf(answer.charAt(anses))));
                    if(savedLevel!=null && savedLevel.charAt(anses)!='0'){
                        sudokuBoxes.get(i).get(j).setValue(Integer.parseInt(String.valueOf(savedLevel.charAt(anses))));
                    }
                    anses++;
                }
            }
        }
        for (int pos : hintsPos){
            sudokuBoxes.get(pos/9).get(pos%9).fillWithAns();
        }
    }

    @Override
    protected void setLevelInfo() {

    }

    @Override
    public void wrongAns(View view) {
        unHighlightImportantField();
        activeInput=null;
        super.wrongAns(view);
    }

    private RelativeLayout getRelativeLayout(int i, int j){
        String relativeLayoutID = "id"+(i+1)+(j+1);
        return this.findViewById(res.getIdentifier(relativeLayoutID, "id", c));
    }
    public void fieldClick(View view){
        SudokuBox sb = map.get(view.getId());
        unHighlightImportantField();
        if (sb==activeInput) {
            activeInput = null;
            return;
        }
        activeInput=sb;
        highlightFields(sb.row, sb.column, R.drawable.sudoku_field_highlighted);
    }
    private void unHighlightImportantField(){
        if (activeInput==null){return;}
        int row = activeInput.row;
        int column = activeInput.column;
        int resource = R.drawable.sudoku_field_white;
        highlightFields(row, column, resource);
    }
    private void highlightFields(int row, int column, int resource){
        for (int i=0;i<9;i++){
            sudokuBoxes.get(row).get(i).highlight(resource);
            sudokuBoxes.get(i).get(column).highlight(resource);
        }
    }
    public void number_click(View view) {
        if (activeInput !=null){
            activeInput.setValue(Integer.parseInt(((Button) view).getText().toString()));
        }
    }
    public void clearBox(View view){
        if (pc.popupIsNull()&&activeInput!=null){
            activeInput.clearBox();
        }
    }
}

