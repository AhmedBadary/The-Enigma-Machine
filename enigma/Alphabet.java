package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ahmed Badary
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        myAlphabet = chars;
        firstChar = myAlphabet.charAt(0);
        lastChar = myAlphabet.charAt(myAlphabet.length() - 1);
    }

    /** Returns the size of the alphabet. */
    int size() {
        return (myAlphabet.length());
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return myAlphabet.contains((String.valueOf(c)));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return myAlphabet.charAt(index);
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        return myAlphabet.indexOf(c);
    }

    /** Getter for MYALPHABET.
     * @return String MYALPHABET.
     * */
    public String getMyAlphabet() {
        return myAlphabet;
    }

    /** A string of the alphabet to use. */
    protected String myAlphabet;

    /** Getter for FIRSTCHAR.
     * @return String MYALPHABET.
     * */
    public int getFirstChar() {
        return firstChar;
    }

    /** The int representation of the first Char in my alphabet. */
    protected int firstChar;

    /** The int representation of the First Char in my alphabet.
     * @return String MYALPHABET.
     * */
    public int getLastChar() {
        return lastChar;
    }

    /** The int representation of the Last Char in my alphabet. */
    protected int lastChar;

}
