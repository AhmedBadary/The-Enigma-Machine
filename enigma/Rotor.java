package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ahmad Badary
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _set = 0;
        god = true;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _set;
    }

    /** Set setting() to POSN.
     * @param posn of setting.
     * @return boolean if pos is at n.
     * */
    boolean set(int posn) {
        _set = posn;
        return false;
    }

    /** Set setting() to POSN.
     * @param cposn of setting.
     * @return boolean if pos is at n.
     * */
    boolean set(char cposn) {
        _set = alphabet().toInt((cposn));
        return false;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        return _permutation.permute(p + _set);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return _permutation.invert(e + _set);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }
    /** A function specific to the first Rotor in the machine.
     * Made to be overridden.
     * @param k K is a value that allows the method to be overloaded.
     * @return boolean
     */
    boolean atNotch(int k) {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemnted by this rotor in its 0 position. */
    protected Permutation _permutation;

    /** The current setting the rotor is in. */
    protected int _set;

    /** GOD!!. */
    protected boolean god;
}
