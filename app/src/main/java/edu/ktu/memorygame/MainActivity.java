package edu.ktu.memorygame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button scoreButton;
    private Button exitButton;
    private Button soundButton;
    private Button musicButton;
    private Context context = this;
    Boolean music = true;
    Boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenudesign);
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        music = prefs.getBoolean("music",true);
        sound = prefs.getBoolean("sound", true);
        startButton = (Button)findViewById(R.id.startButton);
        scoreButton = (Button)findViewById(R.id.scoreButton);
        exitButton = (Button)findViewById(R.id.exitButton);

        soundButton = (Button)findViewById(R.id.soundsMuteButton);
        musicButton = (Button)findViewById(R.id.musicMuteButton);

        scoreButton.setOnClickListener(scoreButtonClick);
        startButton.setOnClickListener(startButtonClick);
        exitButton.setOnClickListener(exitButtonClick);
        soundButton.setOnClickListener(soundButtonClick);
        musicButton.setOnClickListener(musicButtonClick);
        if(music)
            musicButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        if(sound)
            soundButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
    }
    public void runGameActivity()
    {
            SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("music", music);
            editor.putBoolean("sound", sound);
            editor.commit();

            Intent intent = new Intent(context, GameActivity.class);
            intent.putExtra("started", true);
            context.startActivity(intent);
    }
    View.OnClickListener musicButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(music) {
                musicButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }
            else{
                musicButton.getBackground().setColorFilter(null);
            }
            music = !music;
        }
    };

    View.OnClickListener scoreButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openScore();
        }
    };
    public void openScore()
    {
        Intent intent = new Intent(context, ScoreActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener soundButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(sound) {
                soundButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }else{
                soundButton.getBackground().setColorFilter(null);
            }
            sound = !sound;
        }
    };
    View.OnClickListener startButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            runGameActivity();
        }
    };
    View.OnClickListener exitButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            System.exit(0);
        }
    };
}
