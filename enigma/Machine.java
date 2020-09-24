package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;


/** Class that represents a complete enigma machine.
 *  @author Sarah Hashemian
 */
class Machine {

    /** this is variable.
     */
    private Collection<Rotor>  _allRo;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new Rotor[allRotors.size()];
        _rotors = new Rotor[_numRotors];
        int x = 0;
        for (Rotor r: allRotors) {
            _allRotors[x] = r;
            x++;
        }
        _allRo = allRotors;
    }
    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }
    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }
    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        boolean b = false;
        int x = 0;
        int y = 0;
        for (Rotor a : _allRo) {
            for (int i = 0; i < rotors.length; i++) {
                if (a.name().equals(rotors[i])) {
                    x++;
                    _rotors [i] = a;
                }
            }
        }

        int z = 1;
        if (x != 0) {
            if (_rotors[0].reflecting()) {
                if (x == _numRotors) {
                    while (z < _rotors.length) {
                        if (!_rotors[z].reflecting()) {
                            int d = 8;
                        } else {
                            throw new EnigmaException("error in machine 2");
                        }
                        z++;
                    }
                } else {
                    throw new EnigmaException("error in machine 3");
                }
            } else {
                throw new EnigmaException("error in machine 4");
            }
        } else {
            throw new EnigmaException("error in machine 5");
        }

        for (int i = 0; i < rotors.length; i++) {
            if (_rotors[i].rotates()) {
                b = true;
                y = i;
            }
            if (i > y && !_rotors[i].rotates() && b) {
                throw new EnigmaException("error in machine 6");
            }
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int x = numRotors() - 1;
        int z = 0;
        if (setting.length() !=  x) {
            throw error("error!!!!!!!!!!");
        }
        while (z < setting.length()) {
            _alphabet.toInt(setting.charAt(z));
            _rotors[z + 1].set(setting.charAt(z));
            z++;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean q = false;
        for (int i = 0; i < numRotors() - 1; i++) {
            if (_rotors[i].rotates() && _rotors[i + 1].atNotch()) {
                for (int j = i; j < numRotors(); j++) {
                    _rotors[j].advance();
                }
                q = true;
                break;
            }
        }
        if (!q) {
            _rotors[numRotors() - 1].advance();
        }
        int result = c % _alphabet.size();
        if (_plugboard != null) {
            result = _plugboard.permute(c);
        }
        int a = numRotors() - 1;
        while (a >= 0) {
            result = _rotors[a].convertForward(result);
            a--;
        }
        int b = 1;
        while (b < numRotors()) {
            result = _rotors[b].convertBackward(result);
            b++;
        }
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String newMsg = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) != ' ') {
                if (!_alphabet.contains((msg.charAt(i)))) {
                    throw new EnigmaException("error in machine 7");
                }
                char g = msg.charAt(i);
                int x = convert(_alphabet.toInt(g));
                String y = Character.toString(_alphabet.toChar(x));
                newMsg = newMsg.concat(y);
            }
        }
        return newMsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** this is variable.
     */
    private int _numRotors;
    /** this is variable.
     */
    private int _pawls;
    /** this is variable.
     */
    private Rotor[] _allRotors;
    /** this is variable.
     */
    private Rotor[] _rotors;
    /** this is variable.
     */
    private Permutation _plugboard;
}

