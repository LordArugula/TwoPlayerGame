package com.example.twoplayergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface Renderer {
    Bitmap getBitmap();
    void setBitmap(Bitmap bitmap);
    void render(Canvas canvas);
}
