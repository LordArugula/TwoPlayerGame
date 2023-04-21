package com.example.twoplayergame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.game_view);
        surfaceView.getHolder().addCallback(this);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_one);
        Bitmap projectileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.projectile);
        Player playerOne = new CharacterBuilder()
                .withPosition(new Vector2(screenWidth * 0.5, screenHeight * 0.75))
                .withBitmap(playerBitmap)
                .withHealth(10)
                .withScore(0)
                .withMovementSpeed(200)
                .withName("Player One")
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withProjectile(new ProjectileData(projectileBitmap, 100, 1), 3)
                        .withFireSpeed(1)
                        .withArcDegrees(30)
                        .withRadius(1)
                        .build())
                .buildPlayer();

        Bitmap playerTwoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_two);
        Player playerTwo = new CharacterBuilder()
                .withPosition(new Vector2(screenWidth * 0.5, screenHeight * 0.25))
                .withBitmap(playerTwoBitmap)
                .withHealth(10)
                .withScore(0)
                .withRotation(180)
                .withMovementSpeed(250)
                .withName("Player Two")
                .withProjectileSpawner(new ProjectileSpawnerBuilder()
                        .withProjectile(new ProjectileData(projectileBitmap, 150, 1), 2)
                        .withFireSpeed(1.25)
                        .withArcDegrees(15)
                        .withRadius(1)
                        .build())
                .buildPlayer();

        game = new PvpGame(this, surfaceView, playerOne, playerTwo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.stop();
    }

    private void setFullscreen() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        decorView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                    || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                insetsController.hide(WindowInsetsCompat.Type.statusBars());
                insetsController.hide(WindowInsetsCompat.Type.navigationBars());
            }
            return windowInsets;
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        game.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        game.stop();
    }
}