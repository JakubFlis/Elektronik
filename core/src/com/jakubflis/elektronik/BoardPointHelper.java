package com.jakubflis.elektronik;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasa zawierająca metody potrzebne do prawidłowego
 * zdefiniowania źródła i początkowego położenia
 * kafelków planszy.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class BoardPointHelper {

    /**
     * Zwraca nazwę pliku graficznego odpowiadającego za wizerunek
     * kafelka na planszy.
     *
     * @param boardCode jest to kod kafelka zapisany w tablicy zdefiniowanej
     *                  w klasie LevelBoards
     * @return nazwa pliku graficznego
     */
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
                return "lowerhalf_f.png";
            case 9:
                return "upperhalf_f.png";
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
                return "righthalf_f.png";
            case 16:
                return "lefthalf_f.png";
            case 17:
                return "crescendo_v.png";
            case 18:
                return "decrescendo_v.png";
            case 19:
                return "full_overflowed_f.png";
            default:
                return null;
        }
    }
    /**
     * Określa, czy kafelek może zostać uznany za zaliczony przy pomocy
     * tradycyjnego ruchu, czy wymaga dodatkowej siły ruchu (w przypadku
     * eDmuchawki - mocniejszego dmuchnięcia).
     *
     * @param boardCode jest to kod kafelka zapisany w tablicy zdefiniowanej
     *                  w klasie LevelBoards
     * @return true, jeśli wymaga wzmocnionego ruchu, false, jeśli wymaga
     * tradycyjnego ruchu
     */
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

    /**
     * Oblicza współrzędne rysowania pierwszego kafelka.
     *
     * @param boardTable Jest to tablica kodów wszystkich kafelków.
     *                   Wymagany jest tylko jej wymiar.
     * @return wektor zawierający współrzędne wynikające z rozmiaru
     * tablicy pomniejszone o miejsce potrzebne dla interfejsu
     * graficznego.
     */
    public static Vector2 getFirstBoardPointCoords(int[][] boardTable) {
        float height = boardTable.length * Constants.SQUARE_SIZE;
        float width = boardTable[0].length * Constants.SQUARE_SIZE;

        return new Vector2(-1 * (width / 2.0f), (height / 2.0f) - (2 * Constants.SQUARE_SIZE));
    }
}
