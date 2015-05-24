/**
 *  Sungka Store class file
 *  Implements a player's store and its functions
 *  @author Roseller M. Velicaria, Jr.
 *  10-12-2011
 *  email: rvelicaria@ucmerced.edu
 */
package com.devars;
public class Store implements Container {
  private int _id;
  private int _seeds;
  private Container _next;
  private Player _owner;
  
  public Store(Player owner) {
    _id = 0;
    _seeds = 0;
    _next = null;
    _owner = owner;
    _owner.setStore(this);
  }
  
  public Store (Store s) {  //copy constructor
    _id = s.getId();
    _seeds = s.getSeeds();
    _next = s.getNext();
    _owner = s.getOwner();
  }
  
  public Store(Container next, Player owner) {
    this(owner);
    _next = next;
  }
  
  @Override
  public boolean isHarvestable(Hand hand) {
    return false;
  }
  
  @Override
  public Player getOwner() {
    return _owner;
  }
  
  @Override
  public void sow(Hand hand) {
    if (this.getOwner() == hand.getOwner() && hand.getSeeds() > 0) {
      hand.setSeeds(hand.getSeeds() - 1);
      this.setSeeds(this.getSeeds() + 1);
    }
    if (hand.getSeeds() > 0)
      hand.setCurrentPos(this._next);
  }

  @Override
  public int getId() {
    return _id;
  }

  @Override
  public void setId(int id) {
    this._id = id;
  }

  @Override
  public Container getNext() {
    return this._next;
  }

  @Override
  public void setNext(Container next) {
    this._next = next;
  }

  @Override
  public int getSeeds() {
    return _seeds;
  }

  @Override
  public void setSeeds(int seeds) {
    this._seeds = seeds;
  }

  public void incSeeds(int seeds) {
    this._seeds += seeds;
  }
  
  @Override
  public Container getOpposite() {
    return null;
  }
  
  @Override
  public void setOpposite(Container c) {
    throw new Error("Stores cannot have opposites");
  }

  @Override
  public boolean isCaptured() {
    return false;
  }

  @Override
  public void setCaptured(boolean captured) {
    throw new Error("Stores cannot be captured");
  }
  
  
}
