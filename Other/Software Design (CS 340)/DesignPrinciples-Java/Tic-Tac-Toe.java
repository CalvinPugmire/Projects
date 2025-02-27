//1. What design principles does this code violate?
   //Information hiding.
//2. Refactor the code to improve its design.

Game.java

public class Game {
   private StringBuffer board;

   public Game(String s) {board = new StringBuffer(s);}

   private Game(StringBuffer s, int position, char player) {
      board = new StringBuffer();
      board.append(s);
      board.setCharAt(position, player);
   }

   public int move(char player) {
      for (int i = 0; i < 9; i++) {
         if (board.charAt(i) == '-') {
            Game game = play(i, player);
            if (game.winner() == player)
               return i;
         }
      }

      for (int i = 0; i < 9; i++) {
         if (board.charAt(i) == '-')
            return i;
      }
      return -1;
   }

   private Game play(int i, char player) {
      return new Game(this.board, i, player);
   }

   private char winner() {
      if (board.charAt(0) != '-'
            && board.charAt(0) == board.charAt(1)
            && board.charAt(1) == board.charAt(2))
         return board.charAt(0);
      if (board.charAt(3) != '-'
            && board.charAt(3) == board.charAt(4)
            && board.charAt(4) == board.charAt(5))
         return board.charAt(3);
      if (board.charAt(6) != '-'
            && board.charAt(6) == board.charAt(7)
            && board.charAt(7) == board.charAt(8))
         return board.charAt(6);
      return '-';
   }
}

GameTest.java

import junit.framework.*;

public class GameTest extends TestCase {

   public GameTest(String s) {super(s);}

   public void testDefaultMove() {
      Game game = new Game("XOXOX-OXO");
      assertEquals(5, game.move('X'));

      game = new Game("XOXOXOOX-");
      assertEquals(8, game.move('O'));

      game = new Game("---------");
      assertEquals(0, game.move('X'));

      game = new Game("XXXXXXXXX");
      assertEquals(-1, game.move('X'));
   }

   public void testFindWinningMove() {
      Game game = new Game("XO-XX-OOX");
      assertEquals(5, game.move('X'));
   }
   public void testWinConditions() {
      Game game = new Game("---XXX---");
      assertEquals('X', game.winner());
   }
}

