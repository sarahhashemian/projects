package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Sarah Hashemian
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _permutation = perm;
        _notches = notches.toCharArray();
    }
    @Override
    void advance() {
        this.set(setting() + 1);
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** returns rotors notches.
     */
    char[] getNotches() {
        return _notches;
    }

    @Override
    boolean atNotch() {
        char c = _permutation.alphabet().toChar(permutation().wrap(setting()));
        int k = getNotches().length;
        if (k == 0) {
            return true;
        }
        for (int f = 0; f < k; f++) {
            if (getNotches()[f] == c) {
                return true;
            }
        }
        return false;
    }

    /** this is variable.
     */
    private char[] _notches;
    /** this is variable.
     */
    private Permutation _permutation;
}

