package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class AssetsFonts {
    public final BitmapFont percentageResult;
    public final BitmapFont finalResultString;
    public final BitmapFont countdownTimer;
    public final BitmapFont blowStrength;

    public AssetsFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("images/Symtext.ttf"));

        blowStrength = createFont(generator, 16);
        blowStrength.setColor(0.3f, 1.0f, 1.0f, 1.0f);
        blowStrength.getData().setScale(.07f, .07f);

        percentageResult = createFont(generator, 16);
        percentageResult.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        percentageResult.getData().setScale(.07f, .07f);

        finalResultString = createFont(generator, 15);
        finalResultString.getData().setScale(.07f, .07f);

        countdownTimer = createFont(generator, 16);
        countdownTimer.setColor(0.3f, 1.0f, 1.0f, 1.0f);
        countdownTimer.getData().setScale(.07f, .07f);
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(dp * Gdx.graphics.getDensity());

        return generator.generateFont(parameter);
    }
}
