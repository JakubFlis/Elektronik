package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

import java.text.DecimalFormat;

public class WorldRenderer implements Disposable {
    private OrthographicCamera _camera;
    private OrthographicCamera _cameraGui;
    private SpriteBatch _batch;
    private WorldController _worldController;

    BitmapFont font;
    BitmapFont fontResult;

    public WorldRenderer(WorldController _worldController) {
        this._worldController = _worldController;
        init();
    }

    private void init() {
        _batch = new SpriteBatch();
        _camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        _camera.position.set(0, 0, 0);
        _camera.update();

        _cameraGui = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        _cameraGui.update();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("images/Symtext.ttf"));
        font = createFont(generator, 16);
        fontResult = createFont(generator, 16);
    }

    public void render() {
        renderTestObjects();
        renderGui();
    }

    public void resize(int width, int height) {
        _camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        _camera.update();

        _cameraGui.viewportWidth = ((Constants.VIEWPORT_GUI_HEIGHT) / (float)height) * (float)width;
        _cameraGui.update();
    }

    private void renderTestObjects() {
        _worldController.cameraHelper.applyTo(_camera, _cameraGui);
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin();

        for (Sprite sprite : _worldController.testSprites) {
            sprite.draw(_batch);
        }

        _batch.end();
    }

    private void renderGui() {
        _batch.setProjectionMatrix(_cameraGui.combined);
        _batch.begin();

        font.setColor(0.3f, 1.0f, 1.0f, 1.0f);
        font.getData().setScale(.07f, .07f);
        font.draw(_batch, "" + _worldController.blowStrength + "%",
                        -1.0f * Constants.VIEWPORT_GUI_WIDTH + 2.2f,
                        (Constants.VIEWPORT_GUI_HEIGHT / 2) - 1.2f);

        fontResult.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        fontResult.getData().setScale(.07f, .07f);
        fontResult.draw(_batch, "" + new DecimalFormat("#.0").format(_worldController.percentageScore) + "%",
                        -1.0f * Constants.VIEWPORT_GUI_WIDTH + 2.2f,
                        -1.0f * (Constants.VIEWPORT_GUI_HEIGHT / 2) + 1.2f);

        _batch.end();
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, float dp)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(dp * Gdx.graphics.getDensity());

        return generator.generateFont(parameter);
    }

    @Override
    public void dispose() {
        _batch.dispose();
    }
}
