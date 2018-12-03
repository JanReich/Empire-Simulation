package engine.cryptography;

import engine.toolBox.Math;

import java.math.BigInteger;

public class RSA {

            //Attribute
        private long p;
        private long q;

        private long e;
        private long d = 0;
        private long n;
        private long phiN;

            //Referenzen
        private String publicKey;
        private String privateKey;

    public RSA() {

        p = Math.generatePrime((int) (java.lang.Math.random() * 1000) + 2000);
        q = Math.generatePrime((int) (java.lang.Math.random() * 1000) + 1000);

        generateKeys();
    }

    private void generateKeys() {

            //public Key (N und e)
        n = p * q;
        phiN = (p - 1) * (q - 1);

        e = Math.generatePrime((int) (java.lang.Math.random() * 1000));
        if(phiN % e == 0) {

            generateKeys();
            return;
        }

            //private Key (N und d)
        for(; true; d++){
            if(((e * d) % phiN == 1)){

                break;
            }
        }

        publicKey = "PublicKey: e: " + e + ": n: " + n;
        privateKey = "PrivateKey: d: " + d + ": n: " + n;
    }

    public String encryptMessage(String message, String publicKey) {

            //Format: PublicKey: e: [e]: n: [n]
        String encryptedMessage = "";
        String[] key = publicKey.split(": ");

        long e = Integer.parseInt(key[2]);
        long n = Integer.parseInt(key[4]);
        char[] letters = message.toCharArray();

        for(char letter : letters) {

            encryptedMessage += encrypt(charToInt(letter), e, n) + " ";
        }

        return encryptedMessage;
    }

    public String decryptMessage(String message, String privateKey) {

            //Format: PrivateKey: d: [d]: n: [n]
        String decryptedMessage = "";
        String[] key = privateKey.split(": ");

        long d = Integer.parseInt(key[2]);
        long n = Integer.parseInt(key[4]);
        String[] encryptedLetters = message.split(" ");

        for (String letter : encryptedLetters) {

            decryptedMessage += intToChar(Integer.parseInt(decrypt(Integer.parseInt(letter), d, n)));
        }

        return decryptedMessage;
    }

    private int charToInt(char value) {

        return (int) value;
    }

    private char intToChar(int value) {

        return (char) value;
    }

    private BigInteger encrypt(long text, long e, long n) {

        BigInteger textBig = new BigInteger(text + "");
        BigInteger eBig = new BigInteger(e + "");
        BigInteger nBig = new BigInteger(n + "");
        BigInteger message = textBig.modPow(eBig, nBig);

        return message;
    }

    private String decrypt(long text, long d, long n) {

        BigInteger textBig = new BigInteger(text + "");
        BigInteger dBig = new BigInteger(d + "");
        BigInteger nBig = new BigInteger(n + "");

        BigInteger message = textBig.modPow(dBig, nBig);
        return message.toString();
    }

        //GETTER AND SETTER

    public String getPublicKey() {

        return publicKey;

    }

    public String getPrivateKey() {

        return privateKey;
    }
}