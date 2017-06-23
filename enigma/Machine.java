package enigma;

import java.util.Collection;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ahmad Badary
 */

class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray(new enigma.Rotor[allRotors.size()]);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }


    /** Get the current rotors that are inserted in the machine.
     * @return Rotor[].
     */
    public enigma.Rotor[] getCurrRotors() {
        return _currRotors;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int len = rotors.length;
        String rotorsuc = "";
        int nummoving = 0;
        _currRotors = new enigma.Rotor[len + 1];
        for (int i = 0; i < len; i++) {
            for (enigma.Rotor rotor : _allRotors) {
                rotorsuc = rotor.name().toUpperCase();
                if (rotors[i].equals(rotorsuc)) {
                    _currRotors[i] = rotor;
                    if (_currRotors[i].rotates()) {
                        nummoving++;
                    }
                }
            }
            if (_currRotors[i] == null) {
                throw new enigma.EnigmaException("bad rotor name");
            }
        }
        if (!_currRotors[0].reflecting()) {
            throw new enigma.EnigmaException("Reflector in wrong place");
        }
        if (nummoving != _pawls) {
            throw new enigma.EnigmaException("Wrong number of arguments");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int len = setting.length();
        boolean phony = false;
        for (int i = numRotors() - 1; i >= 1; i--) {
            if (len >= 0) {
                try {
                    if (_currRotors[i].set(
                            _alphabet.toInt((setting.charAt(len - 1))))
                            &&
                            i != numRotors() - 1) {
                        if (!phony) {
                            _currRotors[i].god = false;
                            _currRotors[i - 1].god = false;
                        }
                        phony = _currRotors[i - 1].set(
                                _alphabet.toInt((setting.charAt(len - 2))));
                        _currRotors[i - 1].advance();
                        len--;
                        i--;
                    } else  {
                        phony = _currRotors[i].set(
                                _alphabet.toInt((setting.charAt(len - 1))));
                        if (phony) {
                            _currRotors[i - 1].god = true;
                        }
                    }
                } catch (StringIndexOutOfBoundsException excp) {
                    throw new enigma.EnigmaException(
                            "Settings are in incorrect"
                                    +
                                    " are not specified correctly");
                }
            }
            len--;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _currRotors[_numRotors] = new
                enigma.Reflector("plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int letter;
        int i = _numRotors;
        letter = _currRotors[i].convertForward(c);
        i--;
        _currRotors[i].advance();
        letter = _currRotors[i].convertForward(letter);
        i--;
        int temp = 0;
        for (; i >= 0; i--) {
            if ((i + 1 == _numRotors - 1)
                    &&
                    ((_currRotors[i + 1])).atNotch(3)) {
                temp = letter;
                _currRotors[i].advance();
                letter = _currRotors[i].convertForward(letter);
            } else if ((i + 1 != _numRotors - 1)
                    &&
                    (_currRotors[i + 1]).atNotch()) {
                if (!(_currRotors[i + 1].god)
                        &&
                        (_currRotors[i] instanceof MovingRotor)) {
                    _currRotors[i + 1].advance();
                    letter = _currRotors[i + 1].convertForward(temp);
                    if (_currRotors[i].god) {
                        _currRotors[i].advance();
                    }
                    letter = _currRotors[i].convertForward(letter);
                    _currRotors[i].god = true;
                    _currRotors[i + 1].god = true;
                } else {
                    temp = letter;
                    letter = _currRotors[i].convertForward(letter);
                    _currRotors[i + 1].god = false;
                }
            } else {
                temp = letter;
                letter = _currRotors[i].convertForward(letter);
            }
        }
        for (i = 1; i < _numRotors + 1; i++) {
            letter = _currRotors[i].convertBackward(letter);
        }
        return letter;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int toconv = 0;
        char[] imsg = msg.toCharArray();
        String imsgstr = "";
        for (int i = 0; i < imsg.length; i++) {
            if (imsg[i] == ' ') {
                imsgstr += " ";
                continue;
            }
            if (!(_alphabet.contains(imsg[i]))) {
                continue;
            }
            toconv = _alphabet.toInt(imsg[i]);
            imsg[i] = _alphabet.toChar((convert(toconv)));
            imsgstr += String.valueOf(imsg[i]);
        }
        return imsgstr;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors.*/
    private int _numRotors;

    /** Number of moving rotors.*/
    private int _pawls;

    /** My available rotors.*/
    private enigma.Rotor[] _allRotors;

    /** My current rotors.*/
    private enigma.Rotor[] _currRotors;



}
