package rokafogo.model;

import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A játlk modellje található az osztályban.
 */
public class BoardGameModel {

    /**
     * A BOARD_SIZE a játékunk táblájának méretét határozza meg.
     */
    public static final int BOARD_SIZE = 8;
    /**
     * A játékunk táblája egy két dimenziós tömb benne Pawn elemekkel.
     */

    public Pawn[][] board = new Pawn[BOARD_SIZE][BOARD_SIZE];
    /**
     * A körök kezelésére használt változó, ha true a fehér jön, ha false a fekete.
     */

    public boolean turn = true;

    /**
     * A BoardGameModel() feltölti a játékunk tábláját.
     * Először minden mezőhöz a Pawn.NONE-t rendeli hozzá.
     * Utánna pedig koordináták alapján megadjuk a bábujainkat.
     */
    public BoardGameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Pawn.NONE;
            }
        }
        board[0][2] = Pawn.ONE;
        board[7][1] = Pawn.MULTIPLE;
        board[7][3] = Pawn.MULTIPLE;
        board[7][5] = Pawn.MULTIPLE;
        board[7][7] = Pawn.MULTIPLE;
    }

    /**
     * Létrehoz egy map objektumot, ami bejárja a játékunk tábláját és megkeresi a bábujainkat.
     *
     * @return Vissza adja a bábujaink koordinátáját egy map-ban.
     */
    public Map<String, Coordinate> getPositions() {
        Map<String, Coordinate> map = new HashMap<>();
        var n = 0;
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Pawn.ONE) {
                    map.put("ONE", new Coordinate(i, j));
                } else if (board[i][j] == Pawn.MULTIPLE) {
                    n++;
                    map.put("MULTIPLE" + n, new Coordinate(i, j));
                }
            }
        }
        return map;
    }

    /**
     * Megnézi, hogy az adott koordinátán milyen bábu található.
     *
     * @param c A koordináta, amit vizsgálunk.
     * @return Vissza ad a Pawn enumból egyet (ONE,MULTIPLE,NONE)
     */
    public Pawn getPawn(Coordinate c) {
        return board[c.getX()][c.getY()];
    }

    /**
     * Egy koordinátán beállítja a paraméterül adott Pawn enum egyikét(ONE,MULTIPLE,NONE).
     *
     * @param c A koordináta, amin beállítjuk a Pawn paramétert.
     * @param p A Pawn paraméter, amit beállítunk a koordináta paraméteren.
     */
    public void setPawn(Coordinate c, Pawn p) {
        board[c.getX()][c.getY()] = p;
    }

    /**
     * Megnézi, hogy tudunk-e egyik helyről a másikba lépni.
     *
     * @param from Egy koordináta, ahonnan lépni szeretnénk
     * @param to   Egy koordináta, ahová lépni szeretnénk
     * @return Egy boolean-t ad vissza, ha true akkor tud lépne, ha false akkor nem.
     */
    public boolean canMove(Coordinate from, Coordinate to) {
        return !isEmpty(from) && isEmpty(to) && isOnBoard(from) && isOnBoard(to) && isPawnMove(from, to) && isTurn(from);
    }

    /**
     * Megnézi, hogy ahonnan lépni akarunk annak a bábunak van-e a köre.
     *
     * @param from A koordináta, ahonnan lépni akarunk.
     * @return boolean-t ad vissza, ha true akkor annak a bábunak a köre van, ha false akkor nem.
     */
    public boolean isTurn(Coordinate from) {
        return (getPawn(from) == Pawn.ONE && turn) || (getPawn(from) == Pawn.MULTIPLE && !turn);
    }

    /**
     * Megnézi,hogy a lépésünkkel csak egy mezőt lépünk-e.
     *
     * @param from A koordináta, ahonnan lépni szeretnénk.
     * @param to   A koordináta, ahová lépni szeretnénk.
     * @return boolean-t ad vissza, hogy egy mezőt léptünk-e.
     */
    public boolean isPawnMove(Coordinate from, Coordinate to) {
        var dx = to.getX() - from.getX();
        var dy = to.getY() - from.getY();
        switch (getPawn(from)) {
            case ONE -> {
                return Math.abs(dx) * Math.abs(dy) == 1;
            }
            case MULTIPLE -> {
                return (dx == -1 && dy == -1) || (dx == -1 && dy == 1);
            }
        }
        return false;
    }


    /**
     * Megnézi, hogy egy adott koordinátán nincs-e bábunk azaz a Pawn.NONE van-e azon a helyen.
     *
     * @param c A koordináta, amit vizsgálunk.
     * @return boolean-t ad vissza, ha true akkor üres a mező(Pawn.NONE), ha false akkor ott egy bábu(Pawn.MULTIPLE vagy Pawn.ONE)
     */
    public boolean isEmpty(Coordinate c) {
        return isOnBoard(c) && getPawn(c) == Pawn.NONE;
    }

    /**
     * Azt vizsgáljuk, hogy az adott koordináta a játékunk tábláján van-e.
     *
     * @param c Az adott koordináta.
     * @return boolean-t ad vissza, ha true akkor a koordináta rajta van a táblán, ha false akkor nincs rajta.
     */
    public boolean isOnBoard(Coordinate c) {
        return c.getX() < BOARD_SIZE && c.getY() < BOARD_SIZE && 0 <= c.getX() && 0 <= c.getY();
    }

    /**
     * Az egyik koordinátáról a másikra lépünk.
     *
     * @param from Az egyik koordináta.
     * @param to   A másik koordináta.
     */
    public void move(Coordinate from, Coordinate to) {
        setPawn(to, getPawn(from));
        setPawn(from, Pawn.NONE);
        turn = !turn;
        Logger.info("Moved ({},{}) to ({},{})", from.getX(), from.getY(), to.getX(), to.getY());
    }

    /**
     * @param x A vizsgálandó koordináta x-e.
     * @param y A vizsgálandó koordináta y-ja.
     * @return true-t ad vissza, ha a fehér bábu lehetséges lépései közül egyikre sem lehet lépni, egyébként false.
     */
    boolean isBlackWon(int x, int y) {
        var cord1 = new Coordinate(x - 1, y - 1);
        var cord2 = new Coordinate(x - 1, y + 1);
        var cord3 = new Coordinate(x + 1, y - 1);
        var cord4 = new Coordinate(x + 1, y + 1);
        return (isEmptyOnBoard(cord1) && isEmptyOnBoard(cord2) && isEmptyOnBoard(cord3) && isEmptyOnBoard(cord4));
    }

    /**
     * Megvizsgálja, hogy az adott koordináta rajta van-e a táblán vagy üres-e.
     *
     * @param cord Az adott koordináta.
     * @return boolean-t ad vissza, ha true akkor vagy nem üres a koordináta vagy nincs a táblán, egyébként false.
     */
    boolean isEmptyOnBoard(Coordinate cord) {
        return (!isEmpty(cord) || !isOnBoard(cord));
    }

    /**
     * Megnézi, hogy a leghátsó fekete bábu sora egyenlő-e a fehér bábunkéval.
     *
     * @param map A megadott map amiben a bábuk koordinátáit tároljuk.
     * @return true-t ad vissza, ha egyenlő egyébként false.
     */
    public boolean isWhiteWon(Map<String, Coordinate> map) {
        var dx = map.get("ONE").getX();
        Coordinate lowest = new Coordinate(0, 0);
        for (var i = 1; i < map.size(); i++) {
            if (map.get("MULTIPLE" + i).getX() > lowest.getX()) lowest = map.get("MULTIPLE" + i);
        }
        return dx == lowest.getX();
    }


    /**
     * Megnézi, hogy vége van-e a játéknak.
     *
     * @return boolean-t ad vissza, ha true akkor vége van, ha false akkor nincs.
     */
    public boolean isGoal() {
        var map = getPositions();
        var dx = map.get("ONE").getX();
        var dy = map.get("ONE").getY();
        if (isBlackWon(dx, dy)) {
            Logger.info("A fekete bábuk nyertek!");

            return true;
        }
        if (isWhiteWon(map)) {
            Logger.info("A fehér bábuk nyertek!");
            return true;
        }
        return false;
    }

    /**
     * Kiiratja a játékunk tábláját.
     *
     * @return Vissza adja Stringként a táblát.
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
