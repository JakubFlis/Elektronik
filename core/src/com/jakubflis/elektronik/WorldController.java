package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;

public class WorldController extends InputAdapter {
    public CameraHelper cameraHelper;
    public BoardPointSprite[] currentSprites;
    public AssetsFonts fonts;
    public Sprite button;
    public Sprite menu;
    public int selectedSprite;
    public int blowStrength;
    public float secondsLeft;
    public float percentageScore;
    public boolean isCountdownWorking;
    public GameScreen.GameState gameState;
    public int currentLevel = 0;
    private JFElektronik _game;
    private int _totalCollected;
    private int _totalCollectables;
    private Vector3 _lastDragCoords;
    private List<int[][]> _gameLevels;
    private static final String TAG = WorldController.class.getName();
    private Vector3 testVector;

    public WorldController(JFElektronik game) {
        _game = game;
        init();
    }

    public void init() {
        cameraHelper = new CameraHelper();
        _gameLevels = LevelBoards.getLevelArrays();
        initSprites();
        fonts = new AssetsFonts();
        blowStrength = 0;
        _totalCollected = 0;
        _totalCollectables = getNumberOfClickableTiles(currentSprites);
        percentageScore = 0;
        _lastDragCoords = new Vector3(0, 0, 0);
        secondsLeft = LevelBoards.levelDuration[currentLevel];
        isCountdownWorking = true;
        gameState = GameScreen.GameState.GAME_ON;
        Gdx.input.setInputProcessor(this);
    }

    private void initSprites() {
        initTestObjects();
        initButtons();
        initMainMenu();
    }

    private void reset() {
        blowStrength = 0;
        percentageScore = 0;
        gameState = GameScreen.GameState.GAME_ON;
        isCountdownWorking = true;
        secondsLeft = LevelBoards.levelDuration[currentLevel];
        _totalCollected = 0;

        initSprites();

        _totalCollectables = getNumberOfClickableTiles(currentSprites);
    }

    public void update (float deltaTime) {
        handleInputDigit(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void initTestObjects() {
        int[][] gameBoard = _gameLevels.get(currentLevel);
        int numberOfArrayRows = gameBoard.length;
        int numberOfArrayCols = gameBoard[0].length;

        currentSprites = new BoardPointSprite[numberOfArrayCols * numberOfArrayRows];

        Vector2 startingPosition = BoardPointHelper.getFirstBoardPointCoords(gameBoard);

        float tempPosition = startingPosition.x;
        float tempYPosition = startingPosition.y;
        int counter = 0;

        for (int[] a_testBoard : gameBoard) {

            for (int i = 0; i < numberOfArrayCols; i++) {
                BoardPointSprite sprite;
                String pointTextureAsset = BoardPointHelper.getBoartPointAssetName(a_testBoard[i]);

                if (pointTextureAsset != null) {
                    sprite = new BoardPointSprite(new Texture(pointTextureAsset));
                    sprite.isCollectable = true;
                } else {
                    sprite = new BoardPointSprite(createBlankBoardPointTexture());
                    sprite.isCollectable = false;
                }

                sprite.isCollected = false;
                sprite.isOverflowedTile = BoardPointHelper.getBoartPointOverflowedStatus(a_testBoard[i]);
                sprite.setSize(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
                sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
                sprite.setPosition(tempPosition, tempYPosition);

                currentSprites[counter++] = sprite;
                tempPosition += Constants.SQUARE_SIZE;
            }

            tempYPosition -= Constants.SQUARE_SIZE;
            tempPosition = startingPosition.x;
        }

        selectedSprite = 0;
    }

    private void initMainMenu() {
        menu = new Sprite(new Texture("logo.png"));
        menu.setSize(7, 5);
        menu.setOrigin(menu.getWidth() / 2.0f, menu.getHeight() / 2.0f);
        menu.setPosition(-3.6f, -2.5f);
    }

    private void initButtons() {
        Pixmap pixmap = new Pixmap(32, 32, Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, 32, 32);
        pixmap.drawLine(32, 0, 0, 32);
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, 32, 32);

        button = new Sprite(new Texture(pixmap));
        button.setSize(1, 1);
        button.setOrigin(button.getWidth() / 2.0f, button.getHeight() / 2.0f);
        button.setPosition(0, 0);
    }

    private Texture createBlankBoardPointTexture() {
        Pixmap pixmap = new Pixmap(Constants.SPRITE_WIDTH, Constants.SPRITE_HEIGHT, Format.RGBA8888);
        pixmap.setColor(38.0f / 255.0f, 102.0f / 255.0f, 41.0f / 255.0f, 0);
        pixmap.fill();

        return new Texture(pixmap);
    }

    private int getNumberOfClickableTiles(BoardPointSprite[] spriteTable) {
        int counter = 0;

        for (BoardPointSprite sprite : spriteTable) {
            counter += (sprite.isCollectable ? 1 : 0);
        }

        return counter;
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.R:
                init();

                break;
            case Keys.SPACE:
                selectedSprite = (selectedSprite + 1) % currentSprites.length;

                if (cameraHelper.hasTarget()) {
                    cameraHelper.setTarget(currentSprites[selectedSprite]);
                }

                break;
            case Keys.ENTER:
                manageLevels(gameState);

                break;
            case Keys.ESCAPE:
                Gdx.app.exit();

                break;
            case Keys.S:
                HighscoreManager.saveHighscoreFile(percentageScore);

                break;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (cameraHelper.camera == null || gameState != GameScreen.GameState.GAME_ON) {
            return false;
        }

        _lastDragCoords.x = screenX;
        _lastDragCoords.y = screenY;
        cameraHelper.camera.unproject(_lastDragCoords);

        for (BoardPointSprite sprite : currentSprites) {
            collectIfCollectable(sprite, _lastDragCoords.x, _lastDragCoords.y, false);
            checkOveflowedBoardTiles(_lastDragCoords);
        }

        return false;
    }

    public void checkOveflowedBoardTiles(Vector3 baseTouchCoords) {
        if (blowStrength >= 25 && blowStrength < 50) {

            for (BoardPointSprite sprite : currentSprites) {
                //vertical:
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y + Constants.SQUARE_SIZE, true);
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y - Constants.SQUARE_SIZE, true);
                //horizontal:
                collectIfCollectable(sprite, baseTouchCoords.x + Constants.SQUARE_SIZE, baseTouchCoords.y, true);
                collectIfCollectable(sprite, baseTouchCoords.x - Constants.SQUARE_SIZE, baseTouchCoords.y, true);
            }
        } else if (blowStrength >= 50 && blowStrength < 75) {
            for (BoardPointSprite sprite : currentSprites) {
                //vertical:
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y + 2 * Constants.SQUARE_SIZE, true);
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y + Constants.SQUARE_SIZE, true);
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y - Constants.SQUARE_SIZE, true);
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y - 2 * Constants.SQUARE_SIZE, true);
                //horizontal:
                collectIfCollectable(sprite, baseTouchCoords.x + 2 * Constants.SQUARE_SIZE, baseTouchCoords.y, true);
                collectIfCollectable(sprite, baseTouchCoords.x + Constants.SQUARE_SIZE, baseTouchCoords.y, true);
                collectIfCollectable(sprite, baseTouchCoords.x - Constants.SQUARE_SIZE, baseTouchCoords.y, true);
                collectIfCollectable(sprite, baseTouchCoords.x - 2 * Constants.SQUARE_SIZE, baseTouchCoords.y, true);
            }
        }
    }

    private void collectIfCollectable(BoardPointSprite sprite, float x, float y, boolean isOverflowedAllowed) {
        if (sprite.getBoundingRectangle().contains(x, y)) {

            if (sprite.isOverflowedTile != isOverflowedAllowed) {
                return;
            }

            if (sprite.isCollectable && !sprite.isCollected) {
                sprite.setColor(Color.CHARTREUSE);
                sprite.isCollected = true;
                _totalCollected++;

                percentageScore = computeCopletePercentage();
            }
        }
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount > 0 && blowStrength < 100) {
            blowStrength++;
        } else if (amount < 0 && blowStrength > 0) {
            blowStrength--;
        }

        checkOveflowedBoardTiles(_lastDragCoords);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (cameraHelper.camera == null) {
            return false;
        }

        Vector3 draggedTouch = new Vector3(screenX, screenY, 0);
        cameraHelper.camera.unproject(draggedTouch);

        //Gdx.app.debug(TAG, "Coords: " + draggedTouch.x + " " + draggedTouch.y);
        if (HighscoreManager.tempBlowingTime != null) {
            float diff = (System.currentTimeMillis() - HighscoreManager.tempBlowingTime) / 1000.0f;
            HighscoreManager.totalBlowingTime += diff;

            Gdx.app.debug(TAG, "Total elapsewd: " + HighscoreManager.totalBlowingTime + " Diff " + diff + " total app time: " + HighscoreManager.totalTime);
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (cameraHelper.camera == null) {
            return false;
        }

        testVector = new Vector3(screenX, screenY, 0);
        cameraHelper.camera.unproject(testVector);

        HighscoreManager.tempBlowingTime = System.currentTimeMillis();

        return false;
    }

    public float computeCopletePercentage() {
        return ((float)_totalCollected / (float)_totalCollectables) * 100;
    }

    private void manageLevels(GameScreen.GameState gameState) {
        switch (gameState) {
            case GAME_LOST:
                Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed /
                        255.0f, 0xff / 255.0f);
                Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

                currentLevel = 0;

                reset();

                _game.worldRenderer.render();

                break;
            case GAME_WON:
                Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed /
                        255.0f, 0xff / 255.0f);
                Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

                if ((currentLevel + 1) < _gameLevels.size()) {
                    currentLevel++;

                    reset();

                    _game.worldRenderer.render();
                } else {
                    _game.worldRenderer.renderMenu();
                }

                break;
            default:
                Gdx.app.debug("status", "Game's still on...");
                break;
        }
    }

    private void handleInputDigit(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            return;
        }

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            camMoveSpeed *= camMoveSpeedAccelerationFactor;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            moveCamera(-camMoveSpeed, 0);
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            moveCamera(camMoveSpeed, 0);
        }

        if (Gdx.input.isKeyPressed(Keys.UP)) {
            moveCamera(0, camMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            moveCamera(0, -camMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
            cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        }

        if (Gdx.input.isKeyPressed(Keys.COMMA)) {
            cameraHelper.addZoom(camZoomSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
            cameraHelper.addZoom(-camZoomSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.SLASH)) {
            cameraHelper.setZoom(1);
        }

        //Blow simulator
        if (Gdx.input.isKeyPressed(Keys.X) && blowStrength < 100) {
            blowStrength++;

            checkOveflowedBoardTiles(_lastDragCoords);
        }

        if (Gdx.input.isKeyPressed(Keys.Z) && blowStrength > 0) {
            blowStrength--;

            checkOveflowedBoardTiles(_lastDragCoords);
        }
    }
}
