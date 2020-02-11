package com.example.dell.abc;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

class SoundPlayer {


    private SoundPool soundPool;

    private int hitOrangeSound;

    private int hitPinkSound;

    private int hitBlackSound;

  public   SoundPlayer(MainActivity mainActivity) {








            // SoundPool is deprecated in API level 21. (Lollipop)

        int SOUND_POOL_MAX = 3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

                soundPool = new SoundPool.Builder()
                        .setAudioAttributes(audioAttributes)
                        .setMaxStreams(SOUND_POOL_MAX)
                        .build();


            } else {

                soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);

            }


            hitOrangeSound = soundPool.load(mainActivity, R.raw.orange, 1);

            hitPinkSound = soundPool.load(mainActivity, R.raw.red, 1);

            hitBlackSound = soundPool.load(mainActivity, R.raw.black, 1);




        }

    public void playHitOrangeSound (){

        soundPool.play(hitOrangeSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }


    public void playHitPinkSound () {

        soundPool.play(hitPinkSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }


    public void playHitBlackSound () {

        soundPool.play(hitBlackSound, 1.0f, 1.0f, 1, 0, 1.0f);


    }


    }
