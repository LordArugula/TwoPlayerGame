package com.example.twoplayergame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CoopGame implements Game {

    private final MainThread mainThread;

    private final SurfaceView surfaceView;
    private final List<Character> characters;
    private final List<Projectile> activeProjectiles;
    private final ProjectilePoolProvider projectilePoolProvider;

    private Character playerOne;
    private Character playerTwo;
    private int screenWidth;
    private int screenHeight;

    public CoopGame(MainActivity context, SurfaceView surfaceView, Player playerOne, Player playerTwo) {
        this.surfaceView = surfaceView;

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        characters = new ArrayList<>();
        activeProjectiles = new ArrayList<>();
        projectilePoolProvider = new ProjectilePoolProvider();
        projectilePoolProvider.setInstance(new ProjectilePool(this::trackProjectile));

        characters.add(playerOne);
        characters.add(playerTwo);

        playerOne.addOnCharacterDiedListener(this::onCharacterDied);

        ViewGroup playerOneViewGroup = context.findViewById(R.id.player_one_game_view);
        context.getLayoutInflater().inflate(R.layout.game_view, playerOneViewGroup);

        PlayerView playerOnePlayerView = new PlayerView(playerOne);
        playerOnePlayerView.bind(playerOneViewGroup, playerOne);

        ViewGroup playerTwoViewGroup = context.findViewById(R.id.player_two_game_view);
        context.getLayoutInflater().inflate(R.layout.game_view, playerTwoViewGroup);

        PlayerView playerTwoPlayerView = new PlayerView(playerTwo);
        playerTwoPlayerView.bind(playerTwoViewGroup, playerTwo);

        mainThread = new MainThread(CoopGame.this);
    }

    private void onCharacterDied(Character character) {
        characters.remove(character);
    }

    private void trackProjectile(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    @Override
    public void update(double deltaTime, double time) {
//        Log.d("Co-op Game", String.format("DeltaTime: %f s", deltaTime));

        // do game logic
        for (Character character : characters) {
            character.update(deltaTime, time);
        }
        for (Projectile projectile : activeProjectiles) {
            projectile.update(deltaTime, time);
        }

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            surfaceView.draw(canvas);
            for (Character character : characters) {
                character.render(canvas);
            }

            for (Projectile projectile : activeProjectiles) {
                projectile.render(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        ProjectilePool pool = projectilePoolProvider.getInstance();

        // do physics
        for (int i = 0; i < activeProjectiles.size(); i++) {
            Projectile projectile = activeProjectiles.get(i);

            if (projectile.getMax().x < 0
                    || projectile.getMax().y < 0
                    || projectile.getMin().x > screenWidth
                    || projectile.getMin().y > screenHeight) {
                removeProjectile(i);
                i--;
                pool.release(projectile);
                continue;
            }

            for (Character character : characters) {
                if (!(projectile.getOwner() instanceof Player)
                        && character.checkCollision(projectile)) {
                    character.takeDamage(projectile.getDamage());

                    removeProjectile(i);
                    i--;
                    pool.release(projectile);
                }
            }
        }
    }

    private void removeProjectile(int i) {
        // remove projectile by swapping it with the last
        // projectile in the list (order does not matter)
        // then removing the last element
        Projectile lastProjectile = activeProjectiles.get(activeProjectiles.size() - 1);
        activeProjectiles.set(i, lastProjectile);
        activeProjectiles.remove(activeProjectiles.size() - 1);
    }

    @Override
    public void start() {
        mainThread.start();
    }

    @Override
    public void stop() {
        mainThread.quit();
    }
}