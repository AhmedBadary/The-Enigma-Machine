package enigma;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
import java.util.HashMap;
import static enigma.TestUtils.*;



public class MachineTest {

    protected Alphabet alpha;
    protected Machine testMach0;
    protected Machine testMach1;
    protected Machine testMach2;
    private enigma.Rotor rotor;
    private String ialpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static ArrayList<Rotor> rotors;

    private void makeMachine() {
        MovingRotor test = setRotor("I", NAVALA, "Q");
        rotors = null;
        rotors = new ArrayList<Rotor>();
        rotors.add(test);
        rotors.add(setRotor("II", NAVALA, "E"));
        rotors.add(setRotor("III", NAVALA, "V"));
        rotors.add(setRotor("IV", NAVALA, "J"));
        rotors.add(setRotor("V", NAVALA, "Z"));
        rotors.add(setRotor("VI", NAVALA, "ZM"));
        rotors.add(setRotor("VII", NAVALA, "ZM"));
        rotors.add(setRotor("VIII", NAVALA, "ZM"));
        rotors.add(setRotorFIXED("Beta", NAVALA));
        rotors.add(setRotorFIXED("Gamma", NAVALA));
        rotors.add(setRotorREFLECTOR("B", NAVALA));
        rotors.add(setRotorREFLECTOR("C", NAVALA));




        alpha = new UpperCaseAlphabet();
        testMach0 = new Machine(alpha, 5, 3, rotors);
        testMach1 = new Machine(alpha, 4, 2, rotors);
        testMach2 = new Machine(alpha, 3, 1, rotors);

        String[] rotorsarraytrue0 = {"B", "BETA", "I", "II", "III"};
        testMach0.insertRotors(rotorsarraytrue0);

        String[] rotorsarraytrue1 = {"B", "BETA", "I", "II"};
        testMach1.insertRotors(rotorsarraytrue1);

        String[] rotorsarraytrue2 = {"C", "GAMMA", "IV"};
        testMach2.insertRotors(rotorsarraytrue2);

        String settings0 = "AAAA";
        testMach0.setRotors(settings0);

        String settings1 = "ABA";
        testMach1.setRotors(settings1);

        String settings2 = "AA";
        testMach2.setRotors(settings2);

        enigma.Permutation fornow0 = new enigma.Permutation("(AQ) (EP)", alpha);
        testMach0.setPlugboard(fornow0);

        enigma.Permutation fornow1 = new enigma.Permutation("", alpha);
        testMach1.setPlugboard(fornow1);

        enigma.Permutation fornow2 = new enigma.Permutation("(AR) (ED)", alpha);
        testMach2.setPlugboard(fornow2);

    }

    @Test
    public void numRotors0() throws Exception {
        makeMachine();
        int truenumrotors = 5;
        assertEquals("Machine 0" + " wrong number of rotors"
                , truenumrotors, testMach0.numRotors());
    }

    @Test
    public void numRotors1() throws Exception {
        makeMachine();
        int truenumrotors = 4;
        assertEquals("Machine 1" + " wrong number of rotors"
                , truenumrotors, testMach1.numRotors());
    }

    @Test
    public void numRotors2() throws Exception {
        makeMachine();
        int truenumrotors = 3;
        assertEquals("Machine 2" + " wrong number of rotors"
                , truenumrotors, testMach2.numRotors());
    }



    @Test
    public void numPawls0() throws Exception {
        makeMachine();
        int truenumpawls = 3;
        assertEquals("Machine 0" + " wrong number of Pawls"
                , truenumpawls, testMach0.numPawls());
    }

    @Test
    public void numPawls1() throws Exception {
        makeMachine();
        int truenumpawls = 2;
        assertEquals("Machine 1" + " wrong number of Pawls"
                , truenumpawls, testMach1.numPawls());
    }

    @Test
    public void numPawls2() throws Exception {
        makeMachine();
        int truenumpawls = 1;
        assertEquals("Machine 2" + " wrong number of Pawls"
                , truenumpawls, testMach2.numPawls());
    }


    @Test
    public void insertRotors0() throws Exception {
        makeMachine();
        String[] rotorsarraytrue = {"B", "BETA", "I", "II", "III"};
        testMach0.insertRotors(rotorsarraytrue);
    }

    @Test
    public void insertRotors1() throws Exception {
        makeMachine();
        String[] rotorsarraytrue = {"B", "BETA", "I", "II"};
        testMach1.insertRotors(rotorsarraytrue);
    }

    @Test
    public void insertRotors2() throws Exception {
        makeMachine();
        String[] rotorsarraytrue = {"C", "GAMMA", "IV"};
        testMach2.insertRotors(rotorsarraytrue);
    }


    @Test
    public void setRotors0() throws Exception {
        makeMachine();
        String settings = "AAAA";
        testMach0.setRotors(settings);
    }

    @Test
    public void setRotors1() throws Exception {
        makeMachine();
        String settings = "ABA";
        testMach1.setRotors(settings);
    }

    @Test
    public void setRotors2() throws Exception {
        makeMachine();
        String settings = "MF";
        testMach2.setRotors(settings);
    }


    @Test
    public void setPlugboard0() throws Exception {
        makeMachine();
        enigma.Permutation fornow = new enigma.Permutation("", alpha);
        testMach0.setPlugboard(fornow);
    }

    @Test
    public void setPlugboard1() throws Exception {
        makeMachine();
        enigma.Permutation fornow = new enigma.Permutation("", alpha);
        testMach1.setPlugboard(fornow);
    }

    @Test
    public void setPlugboard2() throws Exception {
        makeMachine();
        enigma.Permutation fornow =
                new enigma.Permutation("(FG) (AD)", alpha);
        testMach2.setPlugboard(fornow);
    }


    @Test
    public void convert0() throws Exception {
        makeMachine();

        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int reschar1 = testMach0.convert(char1);
        int reschar2 = testMach0.convert(char2);
        assertEquals("Machine 0" + " wrong Converted char1 (H)",
                24, reschar1);
        assertEquals("Machine 0" + " wrong Converted char2 (E)",
                22, reschar2);
    }

    @Test
    public void convert1() throws Exception {
        makeMachine();

        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int reschar1 = testMach1.convert(char1);
        int reschar2 = testMach1.convert(char2);
        assertEquals("Machine 1" + " wrong Converted char1 (H)"
                , 1, reschar1);
        assertEquals("Machine 1" + " wrong Converted char2 (E)"
                , 22, reschar2);

    }

    @Test
    public void convert2() throws Exception {
        makeMachine();
        int char1, char2;
        char1 = 'H' - 'A';
        char2 = 'E' - 'A';
        int reschar1 = testMach2.convert(char1);
        int reschar2 = testMach2.convert(char2);
        assertEquals("Machine 2" + " wrong Converted char1 (H)"
                , ('P' - 'A'), reschar1);
        assertEquals("Machine 2" + " wrong Converted char2 (E)"
                , ('R' - 'A'), reschar2);
    }



    @Test
    public void convertstr0() throws Exception {
        makeMachine();

        String str1, str2;
        str1 = "HELLO WORLD";
        str2 = "YWTGM YIAPU";
        String resstr1 = testMach0.convert(str1);
        assertEquals("Machine 0"
                        +
                        " wrong Converted str1 (\"HELLO WORLD\")",
                str2, resstr1);
        makeMachine();
        String resstr2 = testMach0.convert(str2);
        assertEquals("Machine 0"
                +
                " wrong Converted str2 (\"ILBDA AMTAZ\")", str1, resstr2);
    }

    @Test
    public void actual() throws Exception {
        String str1, str2;
        str1 = "HELLO WORLD";
        str2 = "ILBDA AMTAZ";
        makeMachine();
    }

    public static MovingRotor setRotor(String name,
                                       HashMap<String,
                                               String> irotors,
                                       String notches) {
        MovingRotor rotor = new MovingRotor(name,
                new Permutation((String) irotors.get(name),
                        TestUtils.UPPER), notches);
        return (MovingRotor) rotor;
    }

    public static FixedRotor setRotorFIXED(String name,
                                           HashMap<String, String> irotors) {
        FixedRotor rotor = new FixedRotor(name,
                new Permutation((String) irotors.get(name), TestUtils.UPPER));
        return rotor;
    }

    public static Reflector setRotorREFLECTOR(String name,
                                              HashMap<String, String> irotors) {
        Reflector rotor = new Reflector(name,
                new Permutation((String) irotors.get(name), TestUtils.UPPER));
        return rotor;
    }
}

