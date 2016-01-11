package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.text.DecimalFormat;

/**
 * Obszerna klasa renderująca obiekty zdefiniowane w klasie WorldController.
 *
 * @see WorldController
 *
 * Definiuje skalę i umiejscowienie na ekranie obiektów.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera _camera;
    private OrthographicCamera _cameraGui;
    private SpriteBatch _batch;
    private WorldController _worldController;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private AssetsFonts assetsFonts = new AssetsFonts();
    private long startTime = java.lang.System.currentTimeMillis();

    public WorldRenderer(WorldController _worldController) {
        this._worldController = _worldController;
        init();
    }

    /**
     * Funckja inicjalizuje pola tej klasy.
     * Jest wyłączona z konstruktora ze względu na częste ponowne używanie w sytuacji
     * bez potrzeby reinicjalizacji samego WorldRenderera.
     */
    private void init() {
        _batch = new SpriteBatch();
        _camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        _camera.position.set(0, 0, 0);
        _camera.update();

        _cameraGui = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        _cameraGui.update();
    }

    /**
     * Łączy wszystkie metody renderowania obiektów graficznych.
     */
    public void render() {
        renderBoardObjects();
        renderCountdownTimer();
        renderGui();
    }

    /**
     * Odpowiada za renderowanie elementów menu.
     */
    public void renderMenu() {
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin();

        _worldController.menu.draw(_batch);

        _batch.end();
    }

    /**
     * Reaguje na akcję zmiany wielkości ekranu.
     */
    public void resize(int width, int height) {
        _camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        _camera.update();

        _cameraGui.viewportWidth = ((Constants.VIEWPORT_GUI_HEIGHT) / (float)height) * (float)width;
        _cameraGui.update();
    }

    /**
     * Odpowiada za renderowanie kafelków planszy.
     */
    private void renderBoardObjects() {
        _worldController.cameraHelper.applyTo(_camera, _cameraGui);
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin();

        _worldController.board.draw(_batch);

        for (Sprite sprite : _worldController.currentSprites) {
            sprite.draw(_batch);
        }

        _batch.end();
    }

    /**
     * Odpowiada za renderowanie elementów interfejsu graficznego.
     */
    private void renderGui() {
        _batch.setProjectionMatrix(_cameraGui.combined);
        _batch.begin();

        assetsFonts.blowStrength.draw(_batch, "B:" + _worldController.blowStrength + "%",
                        -1.0f * Constants.VIEWPORT_GUI_WIDTH + 2.2f,
                        (Constants.VIEWPORT_GUI_HEIGHT / 2) - 0.2f);

        assetsFonts.percentageResult.draw(_batch, "W:" + new DecimalFormat("0.0").format(_worldController.percentageScore) + "%",
                         -4.5f,
                        (Constants.VIEWPORT_GUI_HEIGHT / 2) - 0.2f);

        assetsFonts.countdownTimer.draw(_batch, "Cz:" + new DecimalFormat("#.0").format(_worldController.secondsLeft) + "s",
                3.2f,
                (Constants.VIEWPORT_GUI_HEIGHT / 2) - 0.2f);

        assetsFonts.levelNumber.draw(_batch, "lvl " + (_worldController.currentLevel + 1),
                -6.2f,
                -3.7f);

        if (_worldController.percentageScore == 100) {
            _worldController.isCountdownWorking = false;
            _worldController.gameState = GameScreen.GameState.GAME_WON;

            glyphLayout.setText(assetsFonts.finalResultString, "Sukces!");
            assetsFonts.finalResultString.setColor(0, 1.0f, 0, 1.0f);
            assetsFonts.finalResultString.draw(_batch, glyphLayout,  -1 * glyphLayout.width / 2, 3.2f);

            _worldController.exitScreen.draw(_batch);
        }

        if ((int)_worldController.secondsLeft == 0) {
            _worldController.secondsLeft = 0;
            _worldController.isCountdownWorking = false;
            _worldController.gameState = GameScreen.GameState.GAME_LOST;

            glyphLayout.setText(assetsFonts.finalResultString, "Koniec czasu!");
            assetsFonts.finalResultString.setColor(1.0f, 0, 0, 1.0f);
            assetsFonts.finalResultString.draw(_batch, glyphLayout,  -1 * glyphLayout.width / 2, 3.2f);

            _worldController.exitScreen.draw(_batch);
        }

        _batch.end();
    }

    /**
     * Odpowiada za renderowanie licznika czasu.
     */
    private void renderCountdownTimer() {
        if (_worldController.isCountdownWorking) {
            _worldController.secondsLeft -= Gdx.graphics.getDeltaTime();
        }

        HighscoreManager.totalTime = ((System.currentTimeMillis() - startTime) / 1000.0f);
    }

    @Override
    public void dispose() {
        _batch.dispose();
    }
}
