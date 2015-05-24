/**
 * Sungka Container interface file
 * base interface for either a Field or a store
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

interface Container {
    Container getNext();

    Container getOpposite();

    void setOpposite(Container c);

    Player getOwner();

    int getId();

    void setId(int id);

    void setNext(Container next);

    int getSeeds();

    void setSeeds(int seeds);

    boolean isHarvestable(Hand hand);

    boolean isCaptured();

    void setCaptured(boolean captured);

    void sow(Hand hand);
}
