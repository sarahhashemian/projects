package enigma;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Sarah Hashemian
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = "";
        int x = 0;
        int y = 0;
        for (int i = 0; i < _alphabet.size(); i++) {
            char t = _alphabet.chars().charAt(i);
            if (t == '(' || t == ')') {
                throw new EnigmaException("error in permutation 1");
            }
        }
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                x++;
            }
            if (cycles.charAt(i) == ')') {
                y++;
            }
        }
        if (x != y) {
            throw new EnigmaException("Poor Permutation constructor");
        }
        if (x == 1) {
            cycles = cycles.replace("(", "");
            cycles = cycles.replace(")", "");
            addCycle(cycles);
        }
        if (x > 1) {
            cycles = cycles.replace(") (", ")(");
            cycles = cycles.replace(")(", "x");
            cycles = cycles.replace(")", "");
            cycles = cycles.replace("(", "");
            cycles = cycles.replace("x", " ");
            addCycle(cycles);
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            char w = cycle.charAt(i);
            if (!_alphabet.contains(w) && (w != ' ')) {
                throw new EnigmaException("error in permutation 3");
            }
        }
        _cycles = cycle;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int index = wrap(p);
        char a = _alphabet.toChar(index);
        char c = permute(a);
        return _alphabet.toInt(c);
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        int index = wrap(c);
        char a = _alphabet.toChar(index);
        char d = invert(a);
        return _alphabet.toInt(d);
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        if (!_cycles.contains(Character.toString(p))) {
            if (_alphabet.contains(p)) {
                return p;
            } else {
                throw new EnigmaException("error in permutation 4");
            }
        }
        int pIndex = _cycles.indexOf(Character.toString(p));
        if (_cycles.charAt(_cycles.length() - 1) == p) {
            if (_cycles.length() == 1) {
                return p;
            }
            if (_cycles.charAt(_cycles.length() - 2) == ' ') {
                return p;
            }
        }
        if (pIndex + 1 < _cycles.length()) {
            if (_cycles.charAt(pIndex + 1) != ' ') {
                return _cycles.charAt(pIndex + 1);
            }
        }
        if (_cycles.charAt(pIndex - 1) == ' ') {
            return _cycles.charAt(pIndex);
        }
        char x = '!';
        while (_cycles.charAt(pIndex - 1) != ' ' || pIndex - 1 < 0) {
            x = _cycles.charAt(pIndex - 1);
            pIndex = pIndex - 1;
            if (pIndex == 0) {
                break;
            }
        }
        return x;
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        if (!_cycles.contains(Character.toString(c))) {
            if (_alphabet.contains(c)) {
                return c;
            } else {
                throw new EnigmaException("error in permutation 5");
            }
        }
        int pIndex = _cycles.indexOf(Character.toString(c));
        if (_cycles.charAt(_cycles.length() - 1) == c) {
            if (_cycles.length() == 1) {
                return c;
            }
            if (_cycles.charAt(_cycles.length() - 2) == ' ') {
                return c;
            }
        }
        if (pIndex - 1 >= 0) {
            if (_cycles.charAt(pIndex - 1) != ' ') {
                return _cycles.charAt(pIndex - 1);
            }
        }
        if (_cycles.charAt(pIndex + 1) == ' ') {
            return _cycles.charAt(pIndex);
        }
        char x = '!';
        while (_cycles.charAt(pIndex + 1) != ' ') {
            x = _cycles.charAt(pIndex + 1);
            pIndex = pIndex + 1;
            if (pIndex == _cycles.length() - 1) {
                break;
            }
        }
        return x;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        boolean x = false;
        if (_alphabet.size() == 1) {
            return false;
        }
        for (int i = 0; i < _alphabet.size(); i++) {
            String y = _alphabet.chars();
            if (_cycles.contains(y.substring(i, i))) {
                x = true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /** this is variable.
     */
    private String _cycles;
}
