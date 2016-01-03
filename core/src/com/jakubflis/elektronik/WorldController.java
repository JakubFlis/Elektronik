package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class WorldController extends InputAdapter {
    public CameraHelper cameraHelper;
    public BoardPointSprite[] testSprites;
    public int selectedSprite;
    private final int SPRITE_WIDTH = 32;
    private final int SPRITE_HEIGHT = 32;
    private static final String TAG = WorldController.class.getName();
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
            { 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 10, 10, 0, 0, 0, 14, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 14, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 7, 1, 1, 2, 10, 10, 3, 1, 1, 11, 1, 2, 10, 3, 1, 1, 1, 1, 1, 1, 5, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public WorldController() {
        init();
    }

    public void init() {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        initTestObjects();
    }

    public void update (float deltaTime) {
        handleInputDigit(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void initTestObjects() {
        float squareSize = 0.25f;

        int numberOfArrayRows = _gameBoard.length;
        int numberOfArrayCols = _gameBoard[0].length;

        testSprites = new BoardPointSprite[numberOfArrayCols * numberOfArrayRows];

        float tempPosition = -3.3f;
        float tempYPosition = 2.0f;
        int counter = 0;

        for (int[] a_testBoard : _gameBoard) {

            for (int i = 0; i < numberOfArrayCols; i++) {
                BoardPointSprite sprite;
                String pointTextureAsset = getBoartPointAssetName(a_testBoard[i]);

                if (pointTextureAsset != null) {
                    sprite = new BoardPointSprite(new Texture(pointTextureAsset));
                    sprite.isCollectable = true;
                } else {
                    sprite = new BoardPointSprite(createBlankBoardPointTexture());
                    sprite.isCollectable = false;
                }


                sprite.isCollected = false;
                sprite.setSize(squareSize, squareSize);
                sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
                sprite.setPosition(tempPosition, tempYPosition);

                testSprites[counter++] = sprite;
                tempPosition += squareSize;
            }

            tempYPosition -= squareSize;
            tempPosition = -3.3f;
        }

        selectedSprite = 0;
    }

    private Texture createBlankBoardPointTexture() {
        Pixmap pixmap = new Pixmap(SPRITE_WIDTH, SPRITE_HEIGHT, Format.RGBA8888);
        pixmap.setColor(38.0f / 255.0f, 102.0f / 255.0f, 41.0f / 255.0f, 0);
        pixmap.fill();

        return new Texture(pixmap);
    }

    private String getBoartPointAssetName(int boardCode) {
        switch (boardCode) {
            case 1:
                return "normal.png";
            case 2:
                return "crescendo.png";
            case 3:
                return "decrescendo.png";
            case 4:
                return "corner_1.png";
            case 5:
                return "corner_2.png";
            case 6:
                return "corner_3.png";
            case 7:
                return "corner_4.png";
            case 8:
                return "lowerhalf.png";
            case 9:
                return "upperhalf.png";
            case 10:
                return "full.png";
            case 11:
                return "connector_1.png";
            case 12:
                return "full_circle.png";
            case 13:
                return "empty_circle.png";
            case 14:
                return "normal_v.png";
            case 15:
                return "righthalf.png";
            case 16:
                return "lefthalf.png";
            case 17:
                return "crescendo_v.png";
            case 18:
                return "decrescendo_v.png";
            default:
                return null;
        }
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
        Vector3 draggedTouch = new Vector3(screenX, screenY, 0);

        cameraHelper.camera.unproject(draggedTouch);

        for (BoardPointSprite sprite : testSprites) {
            if (sprite.getBoundingRectangle().contains(draggedTouch.x, draggedTouch.y)) {

                if (sprite.isCollectable) {
                    sprite.setColor(Color.CHARTREUSE);
                    sprite.isCollected = true;
                }
            }
        }

        return false;
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
    }
}
