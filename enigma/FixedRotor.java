package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Ahmad Badary
 */
class FixedRotor extends Rotor {
    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    @Override
    int convertForward(int p) {
        return _permutation.wrap((_permutation.permute(
                _permutation.wrap(p + _set)) - _set));
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    @Override
    int convertBackward(int e) {
        return _permutation.wrap((_permutation.invert(
                (e + _set) % size()) - _set));
    }
    /** Set setting() to POSN.  */
    @Override
    boolean set(int posn) {
        _set = posn  % (size());
        return false;
    }

    /** Set setting() to character CPOSN. */
    @Override
    boolean set(char cposn) {
        _set = ((int) alphabet().toInt((cposn)) % (size()));
        return false;
    }
}
