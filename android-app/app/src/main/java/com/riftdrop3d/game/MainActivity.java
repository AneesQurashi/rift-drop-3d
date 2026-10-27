package com.riftdrop3d.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;

/** Fullscreen, landscape, offline WebView container for RIFT DROP 3D. */
public final class MainActivity extends Activity {
    private WebView gameView;
    private int gamepadDirection = 0;

    private void immersive() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersive();
        gameView = new WebView(this);
        gameView.setBackgroundColor(Color.rgb(5, 9, 16));
        gameView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gameView.setWebChromeClient(new WebChromeClient());
        gameView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !"file".equalsIgnoreCase(request.getUrl().getScheme());
            }
        });
        gameView.getSettings().setJavaScriptEnabled(true);
        gameView.getSettings().setDomStorageEnabled(true);
        gameView.getSettings().setAllowFileAccess(true);
        gameView.getSettings().setAllowContentAccess(false);
        gameView.getSettings().setAllowFileAccessFromFileURLs(false);
        gameView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        setContentView(gameView);
        gameView.loadUrl("file:///android_asset/index.html");
    }

    private void sendKey(String type, String key) {
        if (gameView != null) {
            gameView.evaluateJavascript("window.dispatchEvent(new KeyboardEvent('" + type +
                    "',{key:'" + key + "',bubbles:true}));", null);
        }
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        int code = event.getKeyCode();
        String key = null;
        switch (code) {
            case KeyEvent.KEYCODE_BUTTON_A: key = "Enter"; break;
            case KeyEvent.KEYCODE_BUTTON_B: key = "r"; break;
            case KeyEvent.KEYCODE_BUTTON_START: key = "Enter"; break;
            case KeyEvent.KEYCODE_DPAD_LEFT: key = "ArrowLeft"; break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: key = "ArrowRight"; break;
            default: break;
        }
        if (key != null && (event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            sendKey(event.getAction() == KeyEvent.ACTION_DOWN ? "keydown" : "keyup", key);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {
            float axis = event.getAxisValue(MotionEvent.AXIS_X);
            float hat = event.getAxisValue(MotionEvent.AXIS_HAT_X);
            if (Math.abs(hat) > Math.abs(axis)) axis = hat;
            int next = axis < -0.2f ? -1 : axis > 0.2f ? 1 : 0;
            if (gamepadDirection != next) {
                if (gamepadDirection == -1) sendKey("keyup", "ArrowLeft");
                if (gamepadDirection == 1) sendKey("keyup", "ArrowRight");
                if (next == -1) sendKey("keydown", "ArrowLeft");
                if (next == 1) sendKey("keydown", "ArrowRight");
                gamepadDirection = next;
            }
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    @Override public void onBackPressed() { sendKey("keydown", "Escape"); }
    @Override protected void onResume() { super.onResume(); immersive(); if (gameView != null) gameView.onResume(); }
    @Override protected void onPause() { if (gameView != null) gameView.onPause(); super.onPause(); }
    @Override protected void onDestroy() { if (gameView != null) { gameView.destroy(); gameView=null; } super.onDestroy(); }
}
