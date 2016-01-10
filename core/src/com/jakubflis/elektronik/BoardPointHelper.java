package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class BoardPointHelper {

    public static String getBoartPointAssetName(int boardCode) {
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
            case 19:
                return "full_overflowed.png";
            default:
                return null;
        }
    }

    public static boolean getBoartPointOverflowedStatus(int boardCode) {
        switch (boardCode) {
            case 8:
            case 9:
            case 15:
            case 16:
            case 19:
                return true;
            default:
                return false;
        }
    }

    public static Vector2 getFirstBoardPointCoords(int[][] boardTable) {
        float height = boardTable.length * Constants.SQUARE_SIZE;
        float width = boardTable[0].length * Constants.SQUARE_SIZE;

        return new Vector2(-1 * (width / 2.0f), height / 2.0f);
    }
}
