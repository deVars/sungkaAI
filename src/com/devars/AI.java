/**
 * Sungka AI AI class file
 * the thinking part of the game
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

public class AI {

    private int maxplies;
    private int pliesbeforeprune;

    public AI() {
        this.maxplies = 4;  // can go to 32, but I won't recommend it
        this.pliesbeforeprune = 2; // sucky at 1, 2 is decent
    }

  /*
   * This getMinimaxMaxPayoff function gets the maximum of each possibilities
   * spawned by possible moves on the board.  Implements pruning at plyCount >
   * pliesbeforeprune
   */

    private float getMinimaxMaxPayoff(Board b, Player p, int plyCount) {
        int playerID = p.getId();
        float minimax = -65535;

        for (int i = 1 + 8 * playerID; i < 8 + 8 * playerID; i++) {
            if (b.getSlot()[i].isCaptured() || b.getSlot()[i].getSeeds() == 0)
                continue;
            Board b2 = new Board(b);
            Hand h = new Hand(b2.getSlot()[i], b2.getPlayer()[playerID]);

            b2 = b2.evaluate(h);
            float fieldSeedsDelta = (b2.getFieldSeeds(b.getOpponent(h.getOwner())) -
                    b.getFieldSeeds(b.getOpponent(h.getOwner())));
            float utility = b.compareUtility(b2, h);
            float enemyPayOff = 0;
            if (plyCount < this.maxplies)
                enemyPayOff = getMinimaxMinPayoff(b2, b2.getOpponent(p), plyCount + 1);

            if (minimax == -65535) {
                if (utility - fieldSeedsDelta == 1.0f) {
                    minimax = 1.0f + getMinimaxMaxPayoff(b2, p, plyCount);
                } else {
                    minimax = utility /*- threat + initSeeds * 0.25f*/ - enemyPayOff;
                }
            } else {
                float result = utility;
                if (utility - fieldSeedsDelta == 1.0f) {
                    result = 1.0f + getMinimaxMaxPayoff(b2, p, plyCount);
                } else {
                    result = utility /*- threat + initSeeds * 0.25f*/ - enemyPayOff;
                    if (result > minimax) minimax = result;
                    else if (result == minimax) continue;
                    else if (plyCount > this.pliesbeforeprune) return minimax;
                }
            }

            b2 = null;
            h = null;
        }
        return minimax;
    }

    /*
     * This is the launcher function and the central hub that makes the final
     * decision for all the minimax calculations it launches.
     */
    public int getMinimaxChoice(Board b, Player p) {
        int playerID = p.getId();
        int choice = 0;
        float maxresult = -65535.0f;

        for (int i = 1 + 8 * playerID; i < 8 + 8 * playerID; i++) {
            if (b.getSlot()[i].isCaptured() || b.getSlot()[i].getSeeds() == 0)
                continue;
            Board b2 = new Board(b);
            Hand h = new Hand(b2.getSlot()[i], b2.getPlayer()[playerID]);

            b2 = b2.evaluate(h);
            float fieldSeedsDelta = (b2.getFieldSeeds(b.getOpponent(h.getOwner())) -
                    b.getFieldSeeds(b.getOpponent(h.getOwner())));
            float utility = b.compareUtility(b2, h);
            float enemyPayOff = getMinimaxMaxPayoff(b2, b2.getOpponent(p), 1);

            if (maxresult == -65535.0f) {
                if (utility - fieldSeedsDelta == 1.0f) { // can double turn
                    maxresult = 1.0f + getMinimaxMinPayoff(b2, p, 1);
                } else {
                    maxresult = utility/* - threat + initSeeds * 0.25f*/;
                }
                maxresult -= enemyPayOff;
                choice = i;
            } else {
                float result = utility/* - threat + initSeeds * 0.25f*/;
                if (utility - fieldSeedsDelta == 1.0f) {// can double turn
                    result = 1.0f + getMinimaxMinPayoff(b2, p, 1);
                } else {
                    result -= enemyPayOff;
                }
                if (result > maxresult) {
                    maxresult = result;
                    choice = i;
                }
            }
            b2 = null;
            h = null;
        }

        return choice;
    }

    /*
     * In case of a double, triple... n- consecutive turn, this gets recursed.
     * Our final rank value is the difference between the lowest max utility the
     * opponent gains and the rewards we get choosing that option.
     */
    private float getMinimaxMinPayoff(Board b, Player p, int plyCount) {
        int playerID = p.getId();
        float minimax = -65535;

        for (int i = 1 + 8 * playerID; i < 8 + 8 * playerID; i++) {
            if (b.getSlot()[i].isCaptured() || b.getSlot()[i].getSeeds() == 0)
                continue;
            Board b2 = new Board(b);
            Hand h = new Hand(b2.getSlot()[i], b2.getPlayer()[playerID]);

            b2 = b2.evaluate(h);
            float fieldSeedsDelta = (b2.getFieldSeeds(b.getOpponent(h.getOwner())) -
                    b.getFieldSeeds(b.getOpponent(h.getOwner())));
            float utility = b.compareUtility(b2, h);
            float enemyPayOff = getMinimaxMaxPayoff(b2, b2.getOpponent(p), plyCount);

            if (minimax == -65535) {
                minimax = utility/* - threat + initSeeds * 0.25f*/;
                if (utility - fieldSeedsDelta == 1.0f) {
                    minimax = 1.0f + getMinimaxMinPayoff(b2, p, plyCount);
                } else {
                    minimax -= enemyPayOff;
                }
            } else {
                float result = utility/* - threat + initSeeds * 0.25f*/;

                if (utility - fieldSeedsDelta == 1.0f) {
                    result = 1.0f + this.getMinimaxMinPayoff(b2, p, plyCount);
                } else {
                    result -= enemyPayOff;
                }
                if (result > minimax) minimax = result;
                else if (result == minimax) continue;
                else if (plyCount > this.pliesbeforeprune) return minimax;
            }
            b2 = null;
            h = null;
        }
        return minimax;
    }


}
