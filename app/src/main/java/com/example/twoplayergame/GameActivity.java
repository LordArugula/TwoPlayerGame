package com.example.twoplayergame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Game game;
    private MainThread gameThread;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenConfigs();
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences(ScoreUtils.SHARED_PREF_FILE, MODE_PRIVATE);

        SurfaceView surfaceView = findViewById(R.id.game_view);
        surfaceView.getHolder().addCallback(this);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_one);
        Bitmap projectileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.projectile_player);
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

        ViewGroup playerOneViewGroup = findViewById(R.id.player_one_game_view);
        PlayerView playerOnePlayerView = new PlayerView();
        playerOnePlayerView.bind(playerOneViewGroup, playerOne);

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

        ViewGroup playerTwoViewGroup = findViewById(R.id.player_two_game_view);
        playerTwoViewGroup.setRotation(180);
        PlayerView playerTwoPlayerView = new PlayerView();
        playerTwoPlayerView.bind(playerTwoViewGroup, playerTwo);

        Intent intent = getIntent();
        GameMode gameMode = (GameMode) intent.getSerializableExtra(String.valueOf(R.string.EXTRA_GAME_MODE));

        game = createGame(gameMode, surfaceView);
        game.setScreenSize(width, height);
        game.addCharacter(playerOne);
        game.addCharacter(playerTwo);
        game.setOnGameWonListener(() -> onGameWon(playerOne, playerTwo));
        game.setOnGameLostListener(() -> onGameLost(playerOne, playerTwo));

        gameThread = new MainThread(game);

        gameThread.start();
    }

    private void onGameLost(Player playerOne, Player playerTwo) {
        handler.post(() -> {
            GameEndFragment gameEndFragment = new GameEndFragment("Defeat!");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_view, gameEndFragment, null)
                    .setReorderingAllowed(true)
                    .commit();
            recordPlayerStats(playerOne, playerTwo);
        });
    }

    private void onGameWon(Player playerOne, Player playerTwo) {
        handler.post(() -> {
            GameEndFragment gameEndFragment = new GameEndFragment("Victory!");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_view, gameEndFragment, null)
                    .setReorderingAllowed(true)
                    .commit();

            recordPlayerStats(playerOne, playerTwo);
        });
    }

    private void recordPlayerStats(Player playerOne, Player playerTwo) {
        List<Score> scores = new ArrayList<>(2);
        scores.add(new Score(playerOne.getName(), playerOne.getScore()));
        scores.add(new Score(playerTwo.getName(), playerTwo.getScore()));
        ScoreUtils.addScores(sharedPreferences, scores);
    }

    private Game createGame(GameMode gameMode, SurfaceView surfaceView) {
        switch (gameMode) {
            case SOLO:
//                return new SoloGame();
                throw new UnsupportedOperationException();
            case PVP:
                return new PvpGame(surfaceView);
            case COOP:
                return new CoopGame(surfaceView, getResources());
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

    @SuppressLint("SourceLockedOrientationActivity")
    private void setScreenConfigs() {
        // removes title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // lock screen to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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