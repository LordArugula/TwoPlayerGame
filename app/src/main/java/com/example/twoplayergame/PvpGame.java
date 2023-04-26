package com.example.twoplayergame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PvpGame implements Game {

    private final MainThread mainThread;

    private final SurfaceView surfaceView;
    private final List<Character> characters;
    private final List<Projectile> activeProjectiles;
    private final ProjectilePoolProvider projectilePoolProvider;

    private final int screenWidth;
    private final int screenHeight;

    public PvpGame(GameActivity context, SurfaceView surfaceView, Player playerOne, Player playerTwo) {
        this.surfaceView = surfaceView;

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        characters = new ArrayList<>();
        activeProjectiles = new ArrayList<>();
        projectilePoolProvider = new ProjectilePoolProvider();
        projectilePoolProvider.setInstance(new ProjectilePool(this::trackProjectile));

        characters.add(playerOne);
        characters.add(playerTwo);

        ViewGroup playerOneViewGroup = context.findViewById(R.id.player_one_game_view);
        context.getLayoutInflater().inflate(R.layout.player_view, playerOneViewGroup);

        PlayerView playerOnePlayerView = new PlayerView(playerOne);
        playerOnePlayerView.bind(playerOneViewGroup, playerOne);

        ViewGroup playerTwoViewGroup = context.findViewById(R.id.player_two_game_view);
        context.getLayoutInflater().inflate(R.layout.player_view, playerTwoViewGroup);

        PlayerView playerTwoPlayerView = new PlayerView(playerTwo);
        playerTwoPlayerView.bind(playerTwoViewGroup, playerTwo);

        mainThread = new MainThread(PvpGame.this);
    }

    private void onCharacterDied(Character character) {

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

        renderGameEntities();
        handleCollisions();

        for (int i = characters.size() - 1; i >= 0; i--) {
            Character character = characters.get(i);
            if (character.getHealth() <= 0) {
                characters.remove(i);
                onCharacterDied(character);
            }
        }

        restrictPlayersToScreenBoundaries();
    }

    private void renderGameEntities() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                surfaceView.draw(canvas);
                for (Character character : characters) {
                    character.render(canvas);
                }

                for (Projectile projectile : activeProjectiles) {
                    projectile.render(canvas);
                }
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void handleCollisions() {
        ProjectilePool pool = projectilePoolProvider.getInstance();

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
                int damage = 0;
                if (projectile.getOwner() != character
                        && character.checkCollision(projectile)) {
                    damage += projectile.getDamage();
                    projectile.getOwner().addScore(1);

                    removeProjectile(i);
                    i--;
                    pool.release(projectile);
                }
                character.takeDamage(damage);
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

    private void restrictPlayersToScreenBoundaries() {
        for (Character character : characters) {
            Vector2 position = character.getPosition();
            Vector2 halfSize = character.getHalfSize();

            double x = position.x;
            double y = position.y;

            if (position.x - halfSize.x < 0) {
                x = halfSize.x;
            }

            if (position.y - halfSize.y < 0) {
                y = halfSize.y;
            }

            if (position.x + halfSize.x > screenWidth) {
                x = screenWidth - halfSize.x;
            }

            if (position.y + halfSize.y > screenHeight) {
                y = screenHeight - halfSize.y;
            }

            character.setPosition(new Vector2(x, y));
        }
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