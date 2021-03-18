package sdes;

public class Simplified2DES {

    //Simple DES global object
    private SimplifiedDES mainObj;

    public Simplified2DES(){
        //Initialize Simple DES global object
        mainObj = new SimplifiedDES();
    }

    public int encrypt2DES(int plaintext, int key1, int key2){

        //apply first des encryption using first key
        int result = mainObj.encrypt(plaintext, key1);

        //apply first des encryption using second key
        int result2 = mainObj.encrypt(result, key2);

        // RETURN encrypted value
        return result2;
    }

    public int decrypt2DES(int ciphertext, int key1, int key2){

        //apply first des decryption using second key
        int result = mainObj.decrypt(ciphertext, key2);

        //apply first des decryption using first key
        int result2 =  mainObj.decrypt(result, key1);

        // RETURN decrypted
        return result2;
    }

    public static void main(String[] args) {
        String option = args[0];
        int masterKey1 = Integer.parseInt(args[1]);
        int masterKey2 = Integer.parseInt(args[2]);
        int plaintext = Integer.parseInt(args[3]);

        Simplified2DES mainObj = new Simplified2DES();

        if (option.equalsIgnoreCase("e")) {
            int result = mainObj.encrypt2DES(plaintext, masterKey1, masterKey2);
            String binaryPlainText = String.format("%12s", Integer.toBinaryString(result)).replace(" ", "0");
            System.out.println(result + " (" + binaryPlainText + ")");
        } else if (option.equalsIgnoreCase("d"))
            System.out.println(mainObj.decrypt2DES(plaintext, masterKey1, masterKey2));
        else
            System.out.println("Invalid option ! ('d' - decrypt, 'e' - encrypt) ");

    }
}
