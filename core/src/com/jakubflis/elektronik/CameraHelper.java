package com.jakubflis.elektronik;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Klasa stworzona na potrzeby procesu tworzenia gry, dzięki niej
 * można poruszać się po wyświetlonej planszy przy użyciu strzałek
 * na klawiaturze, dodatkowo można również przybliżać i oddalać
 * widok przy pomocy przycisków , oraz .
 * Przycisk / resetuje zoom.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class CameraHelper {
    public OrthographicCamera camera;
    public OrthographicCamera guiCamera;
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private Vector2 _position;
    private float _zoom;
    private Sprite _target;

    public CameraHelper() {
        _position = new Vector2();
        _zoom = 1.0f;
    }

    public void update(float deltaTime) {
        if (!hasTarget()) {
            return;
        }
        _position.x = _target.getX() + _target.getOriginX();
        _position.y = _target.getY() + _target.getOriginY();
    }

    public void setPosition(float x, float y) {
        _position.set(x, y);
    }

    public Vector2 getPosition() {
        return _position;
    }

    public void addZoom(float amount) {
        setZoom(_zoom + amount);
    }

    public void setZoom(float zoom) {
        _zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public void setTarget (Sprite target) {
        _target = target;
    }

    public boolean hasTarget () {
        return _target != null;
    }

    public void applyTo (OrthographicCamera camera, OrthographicCamera guiCamera) {
        this.camera = camera;
        this.guiCamera = guiCamera;

        camera.position.x = _position.x;
        camera.position.y = _position.y;
        camera.zoom = _zoom;
        camera.update();
    }
}
