package com.ace_project.ace_hud;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class HUDActivity extends AppCompatActivity {


    private TextView noRadarIndicator, lockIndicator, speedIndicator, altitudeIndicator, tacticalModeIndicator, controlModeIndicator, targetingPodMode;
    private ImageView altitudeDoubleCrossCue;
    private Thread indicatorControlThread;
    private Handler eventHandler;
    private Message controlMessage;
    private String commandMessage;
    private String targetIndicator;
    private boolean indicatorFlashing = false;
    private boolean enableIndicators = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hud);


        eventHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                commandMessage = message.obj.toString();
                switch(commandMessage){

                    case "TEST-ALL-INDICATORS":
                        if(indicatorFlashing){
                            indicatorFlashing = false;
                            noRadarIndicator.setVisibility(View.VISIBLE);
                            lockIndicator.setVisibility(View.VISIBLE);
                            speedIndicator.setVisibility(View.VISIBLE);
                            altitudeIndicator.setVisibility(View.VISIBLE);
                            tacticalModeIndicator.setVisibility(View.VISIBLE);
                            controlModeIndicator.setVisibility(View.VISIBLE);
                            targetingPodMode.setVisibility(View.VISIBLE);
                            altitudeDoubleCrossCue.setVisibility(View.VISIBLE);
                        }else{
                            indicatorFlashing = true;
                            noRadarIndicator.setVisibility(View.INVISIBLE);
                            lockIndicator.setVisibility(View.INVISIBLE);
                            speedIndicator.setVisibility(View.INVISIBLE);
                            altitudeIndicator.setVisibility(View.INVISIBLE);
                            tacticalModeIndicator.setVisibility(View.INVISIBLE);
                            controlModeIndicator.setVisibility(View.INVISIBLE);
                            targetingPodMode.setVisibility(View.INVISIBLE);
                            altitudeDoubleCrossCue.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case "TEST-WARNING-INDICATORS":
                        if(indicatorFlashing){
                            indicatorFlashing = false;
                            noRadarIndicator.setVisibility(View.VISIBLE);
                            lockIndicator.setVisibility(View.VISIBLE);
                            speedIndicator.setVisibility(View.VISIBLE);
                            altitudeIndicator.setVisibility(View.VISIBLE);
                            tacticalModeIndicator.setVisibility(View.VISIBLE);
                            controlModeIndicator.setVisibility(View.VISIBLE);
                            targetingPodMode.setVisibility(View.VISIBLE);
                        }else{
                            indicatorFlashing = true;
                            noRadarIndicator.setVisibility(View.INVISIBLE);
                            lockIndicator.setVisibility(View.INVISIBLE);
                            speedIndicator.setVisibility(View.INVISIBLE);
                            altitudeIndicator.setVisibility(View.INVISIBLE);
                            tacticalModeIndicator.setVisibility(View.INVISIBLE);
                            controlModeIndicator.setVisibility(View.INVISIBLE);
                            targetingPodMode.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case "TEST-CROSS-CUE-INDICATORS":
                        if(indicatorFlashing){
                            indicatorFlashing = false;
                            altitudeDoubleCrossCue.setVisibility(View.VISIBLE);
                        }else{
                            indicatorFlashing = true;
                            altitudeDoubleCrossCue.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case "NO-OPERATION":

                    default:
                        break;
                }
            }
        };

        noRadarIndicator = findViewById(R.id.radar_status);
        lockIndicator = findViewById(R.id.lock_status);
        speedIndicator = findViewById(R.id.speed_value);
        altitudeIndicator = findViewById(R.id.altitude_warning);
        tacticalModeIndicator = findViewById(R.id.tactical_mode);
        controlModeIndicator = findViewById(R.id.control_mode);
        targetingPodMode = findViewById(R.id.tgp_mode);
        altitudeDoubleCrossCue = findViewById(R.id.altitude_cross_cue);

        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView coreHUDView = findViewById(R.id.webView);
        coreHUDView.getSettings().setJavaScriptEnabled(true);
        coreHUDView.loadUrl("http://127.0.0.1:8000/");

        indicatorControlThread = new Thread(() -> {
            if(enableIndicators) {
                try {
                    Thread.sleep(3000);
                } catch (Exception ignored) {
                }

                while (true) {
                    try {
                        controlMessage = new Message();
                        controlMessage.obj = targetIndicator;
                        eventHandler.sendMessage(controlMessage);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        Log.e("[ACE-HUD: ERROR] > ", e.toString());
                    }
                }
            }
        });


        CONTROL_INDICATORS(View.INVISIBLE);
        //TEST_INDICATORS("TEST-CROSS-CUE-INDICATORS", true);

    }

    private void TEST_INDICATORS(String indicatorMode, boolean controlFlag) {
        enableIndicators = controlFlag;
        targetIndicator = indicatorMode;
        indicatorControlThread.start();
    }

    private void CONTROL_INDICATORS(int mode) {
        noRadarIndicator.setVisibility(mode);
        lockIndicator.setVisibility(mode);
        speedIndicator.setVisibility(mode);
        altitudeIndicator.setVisibility(mode);
        tacticalModeIndicator.setVisibility(mode);
        controlModeIndicator.setVisibility(mode);
        targetingPodMode.setVisibility(mode);
    }
}