package com.example.pankaj.face_recong_mlkit.Overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RectOver extends GraphicOverlay.Graphic {

    private int red = Color.RED;
    private float stroke = 4.0f;
    private Paint paint;
    GraphicOverlay graphicOverlay;
    private Rect rect;

    public RectOver(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        paint = new Paint();
     paint.setColor(red);
     paint.setStyle(Paint.Style.STROKE);
     paint.setStrokeWidth(stroke);

     this.graphicOverlay = graphicOverlay;
     this.rect = rect;
       postInvalidate();

    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateY(rectF.top);
        rectF.bottom = translateY(rectF.bottom);


        canvas.drawRect(rectF,paint);



    }
}
