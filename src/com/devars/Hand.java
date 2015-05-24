/**
 * Sungka Hand class file
 * Class that acts on the board to do sows, harvests and captures
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

public class Hand {
    private int _seeds;
    private Container _currentPos;
    private Player _owner;

    public Hand(Container field, Player owner) {
        this._seeds = 0;
        this._currentPos = field;
        this._owner = owner;
    }

    public boolean update() {
        while (_currentPos.isCaptured() ||
                (getOwner().getId() == 0 && this.getCurrentPos().getId() == 8) ||
                (getOwner().getId() == 1 && this.getCurrentPos().getId() == 0)) {
            _currentPos = _currentPos.getNext();
        }
        if (this._seeds > 1) {
            //  sow seeds if we aren't on our last seed,
            // or if the last seed ends on a store
            sow();
            return false;
        } else if (this._seeds == 1 &&
                !this._currentPos.isHarvestable(this) &&
                !this._currentPos.isCaptured()) { //either it's a storage or not your field
            sow();
            return true;
        } else if (this._seeds == 1 &&
                this._currentPos.getSeeds() > 0 &&
                this._currentPos.isHarvestable(this)) {
            harvest(this._currentPos);
            return false;
        } else if (this._currentPos.getSeeds() == 0 &&
                this._currentPos.isHarvestable(this) &&
                !this._currentPos.getOpposite().isCaptured()) {
            capture();
            return true;
        } else {
            sow();  //we got into an empty field
        }
        return true;
    }

    public void sow() { //sows seed and moves to next container
        this._currentPos.sow(this);
    }

    public void capture() {
        if (_currentPos.isHarvestable(this) &&
                !_currentPos.getOpposite().isCaptured()) {
            this._owner.getStore().incSeeds(_currentPos.getOpposite().getSeeds());
            _currentPos.getOpposite().setSeeds(0);
            _currentPos.getOpposite().setCaptured(true);
            _currentPos.setSeeds(1);
            this._seeds = 0;
        }
    }

    public void harvest(Container field) {
        if (field.isHarvestable(this) && !field.isCaptured()) {
            this._seeds += field.getSeeds();
            field.setSeeds(0);
            if ((field.getId() == 1 || field.getId() == 9) &&
                    this._owner != field.getOwner())
                this.setCurrentPos(field.getNext().getNext());
            else this.setCurrentPos(field.getNext());
        }
    }

    public Container getCurrentPos() {
        return _currentPos;
    }

    public void setCurrentPos(Container currentPos) {
        this._currentPos = currentPos;
    }

    public int getSeeds() {
        return _seeds;
    }

    public void setSeeds(int seeds) {
        this._seeds = seeds;
    }

    public Player getOwner() {
        return _owner;
    }

}
