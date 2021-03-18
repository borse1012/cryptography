package sdes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class MITM {

    SimplifiedDES des;

    public MITM(){
        des = new SimplifiedDES();
    }

    public Hashtable<Integer, Integer> getKey1UsingPossibleKey1(int plaintext, List<Integer> possibleKey1){
        Hashtable<Integer, Integer> hashTable = new Hashtable();
        for(int key1: possibleKey1){
            int result = des.encrypt(plaintext, key1);
            hashTable.put(result,key1);
        }
        return hashTable;
    }

    public Hashtable<Integer, Integer> getKey2UsingPossibleKey2(int cipherText, List<Integer> possibleKey2){
        Hashtable<Integer, Integer> hashTable = new Hashtable();
        for(int key1: possibleKey2){
            int result = des.decrypt(cipherText, key1);
            hashTable.put(result,key1);
        }
        return hashTable;
    }

    public List<Integer> getInitialKeys(){
        List<Integer> possibleKeys = new ArrayList<>();
        int max = 512;
        int min = 1;
        for(int i = min; i<= max ; i++){
                possibleKeys.add(i);
        }

        return possibleKeys;
    }

    public static void main(String[] args) {
        int plaintext_1 = Integer.parseInt(args[0]);
        int ciphertext_1 = Integer.parseInt(args[1]);
        int plaintext_2 = Integer.parseInt(args[2]);
        int ciphertext_2 = Integer.parseInt(args[3]);

        MITM mitm = new MITM();

        List<Integer> possibleKey1 = mitm.getInitialKeys();
        List<Integer> possibleKey2 =  mitm.getInitialKeys();
        int loopCount = 0;

        while (possibleKey1.size() > 1) {

            //Performing MIMT on first plaintext/ciphertext pair
            Hashtable<Integer, Integer> key1Pair = mitm.getKey1UsingPossibleKey1(plaintext_1, possibleKey1);
            Hashtable<Integer, Integer> key2Pair = mitm.getKey2UsingPossibleKey2(ciphertext_1, possibleKey2);

            possibleKey1 = new ArrayList<>();
            possibleKey2 = new ArrayList<>();
            Set<Integer> keys = key1Pair.keySet();
            for(Integer key : keys){
                if(key2Pair.containsKey(key)){
                    possibleKey1.add(key1Pair.get(key));
                    possibleKey2.add(key2Pair.get(key));
                }
            }

            //Performing MIMT on second plaintext/ciphertext pair
            key1Pair = mitm.getKey1UsingPossibleKey1(plaintext_2, possibleKey1);
            key2Pair = mitm.getKey2UsingPossibleKey2(ciphertext_2, possibleKey2);

            possibleKey1 = new ArrayList<>();
            possibleKey2 = new ArrayList<>();
            keys = key1Pair.keySet();
            for (Integer key : keys) {
                if (key2Pair.containsKey(key)) {
                    possibleKey1.add(key1Pair.get(key));
                    possibleKey2.add(key2Pair.get(key));
                }
            }

            System.out.println("Possible Key Pairs====");
            for(int i=0;i<possibleKey1.size();i++){
                System.out.println("Key1=" + possibleKey1.get(i) + ", Key2=" + possibleKey2.get(i));
            }

            loopCount++;
            if(loopCount >= 1000)
                break;
        }

        if(possibleKey1.size()!=0) {
            System.out.println("Final matched key Pairs====");
            for(int i=0;i<possibleKey1.size();i++){
                System.out.println("Key1=" + possibleKey1.get(i) + ", Key2=" + possibleKey2.get(i));
            }
        }else
            System.out.println("Please re-Run program...");


    }
}
