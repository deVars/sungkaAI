/**
 *  Sungka Field class file
 *  Implements the field container
 *  @author Roseller M. Velicaria, Jr.
 *  10-12-2011
 *  email: rvelicaria@ucmerced.edu
 */
package com.devars;
public class Field implements Container {
  private boolean _captured;
  private int _id;
  private int _seeds;
  private Container _next;
  private Container _opposite;
  private Player _owner;

  public Field (int id, int seeds, Player owner, Container next) {
    this._id = id;
    this._next = next;
    this._seeds = seeds;
    this._captured = false;
    this._opposite = null;
    this._owner = owner;
  }
  
  public Field (Field f) { //copy constructor
    this(-1, 0, null, null);
    
    this._captured = f.isCaptured();
    this._id = f.getId();
    this._next = f.getNext();
    this._opposite = f.getOpposite();
    this._owner = f.getOwner();
    this._seeds = f.getSeeds();
  }
  
  @Override
  public void sow(Hand hand) {
    if (!isCaptured()) {
    hand.setSeeds(hand.getSeeds() - 1);
    this.setSeeds(this.getSeeds() + 1);
    }
    if (hand.getSeeds() > 0) {
      if ( (this._id == 1 || this._id == 9) &&
          this._owner != hand.getOwner() )
        hand.setCurrentPos(_next.getNext());
      else hand.setCurrentPos(_next);
    }
  }

  @Override
  public Container getOpposite() {
    return _opposite;
  }
  
  public void setOpposite(Container opposite) {
    this._opposite = opposite;
  }
  
  public boolean isCaptured() {
    return _captured;
  }

  public void setCaptured(boolean captured) {
    this._captured = captured;
  }
  
  public Player getOwner() {
    return _owner;
  }

  public void setOwner(Player owner) {
    this._owner = owner;
  }
  
  @Override
  public boolean isHarvestable(Hand hand) {
    if ( _captured || 
        _owner != hand.getOwner() ) return false;
    return true;
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
    return _next;
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
  
}
