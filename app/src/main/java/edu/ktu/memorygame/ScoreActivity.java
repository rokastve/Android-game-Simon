package edu.ktu.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class ScoreActivity extends MainActivity {

    private TableLayout tableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
        int[] scoreList = new int[10];
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        scoreList[0]=prefs.getInt("one", 0);
        scoreList[1]=prefs.getInt("two", 0);
        scoreList[2]=prefs.getInt("three", 0);
        scoreList[3]=prefs.getInt("four", 0);
        scoreList[4]=prefs.getInt("five", 0);
        scoreList[5]=prefs.getInt("six", 0);
        scoreList[6]=prefs.getInt("seven", 0);
        scoreList[7]=prefs.getInt("eight", 0);
        scoreList[8]=prefs.getInt("nine", 0);
        scoreList[9]=prefs.getInt("ten", 0);
        for(int i = 0; i < 10;i++) {
                View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                TextView table_place = (TextView) tableRow.findViewById(R.id.table_place);
                TextView table_score = (TextView) tableRow.findViewById(R.id.table_score);
                table_place.setText(String.valueOf(i+1));
                table_score.setText(String.valueOf(scoreList[i]));
            tableLayout.addView(tableRow);
        }
    }
}
