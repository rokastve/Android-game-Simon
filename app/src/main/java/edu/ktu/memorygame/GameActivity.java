package edu.ktu.memorygame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends MainActivity {
    private Context context = this;
    private ProgressBar remainingTimeProgress;
    private TextView remainingTimeText;
    private Button redButton;
    private Button blueButton;
    private Button yellowButton;
    private Button greenButton;
    private TextView scoreText;
    private TextView stage;
    private int timeLeft;
    private int score = 0;
    private List<List<String>> sequences = new ArrayList<>();
    final int PLAYING = 1;
    final int IDLE = 0;
    private int state = IDLE;
    int stagenum = 0;
    int ind = 0;
    int currind = 0;
    private SoundPool soundPool;
    private int buttonSound, bgSound;
    private final String[] colour={"red","blue","green","yellow"};
    int[] scoreList = new int[]{0,0,0,0,0,0,0,0,0,0,0};

    Boolean music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingamedesign);
        remainingTimeProgress = findViewById(R.id.progress);
        remainingTimeText = findViewById(R.id.timeText);
        redButton = findViewById(R.id.redButton);
        blueButton = findViewById(R.id.blueButton);
        yellowButton = findViewById(R.id.yellowButton);
        greenButton = findViewById(R.id.greenButton);
        scoreText = findViewById(R.id.scoreText);
        stage = findViewById(R.id.stageText);

        redButton.setOnClickListener(redButtonClicked);
        blueButton.setOnClickListener(blueButtonClicked);
        greenButton.setOnClickListener(greenButtonClicked);
        yellowButton.setOnClickListener(yellowButtonClicked);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        buttonSound = soundPool.load(this, R.raw.button, 1);
        bgSound = soundPool.load(this, R.raw.background,1);
        score = 0;
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        music = prefs.getBoolean("music",true);
        timeLeft = 60;
        Thread t=new Thread(){


            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(timeLeft>0 && state == PLAYING) {
                                    timeLeft--;
                                    remainingTimeText.setText("Time left: " + String.valueOf(timeLeft));
                                    remainingTimeProgress.setProgress((timeLeft * 100 / 60));
                                }else {
                                    if(timeLeft>0)
                                        remainingTimeText.setText("Time left: " + String.valueOf(timeLeft));
                                    else
                                    remainingTimeText.setText("You lost");
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();

        new AlertDialog.Builder(this)
                .setTitle("Game start")
                .setNeutralButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                        getScore();
                    }
                }).show();


    }
    public void getScore(){
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
    }
    public  void saveScore(int currScore){
        SharedPreferences prefs = getSharedPreferences("values", MODE_PRIVATE);
        scoreList[10]=currScore;
        Arrays.sort(scoreList);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("one", scoreList[10]);
        editor.putInt("two", scoreList[9]);
        editor.putInt("three", scoreList[8]);
        editor.putInt("four", scoreList[7]);
        editor.putInt("five", scoreList[6]);
        editor.putInt("six", scoreList[5]);
        editor.putInt("seven", scoreList[4]);
        editor.putInt("eight", scoreList[3]);
        editor.putInt("nine", scoreList[2]);
        editor.putInt("ten", scoreList[1]);
        editor.commit();

    }
    public void generate(){
        int num = 3 + stagenum/3;
        ArrayList<String> str = new ArrayList<>();
        Random rn = new Random();
        for(int i = 0; i < num; i++){
            int nmbr = rn.nextInt(100);
            if(nmbr<=25)
                str.add(colour[0]);
            if(nmbr>25&&nmbr<=50)
                str.add(colour[1]);
            if(nmbr>50&&nmbr<=75)
                str.add(colour[2]);
            if(nmbr>75)
                str.add(colour[3]);
        }
        sequences.add(str);
    }
    public void startGame(){
        generate();
        if(music) {
            soundPool.play(bgSound, 1, 1, 1, -1, 1);
        }
        stage.setText(String.valueOf(stagenum+1));
        blinkButtons(sequences.get(stagenum));
    }
    public void increase_stage()
    {
            timeLeft=60;
            stagenum++;
            stage.setText(String.valueOf(stagenum+1));
            generate();
            state = IDLE;
            remainingTimeText.setText("Win");
            new AlertDialog.Builder(this)
                    .setTitle("Stage completed")
                    .setMessage("Next stage:"+stagenum)
                    .setNeutralButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            blinkButtons(sequences.get(stagenum));
                            currind=0;
                        }
                    }).show();
    }
    public void  gameOver(){
        state = IDLE;
        saveScore(score);
        remainingTimeText.setText("Loss");
        new AlertDialog.Builder(this)
                .setTitle("You lost")
                .setMessage("Your score:"+score)
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        soundPool.release();
                        soundPool = null;
                        toMenu();
                    }
                }).setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetGame();
            }
        }).show();
    }
    public void resetGame(){
        stagenum=0;
        state=IDLE;
        score=0;
        timeLeft=60;
        currind=0;
        scoreText.setText("0");
        startGame();
    }
    public void toMenu(){
        Intent intent = new Intent(null, MainActivity.class);
        intent.putExtra("started", false);
        context.startActivity(intent);
    }

    View.OnClickListener redButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state==PLAYING ) {
                if (sound)
                soundPool.play(buttonSound, 1,1,0 ,0,1);
                if (sequences.get(stagenum).get(currind) == "red") {
                    redButton.setBackgroundColor(Color.WHITE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            redButton.setBackgroundColor(Color.RED);
                        }
                    }, 100);
                    score += timeLeft;
                    scoreText.setText(String.valueOf(score));
                    currind++;
                    if (currind == sequences.get(stagenum).size()) {
                        increase_stage();
                    }
                } else {
                    gameOver();
                }
            }
        }
    };
    View.OnClickListener blueButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state==PLAYING) {
                if (sound)
                soundPool.play(buttonSound, 1,1,0 ,0,1);
                if (sequences.get(stagenum).get(currind) == "blue") {
                    blueButton.setBackgroundColor(Color.WHITE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            blueButton.setBackgroundColor(Color.BLUE);
                        }
                    }, 100);
                    score += timeLeft;
                    scoreText.setText(String.valueOf(score));
                    currind++;
                    if (currind == sequences.get(stagenum).size()) {
                        increase_stage();
                    }
                } else {
                    gameOver();
                }
            }
        }
    };
    View.OnClickListener greenButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state==PLAYING) {
                if (sound)
                soundPool.play(buttonSound, 1,1,0 ,0,1);
                if (sequences.get(stagenum).get(currind) == "green") {
                    greenButton.setBackgroundColor(Color.WHITE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            greenButton.setBackgroundColor(Color.parseColor("#008000"));
                        }
                    }, 100);
                    score += timeLeft;
                    scoreText.setText(String.valueOf(score));
                    currind++;
                    if (currind == sequences.get(stagenum).size()) {
                        increase_stage();
                    }
                } else {
                    gameOver();
                }
            }
        }
    };
    @Override
    public void onDestroy() {

        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
    View.OnClickListener yellowButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(state==PLAYING) {
                if (sound)
                soundPool.play(buttonSound, 1,1,0 ,0,1);
                yellowButton.setBackgroundColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yellowButton.setBackgroundColor(Color.YELLOW);
                    }
                }, 100);
                if (sequences.get(stagenum).get(currind) == "yellow") {
                    score += timeLeft;
                    scoreText.setText(String.valueOf(score));
                    currind++;
                    if (currind == sequences.get(stagenum).size()) {
                        increase_stage();
                    }
                } else {
                    gameOver();
                }
            }
        }
    };
    public void blinkButtons(final List<String> colors){
        state = IDLE;
        ind = 0;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                blinkButton(colors.get(ind));
                ind++;
                if(ind!=(colors.size())) {
                    handler.postDelayed(this, 650);
                }else{
                    state = PLAYING;
                }
            }
        });
    }
    public  void blinkButton(String color){
        switch (color){
            case "red":
                redButton.setBackgroundColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        redButton.setBackgroundColor(Color.RED);
                    }
                }, 500);
                break;
            case "blue":
                blueButton.setBackgroundColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blueButton.setBackgroundColor(Color.BLUE);
                    }
                }, 500);
                break;
            case "green":
                greenButton.setBackgroundColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        greenButton.setBackgroundColor(Color.parseColor("#008000"));
                    }
                }, 500);
                break;
            case "yellow":
                yellowButton.setBackgroundColor(Color.WHITE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yellowButton.setBackgroundColor(Color.YELLOW);
                    }
                }, 500);
                break;
            default:
                break;
        }
    }
}
