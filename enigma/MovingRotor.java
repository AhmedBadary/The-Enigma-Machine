package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ahmad Badary
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = new int[notches.length()];
        for (int i = 0; i < notches.length(); i++) {
            _notches[i] = (int) notches.charAt(i);
        }
        _set = 0;
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    /** Set setting() to POSN.  */
    @Override
    boolean set(int posn) {
        _set = posn  % (size());
        if (this.atNotch()) {
            return true;
        }
        return false;
    }

    /** Set setting() to character CPOSN. */
    @Override
    boolean set(char cposn) {
        _set = (alphabet().toInt(cposn)) % (size());
        if (this.atNotch(3)) {
            return true;
        }
        return false;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        for (int i : _notches) {
            if (alphabet().toChar(setting()) == i) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean atNotch(int k) {
        for (int i : _notches) {
            if (((alphabet().toChar(_permutation.wrap(setting() - 1)))) == i) {
                return true;
            }
        }
        return false;
    }



    /** Advance me one position, if possible. By default, does nothing. */
    @Override
    void advance() {
        _set = (_set + 1)  % (size());
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    @Override
    int convertForward(int p) {
        return _permutation.wrap(
                (_permutation.permute(_permutation.wrap(p + _set)) - _set));
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    @Override
    int convertBackward(int e) {
        return _permutation.wrap(
                (_permutation.invert((e + _set) % size()) - _set));
    }


    /** An array representation of the notches. */
    private int[] _notches;
}
