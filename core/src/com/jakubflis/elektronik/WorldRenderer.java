package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.text.DecimalFormat;

public class WorldRenderer implements Disposable {
    private OrthographicCamera _camera;
    private OrthographicCamera _cameraGui;
    private SpriteBatch _batch;
    private WorldController _worldController;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private AssetsFonts assetsFonts = new AssetsFonts();

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
    }

    public void render() {
        renderTestObjects();
        renderCountdownTimer();
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

        _worldController.button.draw(_batch);

        _batch.end();
    }

    private void renderGui() {
        _batch.setProjectionMatrix(_cameraGui.combined);
        _batch.begin();

        assetsFonts.blowStrength.draw(_batch, "" + _worldController.blowStrength + "%",
                        -1.0f * Constants.VIEWPORT_GUI_WIDTH + 2.2f,
                        (Constants.VIEWPORT_GUI_HEIGHT / 2) - 1.0f);

        assetsFonts.percentageResult.draw(_batch, "" + new DecimalFormat("#.0").format(_worldController.percentageScore) + "%",
                        -1.0f * Constants.VIEWPORT_GUI_WIDTH + 2.2f,
                        -1.0f * (Constants.VIEWPORT_GUI_HEIGHT / 2) + 1.2f);

        assetsFonts.countdownTimer.draw(_batch, "" + new DecimalFormat("#.0").format(_worldController.secondsLeft) + "s", 0, (Constants.VIEWPORT_GUI_HEIGHT / 2) - 1.0f);

        if (_worldController.percentageScore == 100) {
            _worldController.isCountdownWorking = false;

            glyphLayout.setText(assetsFonts.finalResultString, "Wygrana!");
            assetsFonts.finalResultString.setColor(0, 1.0f, 0, 1.0f);
            assetsFonts.finalResultString.draw(_batch, glyphLayout,  -1 * glyphLayout.width / 2, glyphLayout.height / 2);
        }

        if ((int)_worldController.secondsLeft == 0) {
            _worldController.secondsLeft = 0;
            _worldController.isCountdownWorking = false;

            glyphLayout.setText(assetsFonts.finalResultString, "Przegrana!");
            assetsFonts.finalResultString.setColor(1.0f, 0, 0, 1.0f);
            assetsFonts.finalResultString.draw(_batch, glyphLayout,  -1 * glyphLayout.width / 2, glyphLayout.height / 2);
        }

        _batch.end();
    }

    private void renderCountdownTimer() {
        if (_worldController.isCountdownWorking) {
            _worldController.secondsLeft -= Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void dispose() {
        _batch.dispose();
    }
}
