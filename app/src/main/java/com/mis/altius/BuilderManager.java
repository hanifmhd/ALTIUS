package com.mis.altius;

/**
 * Created by Hanifmhd on 9/11/2017.
 */

import android.util.Pair;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiping Huang at 23:44 on 16/11/21
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */
public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.favorite,
            R.drawable.histori,
            R.drawable.about
    };

    private static int[] textResources = new int[]{
            R.string.text_favorit,
            R.string.text_histori,
            R.string.text_about
    };

    private static int textResourceIndex = 0;
    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static int getTextResource() {
        if (textResourceIndex >= textResources.length) textResourceIndex = 0;
        return textResources[textResourceIndex++];
    }

    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_inside_circle_button_text_normal);
    }
    static SimpleCircleButton.Builder getSimpleCircleButtonBuilder() {
        return new SimpleCircleButton.Builder()
                .normalImageRes(getImageResource());
    }
    static TextOutsideCircleButton.Builder getTextOutsideCircleButtonBuilder() {
        return new TextOutsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(getTextResource());
    }

    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}