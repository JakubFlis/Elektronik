package com.jakubflis.elektronik;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
    public OrthographicCamera camera;
    private static final String TAG = CameraHelper.class.getName();
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

    public float getZoom() {
        return _zoom;
    }

    public void setTarget (Sprite target) {
        _target = target;
    }

    public Sprite getTarget () {
        return _target;
    }

    public boolean hasTarget () {
        return _target != null;
    }

    public boolean hasTarget (Sprite target) {
        return hasTarget() && _target.equals(target);
    }

    public void applyTo (OrthographicCamera camera) {
        this.camera = camera;

        camera.position.x = _position.x;
        camera.position.y = _position.y;
        camera.zoom = _zoom;
        camera.update();
    }
}
