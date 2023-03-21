package SHAMIR;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class Shamir_scheme {
    protected final int sizeP = 2048;
    protected BigInteger P;
    protected int[] xi;
    protected int t = 5;
    protected BigInteger[] ai;
    protected BigInteger K;
    public Shamir_scheme(){
        SecureRandom rnd = new SecureRandom();
        P = BigInteger.probablePrime(sizeP, rnd);
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 1];
        rnd.nextBytes(temp);
        K = new BigInteger(temp).abs();
    }

    public Shamir_scheme(int t){
        SecureRandom rnd = new SecureRandom();
        P = BigInteger.probablePrime(sizeP, rnd);
        this.t = t;
        ai = new BigInteger[t - 1];
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 1];
        rnd.nextBytes(temp);
        K = new BigInteger(temp).abs();
    }



    public BigInteger getP() {
        return P;
    }

    public BigInteger getK() {
        return K;
    }

    public int[] initialiseClients(int countClients){
        assert countClients >= t : "n должно быть больше или равно t";
        xi = new int[countClients];
        for (int i = 1; i < xi.length + 1; i++){
            xi[i-1] = i;
        }
        return xi;
    }

    public void chooseAi(){ //Генерация ai
        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] tempNumber = new byte[sizeA - 1];
        for (int i = 0; i < t - 1; i++){
            rnd.nextBytes(tempNumber);
            if (Arrays.equals(tempNumber, new byte[tempNumber.length])){ //Проверка на нулевой массив
                i--;
                continue;
            }
            ai[i] = new BigInteger(tempNumber).abs();
        }
    }

    public BigInteger getYi(int i){
        if (i == 0){
            return BigInteger.valueOf(0);
        }
        BigInteger result = new BigInteger("0");
        result = result.add(K);
        for (int iter = 0; iter < ai.length; iter++){
            BigInteger x = BigInteger.valueOf(i);
            result = result.add(ai[iter].multiply(x.pow(iter + 1)).mod(P)).mod(P);
        }
        return result;
    }


}
