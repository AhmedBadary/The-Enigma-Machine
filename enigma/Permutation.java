package enigma;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ahmad Badary
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        this.ipermutationtable =
                new int[alphabet.size()];
        map(cycles, this.ipermutationtable);
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        map(cycle, this.ipermutationtable);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt((char) (this.ipermutationtable[(p)]));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return indexof(ipermutationtable, _alphabet.toChar(c));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return  _alphabet.toChar((permute(_alphabet.toInt(p))));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return _alphabet.toChar(invert((_alphabet.toInt(c))));
    }

    /** Return the alphabet used to initialize this Permutation. */
    enigma.Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return (this.derangment == 0);
    }

    /** Alphabet of this permutation. */
    private enigma.Alphabet _alphabet;
    /** Boolean for derangment. */
    private int derangment;


    /** The mapping table. */
    protected int[] ipermutationtable;


    /**
     * Creates an array that stores information of the permutation cycles
     * of a given rotor; the array behaves in constant time.
     * @param perm is a string representing the cycles.
     * @param permtable is an array that will include the representation
     *                  of the cycles.
     */
    private void map(String perm, int[] permtable) {
        this.derangment = 0;
        String[] parts = perm.split("\\)");
        for (int len = 0; len < parts.length; len++) {
            int k = -1;
            for (char i : (_alphabet.getMyAlphabet()).toCharArray()) {
                int index = parts[len].indexOf((char) i);
                k++;
                if (index >= 0) {
                    if (parts[len].charAt((index + 1)
                            %
                            parts[len].length()) == ' '
                            ||
                            parts[len].charAt((index + 1)
                            %
                            parts[len].length()) == '(') {
                        if (parts[len].charAt((index + 1)
                                %
                                parts[len].length()) == ' ') {
                            ipermutationtable[k] = parts[len].charAt(2);
                        } else {
                            ipermutationtable[k] = parts[len].charAt(1);
                        }
                    } else {
                        ipermutationtable[k] = parts[len].charAt(
                                (index + 1) % (parts[len].length()));
                        derangment++;
                    }
                } else if (ipermutationtable[k] != 0) {
                    continue;
                } else {
                    ipermutationtable[k] = i;
                }
            }
        }
    }

    /**
     * Get the index of a letter in the permutation table.
     * @param arr is the ARR.
     * @param a A is the letter index we need.
     * @return int Index.
     */
    public static int indexof(int[] arr, int a) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == a) {
                return i;
            }
        }
        return -1;
    }
}

