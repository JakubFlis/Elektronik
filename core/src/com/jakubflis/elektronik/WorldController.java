package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class WorldController extends InputAdapter {
    public CameraHelper cameraHelper;
    public BoardPointSprite[] testSprites;
    public AssetsFonts fonts;
    public int selectedSprite;
    public int blowStrength;
    public float percentageScore;
    private int _totalCollected;
    private int _totalCollectables;
    private static final String TAG = WorldController.class.getName();
    private Vector3 _lastDragCoords;
    private int[][] _gameBoard = new int[][]{
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 1, 1, 1, 1, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 8, 8, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 13, 1, 1, 1, 1, 4, 0, 0, 0, 0, 0 },
            { 0, 12, 1, 1, 1, 1, 2, 10, 10, 3, 1, 13, 5, 13, 1, 1, 13, 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 9, 9, 0, 6, 1, 1, 5, 0, 13, 1, 1, 4, 0, 0, 0, 0, 15, 10, 16, 6, 1, 12, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 13, 4, 14, 0, 0, 0, 0, 0, 18, 0, 14, 0, 0, 0 },
            { 0, 12, 1, 1, 1, 4, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 14, 7, 13, 1, 1, 4, 0, 14, 0, 14, 0, 12, 0 },
            { 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 14, 0, 14, 0, 14, 0, 14, 0 },
            { 0, 0, 0, 0, 0, 7, 13, 1, 13, 0, 13, 0, 8, 8, 0, 0, 0, 14, 0, 0, 0, 0, 13, 0, 13, 0, 13, 0, 14, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 19, 19, 0, 0, 0, 14, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 14, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 7, 1, 1, 2, 10, 10, 3, 1, 1, 11, 1, 2, 10, 3, 1, 1, 1, 1, 1, 1, 5, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 19, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    private Vector3 testVector;

    public WorldController() {
        init();
    }

    public void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        fonts = new AssetsFonts();
        blowStrength = 0;
        initTestObjects();
        _totalCollected = 0;
        _totalCollectables = getNumberOfClickableTiles(testSprites);
        percentageScore = 0;
        _lastDragCoords = new Vector3(0, 0, 0);
    }

    public void update (float deltaTime) {
        handleInputDigit(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void initTestObjects() {
        int numberOfArrayRows = _gameBoard.length;
        int numberOfArrayCols = _gameBoard[0].length;

        testSprites = new BoardPointSprite[numberOfArrayCols * numberOfArrayRows];

        Vector2 startingPosition = BoardPointHelper.getFirstBoardPointCoords(_gameBoard);

        float tempPosition = startingPosition.x;
        float tempYPosition = startingPosition.y;
        int counter = 0;

        for (int[] a_testBoard : _gameBoard) {

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

                testSprites[counter++] = sprite;
                tempPosition += Constants.SQUARE_SIZE;
            }

            tempYPosition -= Constants.SQUARE_SIZE;
            tempPosition = startingPosition.x;
        }

        selectedSprite = 0;
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
                selectedSprite = (selectedSprite + 1) % testSprites.length;

                if (cameraHelper.hasTarget()) {
                    cameraHelper.setTarget(testSprites[selectedSprite]);
                }

                break;
            case Keys.ENTER:
                cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]);

                break;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        _lastDragCoords.x = screenX;
        _lastDragCoords.y = screenY;
        cameraHelper.camera.unproject(_lastDragCoords);

        for (BoardPointSprite sprite : testSprites) {
            collectIfCollectable(sprite, _lastDragCoords.x, _lastDragCoords.y, false);
            checkOveflowedBoardTiles(_lastDragCoords);
        }

        return false;
    }

    public void checkOveflowedBoardTiles(Vector3 baseTouchCoords) {
        if (blowStrength >= 25 && blowStrength < 50) {

            for (BoardPointSprite sprite : testSprites) {
                //vertical:
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y + Constants.SQUARE_SIZE, true);
                collectIfCollectable(sprite, baseTouchCoords.x, baseTouchCoords.y - Constants.SQUARE_SIZE, true);
                //horizontal:
                collectIfCollectable(sprite, baseTouchCoords.x + Constants.SQUARE_SIZE, baseTouchCoords.y, true);
                collectIfCollectable(sprite, baseTouchCoords.x - Constants.SQUARE_SIZE, baseTouchCoords.y, true);
            }
        } else if (blowStrength >= 50 && blowStrength < 75) {
            for (BoardPointSprite sprite : testSprites) {
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
        Vector3 draggedTouch = new Vector3(screenX, screenY, 0);
        cameraHelper.camera.unproject(draggedTouch);

        Gdx.app.debug(TAG, "Coords: " + draggedTouch.x + " " + draggedTouch.y);

        //Gdx.app.debug(TAG, "Difference: " + (draggedTouch.x - testVector.x) + " " + (draggedTouch.y - testVector.y));

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        testVector = new Vector3(screenX, screenY, 0);
        cameraHelper.camera.unproject(testVector);

        return false;
    }

    public float computeCopletePercentage() {
        return ((float)_totalCollected / (float)_totalCollectables) * 100;
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
