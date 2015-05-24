/**
 * Sungka Player class file
 * Implements a sungka player
 *
 * @author Roseller M. Velicaria, Jr.
 * 10-12-2011
 * email: rvelicaria@ucmerced.edu
 */
package com.devars;

public class Player {
    public static final int HUMAN = 0;
    public static final int PC = 1;

    private int _id;
    private Store _store;

    public Player(int id) {
        this._id = id;
        this._store = null;
    }

    public int getScore() {
        return this._store.getSeeds();
    }

    public Store getStore() {
        return _store;
    }

    public void setStore(Store s) {
        this._store = s;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

}
