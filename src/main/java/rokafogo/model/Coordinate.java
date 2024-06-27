package rokafogo.model;

/**
 * Egy 2 pontos koordinátát ábrázol.
 */
public class Coordinate {

    private int x;
    private int y;

    /**
     * A koordináta osztály konstruktora.
     *
     * @param x A koordinátánk x-e.
     * @param y A koordinátánk y-ja.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Vissza adja a koordinátánk x-ét.
     */
    public int getX() {
        return x;
    }

    /**
     * @return Vissza adja a koordinátánk y-ját.
     */
    public int getY() {
        return y;
    }
}
