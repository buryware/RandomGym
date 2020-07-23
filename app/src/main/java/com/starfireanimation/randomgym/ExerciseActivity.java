package com.starfireanimation.randomgym;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import static android.os.SystemClock.sleep;

public class ExerciseActivity extends AppCompatActivity {

    private Random randomSeed;
    private Switch RGSwitchBtn;
    private ImageButton TimerBtn;
    private ArrayList gym_images;
    private ArrayList home_images;

    private String ExerciseType;
    private ImageView exerciseView;
    private int exerciseViewID = -1;
    private int lastViewID = -1;
    private long timer_duration;
    private boolean bRunning = false;

    private Dialog dialog;
    private String reset_time;
    private ImageButton close;
    private Chronometer chronometer;
    private ToneGenerator tone;
    private CountDownTimer countDownTimer = null;
    private Button start, stop, reset, time_a, time_b, time_c, time_d, time_e, time_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ExerciseType = extras.getString("ExerciseType");
        }
        setContentView(R.layout.exercise_view);

        gym_images = new ArrayList<Integer>();
        home_images = new ArrayList<Integer>();

        randomSeed = new Random();
        getRandomGymImagesIdentifiers();
        getExerciseImage();

        TimerBtn = (ImageButton) findViewById(R.id.imageTimerButton);
        TimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   DimExerciseView(view);

                dialog = new Dialog(view.getContext());
                View vLoad = LayoutInflater.from(ExerciseActivity.this).inflate(R.layout.timer_main, null);
                dialog.setContentView(vLoad);

                start = (Button) dialog.findViewById(R.id.start_button);
                stop = (Button) dialog.findViewById(R.id.stop_button);
                reset = (Button) dialog.findViewById(R.id.reset_button);

                time_a = (Button) dialog.findViewById(R.id.time_a_button);
                time_b = (Button) dialog.findViewById(R.id.time_b_button);
                time_c = (Button) dialog.findViewById(R.id.time_c_button);
                time_d = (Button) dialog.findViewById(R.id.time_d_button);
                time_e = (Button) dialog.findViewById(R.id.time_e_button);
                time_f = (Button) dialog.findViewById(R.id.time_f_button);

                //   close = (ImageButton)dialog.findViewById(R.id.close_button);
                chronometer = (Chronometer) dialog.findViewById(R.id.chronometer);
                chronometer.setText(getResources().getString(R.string.default_timer));
                //  chronometer.setCountDown(true);
                reset_time = getResources().getString(R.string.default_timer);
                timer_duration = 60000;

                dialog.setCancelable(false);

                start.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (bRunning == false) {

                            bRunning = true; // start the timer

                            countDownTimer = new CountDownTimer(timer_duration, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    int seconds = (int) (millisUntilFinished / 1000);
                                    int minutes = seconds / 60;
                                    seconds = seconds % 60;
                                    chronometer.setText(String.format("%02d", minutes)
                                            + ":" + String.format("%02d", seconds));
                                }

                                public void onFinish() {
                                    //chronometer.setText(getResources().getString(R.string.finish_time));
                                    chronometer.setText(getResources().getString(R.string.finish_time));

                                    // beep
                                    play_our_alarm();
                                    bRunning = false;
                                }
                            };
                            countDownTimer.start();
                        }
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // chronometer.stop();

                        if (bRunning == true) {
                            countDownTimer.cancel();

                            if (tone != null) {
                                tone.stopTone();
                            }
                            sleep(1000);

                            bRunning = false;  // next time
                        } else {
                             dialog.dismiss();
                        }
                    }
                });

                reset.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.setText(reset_time);
                        countDownTimer.cancel();

                        if (tone != null) {
                            tone.stopTone();
                        }

                        bRunning = false;
                    }
                });

                /*   close.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                */

                time_a.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_a));
                        reset_time = getResources().getString(R.string.time_a);
                        timer_duration = 30000;
                    }
                });

                time_b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_b));
                        reset_time = getResources().getString(R.string.time_b);
                        timer_duration = 60000;
                    }
                });

                time_c.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_c));
                        reset_time = getResources().getString(R.string.time_c);
                        timer_duration = 90000;
                    }
                });

                time_d.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_d));
                        reset_time = getResources().getString(R.string.time_d);
                        timer_duration = 120000;
                    }
                });

                time_e.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_e));
                        reset_time = getResources().getString(R.string.time_e);
                        timer_duration = 150000;
                    }
                });

                time_f.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        chronometer.setText(getResources().getString(R.string.time_f));
                        reset_time = getResources().getString(R.string.time_f);
                        timer_duration = 180000;
                    }
                });


                dialog.show();

                /*Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.pizzaentertainment.androidtimer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }*/

            }
        });

        RGSwitchBtn = (Switch) findViewById(R.id.nextworkoutswitch);
        RGSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getExerciseImage();
            }
        });
        RGSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //sleep(2000);
                    RGSwitchBtn.toggle();
                    getExerciseImage();
                } else {
                    //code  for off setting
                }
            }
        });
    }

    private void play_our_alarm(){
        // beep
        AudioManager audioManager = (AudioManager) ExerciseActivity.this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, audioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF), AudioManager.FLAG_PLAY_SOUND);

        tone = new ToneGenerator(AudioManager.STREAM_DTMF, 100); // 100 is max volume

        for (int i = 0; i < 2; i++){
            tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500); // 500ms
            sleep(500);
            tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500); // 500ms
            sleep(2500);
        }
        tone.stopTone();
    };

    final private void DimExerciseView(View view) {
        view.getBackground().setAlpha(0x30);

        View workoutview = (ImageView) findViewById(R.id.exerciseView);
        workoutview.getBackground().setAlpha(0x30);

        View nextview = (ImageView) findViewById(R.id.nextworkouttext);
        nextview.getBackground().setAlpha(0x30);

        View rgswitchview = (Switch) findViewById(R.id.nextworkoutswitch);
        rgswitchview.getBackground().setAlpha(0x30);
    }

    private void getExerciseImage() {
        exerciseView = (ImageView) findViewById(R.id.exerciseView);
        lastViewID = exerciseViewID;
        for (int i = 0; i < 10; i++){
            exerciseViewID = getRandomGymImageID();
            if (exerciseViewID != lastViewID){
                break;
            }
        }
        if (ExerciseType.matches("Home")) {
            exerciseView.setBackgroundResource((Integer) home_images.get(exerciseViewID));
        } else {
            exerciseView.setBackgroundResource((Integer) gym_images.get(exerciseViewID));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getRandomGymImagesIdentifiers() {
        int resID = 0;
        int imgnum = 0;

        Field[] drawables = com.starfireanimation.randomgym.R.drawable.class.getFields();
        for (Field field : drawables) {
            if (field.getName().indexOf( "mainscreen_gym") != -1) {
                resID = getResources().getIdentifier(field.getName(), "drawable", "com.starfireanimation.randomgym");
                gym_images.add(resID);
            }
            if (field.getName().indexOf( "mainscreen_home") != -1) {
                resID = getResources().getIdentifier(field.getName(), "drawable", "com.starfireanimation.randomgym");
                home_images.add(resID);
            }
            imgnum++;
        }
    }

    private int getRandomGymImageID() {
        int max, seed;

        if (ExerciseType.indexOf( "Home") != -1) {
            max = home_images.size();
        } else {
            max = gym_images.size();
        }
        seed = randomSeed.nextInt(max);

        return seed;
    }
}
