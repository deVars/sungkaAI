/**
 * Sungka AI main file
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

import java.io.IOException;

public class SungkaAI {


    public static void main(String[] args) throws IOException {
        Board game = new Board();
        Hand hand = null;

        game.drawBoard();
        while (!game.isOver()) {
            if (!game.isMyTurn() && hand == null) {
                hand = new Hand(game.getSlot()[game.askUser()],
                        game.getPlayer()[Player.HUMAN]);
                hand.harvest(hand.getCurrentPos());
                game.setTurnEnded(false);
            } else if (hand == null) { //PC turn
                hand = new Hand(game.getSlot()[game.askPC()],
                        game.getPlayer()[Player.PC]);
                hand.harvest(hand.getCurrentPos());
                game.setTurnEnded(false);
                //break;
            }
            // if (hand != null) {
                if (hand.getSeeds() > 0 || !game.isTurnEnded()) {
                    game.setTurnEnded(hand.update());
                } else {
                    game.setTurnEnded(true);
                }
                if (game.isTurnEnded()) {
                    game.setMyTurn(game.figureNextTurn(hand));
                    hand = null;
                }
            // }
            game.drawBoard();
        }
        if (game.getPlayer()[Player.HUMAN].getScore() > game.getPlayer()[Player.PC].getScore()) {
            System.out.println("YOU WIN!");
        } else if (game.getPlayer()[Player.HUMAN].getScore() < game.getPlayer()[Player.HUMAN].getScore()) {
            System.out.println("YOU LOSE!");
        } else {
            System.out.println("DRAW GAME");
        }
        System.out.println("Thanks for playing!");
    }

}
