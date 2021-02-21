package com.duolacloud.xsocial.core.model;

import android.content.Context;
import android.graphics.Bitmap;

public class XImageMark extends XWaterMark {
    private Bitmap mMarkBitmap;

    public void bringToFront() {
        super.bringToFront();
    }

    public Bitmap compound(Bitmap bitmap) {
        return super.compound(bitmap);
    }

    public void setAlpha(float f) {
        super.setAlpha(f);
    }

    public void setContext(Context context) {
        super.setContext(context);
    }

    public void setGravity(int i) {
        super.setGravity(i);
    }

    public void setMargins(int i, int i2, int i3, int i4) {
        super.setMargins(i, i2, i3, i4);
    }

    public void setRotate(int i) {
        super.setRotate(i);
    }

    public void setScale(float f) {
        super.setScale(f);
    }

    public void setTransparent() {
        super.setTransparent();
    }

    public void setMarkBitmap(Bitmap bitmap) {
        this.mMarkBitmap = bitmap;
    }

    Bitmap getMarkBitmap() {
        return this.mMarkBitmap;
    }
}
