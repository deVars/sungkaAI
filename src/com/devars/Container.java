/**
 *  Sungka Container interface file
 *  base interface for either a Field or a store
 *  @author Roseller M. Velicaria, Jr.
 *  10-12-2011
 *  email: rvelicaria@ucmerced.edu
 */
package com.devars;
interface Container { 
  public Container getNext();
  public Container getOpposite();
  public void setOpposite(Container c);
  public Player getOwner();
  public int getId();
  public void setId(int id);
  public void setNext(Container next);
  public int getSeeds();
  public void setSeeds(int seeds);
  public boolean isHarvestable( Hand hand );
  public boolean isCaptured();
  public void setCaptured(boolean captured);
  public void sow(Hand hand);
}
