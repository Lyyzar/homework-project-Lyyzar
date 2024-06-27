package rokafogo.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameModelTest {

    BoardGameModel model;

    @BeforeEach
    void init(){
        model = new BoardGameModel();
    }

    @Test
    void goodInitialize(){
        assertEquals(Pawn.ONE,model.board[0][2]);
        assertEquals(Pawn.MULTIPLE,model.board[7][3]);
        assertEquals(Pawn.MULTIPLE,model.board[7][5]);
        assertEquals(Pawn.MULTIPLE,model.board[7][7]);
    }
    @Test
    void getPositionsTest(){
        var map = model.getPositions();
        assertEquals(Pawn.ONE,model.board[map.get("ONE").getX()][map.get("ONE").getY()]);
        assertEquals(Pawn.MULTIPLE,model.board[map.get("MULTIPLE1").getX()][map.get("MULTIPLE1").getY()]);
        assertEquals(Pawn.MULTIPLE,model.board[map.get("MULTIPLE2").getX()][map.get("MULTIPLE2").getY()]);
        assertEquals(Pawn.MULTIPLE,model.board[map.get("MULTIPLE3").getX()][map.get("MULTIPLE3").getY()]);
        assertEquals(Pawn.MULTIPLE,model.board[map.get("MULTIPLE4").getX()][map.get("MULTIPLE4").getY()]);
    }

    @Test
    void getPawnTest(){
        assertEquals(Pawn.ONE,model.getPawn(new Coordinate(0,2)));
        assertEquals(Pawn.NONE,model.getPawn(new Coordinate(7,2)));
        assertNotEquals(Pawn.MULTIPLE,model.getPawn(new Coordinate(3,6)));
    }

    //setPawn
    @Test
    void canMoveTest(){
        assertTrue(model.canMove(new Coordinate(0,2),new Coordinate(1,3)));
        assertFalse(model.canMove(new Coordinate(0,2),new Coordinate(1,2)));
        assertFalse(model.canMove(new Coordinate(7,5),new Coordinate(6,4)));
        assertFalse(model.canMove(new Coordinate(0,2),new Coordinate(2,4)));
    }

    @Test
    void isTurnTest(){
        assertTrue(model.isTurn(new Coordinate(0,2)));
        assertFalse(model.isTurn(new Coordinate(7,7)));
        model.turn = false;
        assertFalse(model.isTurn(new Coordinate(5,7)));
        assertFalse(model.isTurn(new Coordinate(0,2)));
        model.turn = true;
    }

    @Test
    void isPawnMoveTest(){
        assertTrue(model.isPawnMove(new Coordinate(0,2),new Coordinate(1,3)));
        assertTrue(model.isPawnMove(new Coordinate(0,2),new Coordinate(1,1)));
        model.turn = false;
        assertTrue(model.isPawnMove(new Coordinate(7,7),new Coordinate(6,6)));
        assertFalse(model.isPawnMove(new Coordinate(0,2),new Coordinate(2,4)));
        model.turn = true;
    }

    @Test
    void isEmptyTest(){
        assertTrue(model.isEmpty(new Coordinate(1,3)));
        assertFalse(model.isEmpty(new Coordinate(0,2)));
    }

    @Test
    void isOnBoardTest(){
        assertTrue(model.isOnBoard(new Coordinate(1,3)));
        assertTrue(model.isOnBoard(new Coordinate(4,7)));
        assertFalse(model.isOnBoard(new Coordinate(10,6)));
        assertFalse(model.isOnBoard(new Coordinate(6,8)));
    }

    @Test
    void moveTest(){
        assertEquals(Pawn.ONE,model.board[0][2]);
        model.move(new Coordinate(0,2),new Coordinate(1,3));
        assertEquals(Pawn.ONE,model.board[1][3]);
        assertEquals(Pawn.MULTIPLE,model.board[7][5]);
        model.move(new Coordinate(7,5),new Coordinate(6,4));
        assertEquals(Pawn.MULTIPLE,model.board[6][4]);
    }

    @Test
    void isBlackWonTest(){
        model.setPawn(new Coordinate(2,6),Pawn.MULTIPLE);
        model.setPawn(new Coordinate(4,6),Pawn.MULTIPLE);
        model.setPawn(new Coordinate(3,7),Pawn.ONE);
        assertTrue(model.isBlackWon(3,7));

        model.setPawn(new Coordinate(1,7),Pawn.ONE);
        assertFalse(model.isBlackWon(1,7));
    }

    @Test
    void isEmptyOnBoardTest(){
        assertTrue(model.isEmptyOnBoard(new Coordinate(0,2)));
        assertTrue(model.isEmptyOnBoard(new Coordinate(5,10)));
    }

    @Test
    void isWhiteWonTest(){
        Map<String,Coordinate> map1 = new HashMap<>();
        map1.put("ONE",new Coordinate(7,3));
        map1.put("MULTIPLE1",new Coordinate(3,3));
        map1.put("MULTIPLE2",new Coordinate(6,0));
        map1.put("MULTIPLE3",new Coordinate(5,5));
        map1.put("MULTIPLE1",new Coordinate(7,7));
        assertTrue(model.isWhiteWon(map1));

        Map<String,Coordinate> map2 = new HashMap<>();
        map2.put("ONE",new Coordinate(4,3));
        map2.put("MULTIPLE1",new Coordinate(6,3));
        map2.put("MULTIPLE2",new Coordinate(3,5));
        map2.put("MULTIPLE3",new Coordinate(2,7));
        map2.put("MULTIPLE4",new Coordinate(7,5));
        assertFalse(model.isWhiteWon(map2));
    }

    @Test
    void isGoalTest(){
        assertFalse(model.isGoal());
        model.setPawn(new Coordinate(0,2),Pawn.NONE);
        model.setPawn(new Coordinate(7,3),Pawn.ONE);
        assertTrue(model.isGoal());
        init();
        model.setPawn(new Coordinate(2,6),Pawn.MULTIPLE);
        model.setPawn(new Coordinate(4,6),Pawn.MULTIPLE);
        model.setPawn(new Coordinate(3,7),Pawn.ONE);
        assertTrue(model.isGoal());
    }


}
