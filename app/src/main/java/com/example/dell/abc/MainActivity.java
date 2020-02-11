package com.example.dell.abc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    //Images
    private ImageView box, black, yellow, orange;
    private Drawable imageBoxRight, imageBoxLeft;

    //size
    private int boxSize;

    //position
    private float boxY, boxX;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float yellowX, yellowY;

    //score
    private TextView scoreText, highScoreText;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    //class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean yellow_flg = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPlayer = new SoundPlayer(this);

        gameFrame = findViewById(R.id.gameLayout);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);
        yellow = findViewById(R.id.yellow);
        black = findViewById(R.id.black);
        scoreText = findViewById(R.id.textScore);
        highScoreText = findViewById(R.id.HighScoreText);

        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

//HighScore
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreText.setText("High Score:" + highScore);
    }
    @SuppressLint("SetTextI18n")
    public void changePos() {


        // Add timeCount

        timeCount += 20;


        // Orange

        orangeY += 12;


        float orangeCenterX = orangeX + orange.getWidth() / 2;

        float orangeCenterY = orangeY + orange.getHeight() / 2;


        if (hitCheck(orangeCenterX, orangeCenterY)) {

            orangeY = frameHeight + 100;

            score += 10;

            soundPlayer.playHitOrangeSound();

        }


        if (orangeY > frameHeight) {

            orangeY = -100;

            orangeX = (float) Math.floor(Math.random()
                    * (frameWidth - orange.getWidth()));

        }

        orange.setX(orangeX);

        orange.setY(orangeY);


        // Pink

        if (!yellow_flg && timeCount % 10000 == 0) {

            yellow_flg = true;

            yellowY = -20;

            yellowX = (float) Math.floor(Math.random() * (frameWidth - yellow.getWidth()));

        }

        if (yellow_flg) {

            yellowY += 20;



            float pinkCenterX = yellowX + yellow.getWidth() / 2;

            float pinkCenterY = yellowY + yellow.getWidth() / 2;


            if (hitCheck(pinkCenterX, pinkCenterY)) {


                yellowY = frameHeight + 30;

                score += 30;

                // Change FrameWidth

                if (initialFrameWidth > frameWidth * 110 / 100) {

                    frameWidth = frameWidth * 110 / 100;

                    changeFrameWidth(frameWidth);

                }

                soundPlayer.playHitPinkSound();

            }


            if (yellowY > frameHeight) yellow_flg = false;

            yellow.setX(yellowX);

            yellow.setY(yellowY);

        }


        // Black

        blackY += 18;


        float blackCenterX = blackX + black.getWidth() / 2;

        float blackCenterY = blackY + black.getHeight() / 2;


        if (hitCheck(blackCenterX, blackCenterY)) {

            blackY = frameHeight + 100;


            // Change FrameWidth

            frameWidth = frameWidth * 80 / 100;

            changeFrameWidth(frameWidth);

            soundPlayer.playHitBlackSound();

            if (frameWidth <= boxSize) {

                gameOver();

            }

        }


        if (blackY > frameHeight) {

            blackY = -100;

            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));

        }


        black.setX(blackX);

        black.setY(blackY);


        // Move Box

        if (action_flg) {

            // Touching

            boxX += 14;

            box.setImageDrawable(imageBoxRight);

        } else {

            // Releasing

            boxX -= 14;

            box.setImageDrawable(imageBoxLeft);

        }


        // Check box position.

        if (boxX < 0) {

            boxX = 0;

            box.setImageDrawable(imageBoxRight);

        }

        if (frameWidth - boxSize < boxX) {

            boxX = frameWidth - boxSize;

            box.setImageDrawable(imageBoxLeft);

        }


        box.setX(boxX);


        scoreText.setText("Score : " + score);


    }



    public boolean hitCheck ( float X, float Y){
            if (boxX <= X && X <= boxX + boxSize && boxY <= Y && Y <= frameHeight) {
                return true;
            }
            return false;
        }
        public void changeFrameWidth ( int frameWidth){

            ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
            params.width = frameWidth;
            gameFrame.setLayoutParams(params);


        }


        @SuppressLint({"ApplySharedPref", "SetTextI18n"})
        public void gameOver () {

            //stop Timer
            timer.cancel();
            timer = null;
            start_flg = false;

            //before showing start layout, sleep 1 second
            try {


                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            changeFrameWidth(initialFrameWidth);
            startLayout.setVisibility(View.VISIBLE);
            box.setVisibility(View.INVISIBLE);
            orange.setVisibility(View.INVISIBLE);
            yellow.setVisibility(View.INVISIBLE);
            black.setVisibility(View.INVISIBLE);

            // Update High Score
            if (score > highScore) {
                highScore = score;
                highScoreText.setText("HIGH SCORE :" + highScore);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("HIGH SCORE", highScore);
                editor.commit();
            }
        }

        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (start_flg) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    action_flg = true;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    action_flg = false;
                }
            }
            return true;
        }

        @SuppressLint("SetTextI18n")
        public void StartGame (View view){
            start_flg = true;
            startLayout.setVisibility(View.INVISIBLE);
            if (frameHeight == 0) {
                frameHeight = gameFrame.getHeight();
                frameWidth = gameFrame.getWidth();
                initialFrameWidth = frameWidth;
                boxSize = box.getHeight();
                boxX = box.getX();
                boxY = box.getY();
            }
            frameWidth = initialFrameWidth;
            box.setX(0.0f);
            black.setY(3000.0f);
            yellow.setY(3000.0f);
            orange.setY(3000.0f);
            blackY = black.getY();
            orangeY = orange.getY();
            yellowY = yellow.getY();
            box.setVisibility(View.VISIBLE);
            black.setVisibility(View.VISIBLE);
            orange.setVisibility(View.VISIBLE);
            yellow.setVisibility(View.VISIBLE);
            timeCount = 0;
            score = 0;

            scoreText.setText("Score :0");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (start_flg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                changePos();
                            }
                        });
                    }
                }
            }, 0, 20);
        }

    public void quitGame(View view){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                finishAndRemoveTask();

            } else {

                finish();

            }

    }
}
