package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class WorldController extends InputAdapter {
    public CameraHelper cameraHelper;
    public BoardPointSprite[] testSprites;
    public int selectedSprite;
    private final int SPRITE_WIDTH = 32;
    private final int SPRITE_HEIGHT = 32;

    //the board is upside-down
    //0 - non-collectable BoardPoint
    //1 - collectable level 1 BoardPoint
    //2 - collectable level 2 BoardPoint
    //3 - starting point
    private int[][] _testBoard = new int[][]{
            { 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 2, 1, 2, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 2, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 2, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 2, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 1, 0, 0 }
    };

    private static final String TAG = WorldController.class.getName();

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
        float squareSize = 0.3f;

        int numberOfArrayRows = _testBoard.length;
        int numberOfArrayCols = _testBoard[0].length;

        testSprites = new BoardPointSprite[numberOfArrayCols * numberOfArrayRows];

        float tempPosition = -3.5f;
        float tempYPosition = -2.0f;
        int counter = 0;

        for (int[] a_testBoard : _testBoard) {

            for (int i = 0; i < numberOfArrayCols; i++) {
                BoardPointSprite sprite;

                switch (a_testBoard[i]) {
                    case 0:
                        sprite = new BoardPointSprite(createBoardPointTexture(EnumManager.PathPoint.BLANK_POINT));
                        sprite.isCollectable = false;
                        break;
                    case 1:
                        sprite = new BoardPointSprite(createBoardPointTexture(EnumManager.PathPoint.LEVEL_1_POINT));
                        sprite.isCollectable = true;
                        break;
                    case 2:
                        sprite = new BoardPointSprite(createBoardPointTexture(EnumManager.PathPoint.LEVEL_2_POINT));
                        sprite.isCollectable = true;
                        break;
                    case 3:
                        sprite = new BoardPointSprite(createBoardPointTexture(EnumManager.PathPoint.STARTING_POINT));
                        sprite.isCollectable = false;
                        break;
                    default:
                        sprite = new BoardPointSprite(createBoardPointTexture(EnumManager.PathPoint.BLANK_POINT));
                        sprite.isCollectable = false;
                        break;
                }
                sprite.isCollected = false;
                sprite.setSize(squareSize, squareSize);
                sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
                sprite.setPosition(tempPosition, tempYPosition);

                testSprites[counter++] = sprite;

                tempPosition += squareSize;
            }

            tempYPosition += squareSize;
            tempPosition = -3.5f;
        }

        selectedSprite = 0;
    }

    private Texture createBoardPointTexture(EnumManager.PathPoint pathPointKind) {
        Pixmap pixmap = new Pixmap(SPRITE_WIDTH, SPRITE_HEIGHT, Format.RGBA8888);

        switch (pathPointKind) {
            case BLANK_POINT:
                pixmap.setColor(38.0f / 255.0f, 102.0f / 255.0f, 41.0f / 255.0f, 1);
                break;
            case LEVEL_1_POINT:
                pixmap.setColor(75.0f / 255.0f, 201.0f / 255.0f, 81.0f / 255.0f, 1);
                break;
            case LEVEL_2_POINT:
                pixmap.setColor(217.0f / 255.0f, 187.0f / 255.0f, 80.0f / 255.0f, 1);
                break;
            case STARTING_POINT:
                pixmap.setColor(0, 0, 0, 1);
                break;
            case CONNECTION_POINT:
                break;
        }

        pixmap.fill();
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.drawRectangle(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);

        return new Texture(pixmap);
    }

    private void handleInputDigit(float deltaTime) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            return;
        }

        float spriteMoveSpeed = 5 * deltaTime;

        if (Gdx.input.isKeyPressed(Keys.A)) {
            moveSelectedSprite(-1 * spriteMoveSpeed, 0);
        }

        if (Gdx.input.isKeyPressed(Keys.W)) {
            moveSelectedSprite(0, spriteMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            moveSelectedSprite(0, -1 * spriteMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            moveSelectedSprite(spriteMoveSpeed, 0);
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

    private void moveSelectedSprite(float x, float y) {
        Gdx.app.debug(TAG, "2. Coordinates: " + x + " " + y);
        testSprites[selectedSprite].translate(x, y);
    }

    private void moveCamera (float x, float y) {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game wolrd resetted.");
        } else if (keycode == Keys.SPACE) {
            selectedSprite = (selectedSprite + 1) % testSprites.length;

            if (cameraHelper.hasTarget()) {
                cameraHelper.setTarget(testSprites[selectedSprite]);
            }
            Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
        } else if (keycode == Keys.ENTER) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(screenX - MathUtils.floor(SPRITE_WIDTH / 2), screenY + MathUtils.floor(SPRITE_HEIGHT / 2), 0);
        cameraHelper.camera.unproject(worldCoordinates);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 draggedTouch = new Vector3(screenX, screenY, 0);

        cameraHelper.camera.unproject(draggedTouch);

        for (BoardPointSprite sprite : testSprites) {
            if (sprite.getBoundingRectangle().contains(draggedTouch.x, draggedTouch.y)) {

                if (sprite.isCollectable) {
                    sprite.setAlpha(0);
                }
            }
        }

        return false;
    }
}
