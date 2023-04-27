package com.example.twoplayergame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Game game;
    private MainThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenConfigs();
        setContentView(R.layout.activity_game);

        SurfaceView surfaceView = findViewById(R.id.game_view);
        surfaceView.getHolder().addCallback(this);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        LayoutInflater layoutInflater = getLayoutInflater();

        ViewGroup playerOneViewGroup = findViewById(R.id.player_one_game_view);
        layoutInflater.inflate(R.layout.player_view, playerOneViewGroup);

        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_one);
        Bitmap projectileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.projectile);
        Player playerOne = new CharacterBuilder()
                .withPosition(new Vector2(width * 0.5, height * 0.75))
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

        PlayerView playerOnePlayerView = new PlayerView();
        playerOnePlayerView.bind(playerOneViewGroup, playerOne);

        ViewGroup playerTwoViewGroup = findViewById(R.id.player_two_game_view);
        getLayoutInflater().inflate(R.layout.player_view, playerTwoViewGroup);

        Bitmap playerTwoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_two);
        Player playerTwo = new CharacterBuilder()
                .withPosition(new Vector2(width * 0.5, height * 0.25))
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

        PlayerView playerTwoPlayerView = new PlayerView();
        playerTwoPlayerView.bind(playerTwoViewGroup, playerTwo);

        Intent intent = getIntent();
        GameMode gameMode = (GameMode) intent.getSerializableExtra(String.valueOf(R.string.EXTRA_GAME_MODE));

        game = createGame(gameMode, surfaceView);

        game.addPlayer(playerOne);
        game.addPlayer(playerTwo);

        gameThread = new MainThread(game);

        game.setScreenSize(width, height);
        gameThread.start();
    }

    private Game createGame(GameMode gameMode, SurfaceView surfaceView) {
        switch (gameMode) {
            case SOLO:
//                return new SoloGame();
                throw new UnsupportedOperationException();
            case PVP:
                return new PvpGame(surfaceView);
            case COOP:
                return new CoopGame(surfaceView);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameThread.interrupt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.start();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d(GameActivity.class.getName(), "SurfaceCreated");
        game.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        game.stop();
    }

    private void setScreenConfigs() {
        // removes title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        // keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // hide status bars and navigation bars
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        decorView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                insetsController.hide(WindowInsetsCompat.Type.statusBars());
                insetsController.hide(WindowInsetsCompat.Type.navigationBars());
            }
            return windowInsets;
        });
    }
}