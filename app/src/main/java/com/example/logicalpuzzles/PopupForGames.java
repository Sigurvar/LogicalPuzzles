package com.example.logicalpuzzles;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PopupForGames extends PopupController{

    private GameController gc;

    PopupForGames(GameController gc){
        super(gc);
        this.gc = gc;
    }
    void popupClick(View view){
        switch ((int)view.getTag()){
            case  R.string.next_level:
                gc.nextLevel(view);
                break;
            case R.string.home:
                gc.goHome(view);
                break;
            default: gc.closePopup(view);
        }
    }
    void showInfo(View v, String subGameMode){
        if(!popupIsNull()){return;}
        int header = 0;
        int info = 0;
        switch (subGameMode.split("_")[0]){
            case "four":
                header=R.string.four_color_puzzles;
                info = R.string.four_color_puzzles_info;
                break;
            case "pyramid":
                header = R.string.pyramid_puzzles;
                info = R.string.pyramid_puzzles_info;
                break;
            case "pattern":
                header = R.string.pattern_puzzles;
                info = R.string.pattern_puzzles_info;
                break;
            case "next":
                header = R.string.next_number_puzzles;
                info = R.string.next_number_puzzles_info;
                break;
            case "make":
                header = R.string.make_them_green_puzzles;
                info = R.string.make_them_green_puzzles_info;
                break;
            case "sudoku":
                header = R.string.sudoku_puzzles;
                info = R.string.sudoku_puzzles_info;
                break;
            case "equation":
                header = R.string.equation_puzzles;
                info = R.string.equation_puzzle_info;
                break;
            case "hashi":
                header = R.string.hashi_puzzles;
                info = R.string.hashi_puzzle_info;
                break;
            case "order":
                header = R.string.order_puzzle;
                info = R.string.order_puzzle_info;
                break;
            case "color":
                header = R.string.color_switching_puzzle;
                info = R.string.color_switching_puzzle_info;
                break;
            case "path":
                header = R.string.path_switching_puzzle;
                info = R.string.path_switching_puzzle_info;
                break;
        }
        View popupView = getStandardPopup(header, info, R.string.got_it);
        popupView.findViewById(R.id.removable).setVisibility(View.GONE);
        popupView.findViewById(R.id.number_of_hints).setVisibility(View.GONE);
        popupView.findViewById(R.id.hint_icon).setVisibility(View.GONE);
        createPopup(v, popupView);
    }
    void showHintScreen(View v){
        if(!popupIsNull()){return;}
        gc.setParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,20);
        View popupView = gc.addHints(inflater.inflate(R.layout.popup_hint_screen, null));
        ((ViewGroup)popupView).addView(inflater.inflate(R.layout.get_more_hints,null));
        ((TextView)popupView.findViewById(R.id.number_of_hints)).setText(String.valueOf(gc.sc.getAmountOfHints()));
        createPopup(v, popupView);
    }
    public void createAnswerPopup(View v, int header, int info, int buttonName){
        View popupView = getStandardPopup(header, info, buttonName);
        createPopup(v, popupView);
    }
    private View getStandardPopup(int header, int info, int buttonName){
        View popupView = inflater.inflate(R.layout.popup_display, null);
        ((TextView)popupView.findViewById(R.id.tv_header)).setText(header);
        ((TextView)popupView.findViewById(R.id.info)).setText(info);
        ((Button)popupView.findViewById(R.id.popup_button)).setText(buttonName);
        ((Button)popupView.findViewById(R.id.popup_button)).setTag(buttonName);
        ((TextView)popupView.findViewById(R.id.number_of_hints)).setText(String.valueOf(gc.sc.getAmountOfHints()));
        return  popupView;
    }
    public void createHintPopup(View view, String info){
        View popupView = inflater.inflate(R.layout.popup_hint, null);
        ((TextView)popupView.findViewById(R.id.info)).setText(info);
        createPopup(view, popupView);
    }

    public View createHintButton(int num, String info, int cost){
        View hint = inflater.inflate(R.layout.hint_button,null);
        if (num==0){
            ((TextView)hint.findViewById(R.id.hint_name)).setText("Hint:");
        }
        else{
            ((TextView)hint.findViewById(R.id.hint_name)).setText("Hint "+num+":");
        }
        hint = setHintCost(hint, cost);
        ((TextView)hint.findViewById(R.id.hint_info)).setText(info);
        hint.setLayoutParams(gc.params);
        return hint;
    }
    public View setHintCost(View hint, int cost){
        ((TextView)hint.findViewById(R.id.hint_cost)).setText(String.valueOf(cost));
        return hint;
    }
}
