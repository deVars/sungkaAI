/**
 * Sungka board class file
 * Contains the other elements and is the main object of the game
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

import java.util.Scanner;

public class Board {
    private Container[] _slot = new Container[16];
    private Player[] _player = new Player[2];
    private AI ai = new AI();
    private boolean _myTurn;
    private boolean _turnEnded;

    public Board() {
        for (int i = 0; i < 2; i++) //setup Players
            _player[i] = new Player(i);

        _slot[0] = new Store(_player[0]); // Human
        _slot[8] = new Store(_player[1]); // AI


        for (int i = 1; i < 16; i++) { //initialize values
            if (i != 8)                   //avoid the Store slot
                _slot[i] = new Field(i, 7, _player[(i < 8) ? 0 : 1], _slot[i - 1]);
        }

        _slot[0].setNext(_slot[15]);
        _slot[0].setId(0);
        _slot[8].setNext(_slot[7]);
        _slot[8].setId(8);

        for (int i = 1; i < 8; i++) { //set capture associations
            _slot[i].setOpposite(_slot[16 - i]);
            _slot[16 - i].setOpposite(_slot[i]);
        }
        _myTurn = false;
        _turnEnded = false;
    }

    public Board(Board b) { //copy constructor
        this();
        for (int i = 0; i < 16; i++) {
            this._slot[i].setSeeds(b.getSlot()[i].getSeeds());
            if (i != 8 && i != 0) //avoid capturing stores
                this._slot[i].setCaptured(b.getSlot()[i].isCaptured());
        }
        this._myTurn = b.isMyTurn();
        this._turnEnded = b.isTurnEnded();
    }

    public void drawBoard() {

        System.out.printf("+-------------------------------------------------------------+\n");
        System.out.printf("|         |     |     |     |     |     |     |     |         |\n");
        System.out.printf("|  CPU's  | %2d  | %2d  | %2d  | %2d  | %2d  | %2d  | %2d  | Player's|\n",
                _slot[9].getSeeds(), _slot[10].getSeeds(), _slot[11].getSeeds(),
                _slot[12].getSeeds(), _slot[13].getSeeds(), _slot[14].getSeeds(),
                _slot[15].getSeeds());
        System.out.printf("|         |  %s  |  %s  |  %s  |  %s  |  %s  |  %s  |  %s  |         |\n",
                (_slot[9].isCaptured()) ? "X" : " ", (_slot[10].isCaptured()) ? "X" : " ",
                (_slot[11].isCaptured()) ? "X" : " ", (_slot[12].isCaptured()) ? "X" : " ",
                (_slot[13].isCaptured()) ? "X" : " ", (_slot[14].isCaptured()) ? "X" : " ",
                (_slot[15].isCaptured()) ? "X" : " ");
        System.out.printf("|    %2d   |-----------------------------------------|   %2d    |\n",
                _slot[8].getSeeds(), _slot[0].getSeeds());
        System.out.printf("|         |  %s  |  %s  |  %s  |  %s  |  %s  |  %s  |  %s  |         |\n",
                (_slot[7].isCaptured()) ? "X" : " ", (_slot[6].isCaptured()) ? "X" : " ",
                (_slot[5].isCaptured()) ? "X" : " ", (_slot[4].isCaptured()) ? "X" : " ",
                (_slot[3].isCaptured()) ? "X" : " ", (_slot[2].isCaptured()) ? "X" : " ",
                (_slot[1].isCaptured()) ? "X" : " ");
        System.out.printf("|  Store  | %2d  | %2d  | %2d  | %2d  | %2d  | %2d  | %2d  |  Store  |\n",
                _slot[7].getSeeds(), _slot[6].getSeeds(), _slot[5].getSeeds(),
                _slot[4].getSeeds(), _slot[3].getSeeds(), _slot[2].getSeeds(),
                _slot[1].getSeeds());
        System.out.printf("|         |     |     |     |     |     |     |     |         |\n");
        System.out.printf("--------------------------------------------------------------+ \n");
        System.out.printf("|         |  7  |  6  |  5  |  4  |  3  |  2  |  1  |         |\n\n");

    }

    public int askUser() {
        int choice = 0;
        Scanner in = new Scanner(System.in);
        do {

            System.out.println("Which field do you want to harvest? (press 's' to be a wuss and accept hints)");
            String choicestr = in.next();
            if (!"s".equals(choicestr)) {
                try {
                    choice = Integer.parseInt(choicestr);
                } catch (Exception e) {
                    choice = -1;
                }
                if ((choice > 7 || choice < 1) || _slot[choice].getSeeds() == 0) {
                    System.out.println("Invalid choice.  Please try again.");
                    drawBoard();
                }
            } else {
                System.err.println("Computer suggests slot " + askPC() + ". . . ");
            }

        } while ((choice > 7 || choice < 1) || _slot[choice].getSeeds() == 0);
        return choice;
    }

    public int askPC() {
        int playerID = (isMyTurn()) ? 1 : 0;

        System.out.println("Computer is thinking . . .");
        int choice = ai.getMinimaxChoice(this, _player[playerID]);

        if (isMyTurn())
            System.out.println("Computer plays slot " + choice + " . . .");

        return choice;
    }

    public float compareUtility(Board b, Hand h) {
    /*float utility = ( this.getFieldSeeds(getOpponent(h.getOwner())) - 
        b.getFieldSeeds(getOpponent(h.getOwner())) ) * 0.5f;*/

        float utility = b.getPlayer()[h.getOwner().getId()].getScore() -
                this.getPlayer()[h.getOwner().getId()].getScore();

        return utility;
    }

    public Board evaluate(Hand h) {
        Board b = new Board(this);
        boolean targetTurn = h.getOwner().getId() == 1;
        Hand h2 = new Hand(b.getSlot()[h.getCurrentPos().getId()],
                b.getPlayer()[h.getOwner().getId()]);
        h2.harvest(h2.getCurrentPos());
        if (b.isTurnEnded()) b.setTurnEnded(false);
        while (!b.isTurnEnded()) {
            if (h2 != null) {
                if (h2.getSeeds() > 0 || !b.isTurnEnded()) {
                    b.setTurnEnded(h2.update());
                } else {
                    b.setTurnEnded(true);
                    b.setMyTurn(b.figureNextTurn(h2));
                    h2 = null;
                }
            } else {
                throw new Error("Hand to evaluate is null\n PositionID: "
                        + h.getCurrentPos().getId());
            }
        }
        return b;
    }

    public int getCaptureThreat(Player q) {
        int threat = 0;
        int playerID = q.getId();

        for (int i = 1 + 8 * playerID; i < 8 + 8 * playerID; i++)
            if (_slot[i].getSeeds() == 0 && !_slot[i].getOpposite().isCaptured())
                threat += _slot[i].getOpposite().getSeeds();

        return threat;
    }

    public int getFieldSeeds(Player p) {
        int seeds = 0;
        int playerID = p.getId();

        for (int i = 1 + 8 * playerID; i < 8 + 8 * playerID; i++)
            seeds += _slot[i].getSeeds();

        return seeds;
    }

    public boolean figureNextTurn(Hand hand) {
        if (isMyTurn()) {
            if (hand.getSeeds() == 0 && isTurnEnded()) {
                if (getFieldSeeds(getPlayer()[Player.PC]) == 0) return false;
                if (getFieldSeeds(getPlayer()[Player.HUMAN]) == 0) return true;
                if (hand.getOwner() == getPlayer()[Player.PC] &&
                        hand.getCurrentPos() == getSlot()[8]) return true;
            }
        } else {
            if (hand.getSeeds() == 0 && isTurnEnded()) {
                if (getFieldSeeds(getPlayer()[Player.HUMAN]) == 0) return true;
                if (getFieldSeeds(getPlayer()[Player.PC]) == 0) return false;
                if (hand.getOwner() == getPlayer()[Player.HUMAN] &&
                        hand.getCurrentPos() == getSlot()[0]) return false;
            }
        }
        return !isMyTurn();
    }

    public int getTotalSeeds() {
        int seeds = 0;
        for (int i = 0; i < 16; i++)
            seeds += _slot[i].getSeeds();
        return seeds;
    }

    public Player getOpponent(Player p) {
        if (p.getId() == 0)
            return _player[1];
        return _player[0];
    }

    public boolean isMyTurn() {
        return _myTurn;
    }

    public void setMyTurn(boolean _myTurn) {
        this._myTurn = _myTurn;
        //this._turnEnded = false;
    }

    public boolean isTurnEnded() {
        return _turnEnded;
    }

    public void setTurnEnded(boolean _turnEnded) {
        this._turnEnded = _turnEnded;
    }

    public Player[] getPlayer() {
        return _player;
    }

    public void setPlayer(Player[] player) {
        this._player = player;
    }

    public Container[] getSlot() {
        return _slot;
    }

    public void setSlot(Container[] slot) {
        this._slot = slot;
    }

    public boolean isOver() {
        if (!isTurnEnded()) return false;
        for (int i = 1; i < 16; i++) {
            if (i == 8) continue;
            if (this.getSlot()[i].getSeeds() > 0) return false;
        }
        return true;
    }

}
