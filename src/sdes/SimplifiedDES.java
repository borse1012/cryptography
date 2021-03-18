package sdes;

public class SimplifiedDES {

    // GET RELATED 8 BIT KEY FOR A ROUND
    private String getKeyForRound(String str, int round) {
        for (int i = 1; i < round; i++) {
            str = appendFirstInLast(str);
        }
        return str.substring(0, 8);
    }

    // APPEND FIRST CHAR TO THE END
    private String appendFirstInLast(String str) {
        char[] chars = str.toCharArray();
        // FIRST CHAR
        char temp = chars[0];
        // SWAP ONE POSITION TO LEFT
        for (int k = 1; k < chars.length; k++) {
            chars[k - 1] = chars[k];
        }
        // LAST CHAR
        chars[chars.length - 1] = temp;
        return String.valueOf(chars);
    }

    // EXPANDER FUNCTION
    private int[] expand(int[] arr) {
        int[] expandedArray = new int[8];
        expandedArray[0] = arr[0];
        expandedArray[1] = arr[1];
        expandedArray[2] = arr[3];
        expandedArray[3] = arr[2];
        expandedArray[4] = arr[3];
        expandedArray[5] = arr[2];
        expandedArray[6] = arr[4];
        expandedArray[7] = arr[5];
        return expandedArray;
    }

    // SWITCH LEFT AND RIGHT
    private int switchLR(int cip) {

        String cipherText = String.format("%12s", Integer.toBinaryString(cip)).replace(" ", "0");

        char cipher[] = cipherText.toCharArray();
        int[] left = new int[6];
        int[] right = new int[6];

        //LEFT HALF
        for (int i = 0; i < 6; i++) {
            left[i] = Integer.parseInt("" + cipher[i]);
        }

        //RIGHT HALF
        for (int i = 0; i < 6; i++) {
            right[i] = Integer.parseInt("" + cipher[6 + i]);
        }

        // INTER CHANGE LEFT TO RIGHT AND RIGHT TO LEFT
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : right) {
            stringBuilder.append(i);
        }

        for (int j : left) {
            stringBuilder.append(j);
        }
        // RETURN VALUE
        return Integer.parseInt(stringBuilder.toString(), 2);
    }

    // DECRYPT FOR 4 ROUNDS
    public int decrypt(int ciphertext, int key) {
        int pt = ciphertext;
        String masterKey = String.format("%9s", Integer.toBinaryString(key)).replace(" ", "0");

        // KEYS IN REVERSE ORDER
        for (int i = 4; i > 0; i--) {
            int subKey = Integer.parseInt(getKeyForRound(masterKey, i), 2);
            pt = decryptRound(pt, subKey);
        }

        return pt;
    }

    // DECRYPT FOR ONE ROUND
    private int decryptRound(int ciphertext, int key) {
        // SWITCH LEFT RIGHT
        int cipher = switchLR(ciphertext);
        // RUN ENCRYPTION
        cipher = encryptRound(cipher, key);
        // SWITCH LEFT RIGHT
        cipher = switchLR(cipher);
        return cipher;
    }

    // ENCRYPT FOR 4 ROUNDS
    public int encrypt(int plaintext, int key) {
        int pt = plaintext;
        String masterKey = String.format("%9s", Integer.toBinaryString(key)).replace(" ", "0");

        for (int i = 1; i < 5; i++) {
            int subKey = Integer.parseInt(getKeyForRound(masterKey, i), 2);
            pt = encryptRound(pt, subKey);
        }

        return pt;
    }

    // ENCRYPTION FOR ONE ROUND
    private int encryptRound(int plaintext, int key) {

        String binaryPlainText = String.format("%12s", Integer.toBinaryString(plaintext)).replace(" ", "0");

        char pt[] = binaryPlainText.toCharArray();
        int[] left = new int[6];
        int[] right = new int[6];

        //LEFT HALF
        for (int i = 0; i < 6; i++) {
            left[i] = Integer.parseInt("" + pt[i]);
        }

        //RIGHT HALF
        for (int i = 0; i < 6; i++) {
            right[i] = Integer.parseInt("" + pt[6 + i]);
        }

        //EXPANDER FUNCTION
        int[] expandedArray = expand(right);

        //S1 BOX
        String[][] S1 = new String[2][8];
        S1[0][0] = "101";
        S1[0][1] = "010";
        S1[0][2] = "001";
        S1[0][3] = "110";
        S1[0][4] = "011";
        S1[0][5] = "100";
        S1[0][6] = "111";
        S1[0][7] = "000";
        S1[1][0] = "001";
        S1[1][1] = "100";
        S1[1][2] = "110";
        S1[1][3] = "010";
        S1[1][4] = "000";
        S1[1][5] = "111";
        S1[1][6] = "101";
        S1[1][7] = "011";

        //S2 BOX
        String[][] S2 = new String[2][8];
        S2[0][0] = "100";
        S2[0][1] = "000";
        S2[0][2] = "110";
        S2[0][3] = "101";
        S2[0][4] = "111";
        S2[0][5] = "001";
        S2[0][6] = "011";
        S2[0][7] = "010";
        S2[1][0] = "101";
        S2[1][1] = "011";
        S2[1][2] = "000";
        S2[1][3] = "111";
        S2[1][4] = "110";
        S2[1][5] = "010";
        S2[1][6] = "001";
        S2[1][7] = "100";

        //EXPANDER FUNCTION TO INTEGER
        StringBuilder s = new StringBuilder();
        for (int i : expandedArray) {
            s.append(i);
        }
        int ert = Integer.parseInt(s.toString(), 2);

        //EXPANDER FUNCTION XOR SUBKEY AND CONVERT TO 8 BIT BINARY
        int xor = ert ^ key;
        String FunXOR = String.format("%8s", Integer.toBinaryString(xor)).replace(" ", "0");

        //SPLIT XOR FUNCTION TO S1 AND S2
        char fun[] = FunXOR.toCharArray();
        int[] s1 = new int[4];
        int[] s2 = new int[4];

        for (int i = 0; i < 4; i++) {
            s1[i] = Integer.parseInt("" + fun[i]);
        }

        for (int i = 0; i < 4; i++) {
            s2[i] = Integer.parseInt("" + fun[4 + i]);
        }

        //VALUES FROM S1 AND S2 BOX
        int S1row = s1[0];
        int S2row = s2[0];

        StringBuilder p = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            p.append(s1[i]);
        }
        int S1col = Integer.parseInt(p.toString(), 2);


        StringBuilder q = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            q.append(s2[i]);
        }
        int S2col = Integer.parseInt(q.toString(), 2);

        String S1value = S1[S1row][S1col];
        String S2value = S2[S2row][S2col];

        //S1 AND S2 BOX CONCAT AND CONVERTED TO INT
        String f = S1value + S2value;

        int SFun = Integer.parseInt(f, 2);


        //NEW LEFT
        StringBuilder lt = new StringBuilder();
        for (int i : right) {
            lt.append(i);
        }
        int Nleft = Integer.parseInt(lt.toString());

        //NEW RIGHT
        StringBuilder rt = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            rt.append(left[i]);
        }
        int lt1 = Integer.parseInt(rt.toString(), 2);
        int Fxor = lt1 ^ SFun;

        String Nright = String.format("%6s", Integer.toBinaryString(Fxor)).replace(" ", "0");

        String CRPBinary = Nleft + Nright;
        return Integer.parseInt(CRPBinary, 2);

    }

    public static void main(String[] args) {
        String option = args[0];
        int masterKey = Integer.parseInt(args[1]);
        int plaintext = Integer.parseInt(args[2]);

        SimplifiedDES mainObj = new SimplifiedDES();

        if (option.equalsIgnoreCase("e")) {
            int result = mainObj.encrypt(plaintext, masterKey);
            String binaryPlainText = String.format("%12s", Integer.toBinaryString(result)).replace(" ", "0");
            System.out.println(result + " (" + binaryPlainText + ")");
        } else if (option.equalsIgnoreCase("d"))
            System.out.println(mainObj.decrypt(plaintext, masterKey));
        else
            System.out.println("Invalid option ! ('d' - decrypt, 'e' - encrypt) ");

    }
}
