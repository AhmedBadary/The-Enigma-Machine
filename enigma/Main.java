package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ahmad Badary
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        pls = false;
        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);

        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
            pls = true;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a setngStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine
     *  from the contents of configuration
     *  file _config and apply it to
     *  the messages in _input, sending the
     *  results to _output. */
    private void process() {
        /** Make a Machine.*/
        if (_config.hasNext()) {
            iMach = readConfig();
        }
        if (proces) {
            setUp(iMach);
            proces = false;
        }
        while (_input.hasNextLine()) {
            try {
                if (_input.hasNext("\\*")) {
                    proces = true;
                    process();
                }
                if (_input.hasNextLine()) {
                    String imsg = _input.nextLine();
                    if (imsg.trim().isEmpty()) {
                        _output.println();
                        continue;
                    }
                    imsg = imsg.toUpperCase();
                    String[] imsgarr = imsg.substring(0).split("\\ ");
                    String[] convertedimsgarr = new String[imsgarr.length];
                    for (int i = 0; i < imsgarr.length; i++) {
                        convertedimsgarr[i] = iMach.convert(imsgarr[i]);
                    }
                    String output = "";
                    for (String str : convertedimsgarr) {
                        output = (output + str + " ");
                    }
                    output = output.trim();
                    output = iformat(output);
                    _output.println(output);
                    if (_input.hasNext("\\*")
                            ||
                            _input.hasNext("\\*B") || _input.hasNext("\\*C")) {
                        proces = true;
                        process();
                    } else if (_input.hasNext()) {
                        process();
                    } else {
                        process();
                    }
                } else {
                    continue;
                }
            } catch (EnigmaException excp) {
                System.err.printf("Error: %s%n", excp.getMessage());
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _allRotors = new ArrayList<Rotor>();
            String currln = _config.nextLine();
            _alphabet = new Alphabet(currln);
            proces = true;
            String currln2 = "";
            int numrotors = _config.nextInt();
            int pivots = _config.nextInt();
            currln = _config.nextLine();
            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numrotors, pivots, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorinfo = (_config.nextLine()).trim();
            if ((rotorinfo.charAt(0) == 'B'
                    &&
                    rotorinfo.charAt(1) == ' ')
                    ||
                    rotorinfo.charAt(0) == 'C') {
                rotorinfo = rotorinfo + " " + (_config.nextLine()).trim();
            }
            String[] strarr = rotorinfo.split("\\ ", 3);
            String iname = strarr[0];
            char itype = strarr[1].charAt(0);
            String inotches = strarr[1].substring(1);
            String icycles = strarr[2].trim();
            if (((icycles.length()
                    -
                    icycles.replace("(", "").length())
                    - (icycles.length() - icycles.replace(")", "").length()))
                    !=
                    0) {
                throw new EnigmaException("bad rotor description");
            }

            switch (itype) {
            case 'M': return new MovingRotor(iname,
                    new Permutation(icycles, _alphabet) , inotches);
            case 'N': return new FixedRotor(iname,
                    new Permutation(icycles, _alphabet));
            case 'R': return new  Reflector(iname,
                    new Permutation(icycles, _alphabet));
            default: return new enigma.Rotor(iname,
                    new Permutation(icycles, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M) {
        try {
            String setng = _input.nextLine();
            while (setng.trim().isEmpty()) {
                if (!pls) {
                    _output.println();
                }
                setng = _input.nextLine();
                if (setng.trim().isEmpty()) {
                    continue;
                }
            }
            setng = setng.toUpperCase();
            String[] strlst;
            String[] rotorsstrlst;
            if (setng.contains("(")) {
                strlst = (setng.substring(1,
                        setng.indexOf("("))).trim().split("\\ ");
                rotorsstrlst = new String[M.numRotors()];
                System.arraycopy(strlst, 0, rotorsstrlst, 0, M.numRotors());
                M.insertRotors(rotorsstrlst);
                M.setRotors(strlst[strlst.length - 1]);
                M.setPlugboard(new Permutation(
                        setng.substring(setng.indexOf("(")), _alphabet));
            } else {
                strlst = setng.substring(1).trim().split("\\ ");
                rotorsstrlst = new String[M.numRotors()];
                System.arraycopy(strlst, 0, rotorsstrlst, 0, M.numRotors());
                M.insertRotors(rotorsstrlst);
                M.setRotors(strlst[strlst.length - 1]);
                M.setPlugboard(new Permutation("", _alphabet));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EnigmaException(
                    "Please provide correct machine settings");
        }
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        char[] msgArr = msg.toCharArray();
        int counter = 0;
        for (char c : msgArr) {
            if (counter == 5) {
                System.out.print(" ");
                counter = 0;
            }
            if (c == ' ') {
                continue;
            }
            System.out.print(c);
            counter++;
        }
        System.out.println();
    }


    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** My available rotors. */
    private Collection<Rotor> _allRotors;

    /** MY MACHINE. */
    private enigma.Machine iMach;

    /** MY MACHINE. */
    private boolean proces, pls;

    /** Correct the format of outputted msg.
     * @param imsg String that is the message to be outputted.
     * @return String of corrected
     */
    private String iformat(String imsg) {
        char[] msgArr = imsg.toCharArray();
        String newmsg = "";
        int counter = 0;
        for (char c : msgArr) {
            if (counter == 5) {
                newmsg += " ";
                counter = 0;
            }
            if (c == ' ') {
                continue;
            }
            newmsg += c;
            counter++;
        }
        return newmsg;
    }
}
