package Feldman;

import SHAMIR.Shamir_scheme;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class Feldman_scheme extends Shamir_scheme {
    protected BigInteger Q;
    protected String preString = "[Feldman_scheme]:";
    private BigInteger g;
    private BigInteger[] gai;

    Feldman_scheme(){
        super();
    }
    Feldman_scheme(int t){
        super(t);

        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 1];
        rnd.nextBytes(temp);
        Q = new BigInteger(P.subtract(BigInteger.valueOf(1)).toString());
        Q = BigInteger.probablePrime(2040, rnd);
        BigInteger k = new BigInteger("2");
        while (!Q.multiply(k).add(BigInteger.valueOf(1)).isProbablePrime(100)){
            k = k.add(BigInteger.valueOf(1));
        }
        temp = new byte[sizeA - 2];
        rnd.nextBytes(temp);
        K = new BigInteger(temp).abs();

        P = Q.multiply(k).add(BigInteger.valueOf(1));
        chooseAi();
        g = generateG();
        gai = new BigInteger[t];
        gai[0] = g.modPow(K, P);
        for (int iter = 1; iter < t; iter++){
            gai[iter] = g.modPow(ai[iter-1], P);
        }

    }

    public BigInteger generateG(){
        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 2];
        while (Arrays.equals(temp, new byte[sizeA - 2])){
            rnd.nextBytes(temp);
        }

        g = new BigInteger(temp).abs().mod(Q);
        g = g.modPow(P.subtract(BigInteger.valueOf(1)).divide(Q), P);
        return g;
    }

    public void chooseAi(){ //Генерация ai
        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] tempNumber = new byte[sizeA - 2];
        for (int i = 0; i < t - 1; i++){
            rnd.nextBytes(tempNumber);
            if (Arrays.equals(tempNumber, new byte[tempNumber.length])){ //Проверка на нулевой массив
                i--;
                continue;
            }
            ai[i] = new BigInteger(tempNumber).abs().mod(Q);
        }
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger[] getCheckNumbers(){
        return gai;
    }


    public BigInteger getYi(int i) {

        BigInteger result = new BigInteger("0");
        result = result.add(K);
        for (int iter = 0; iter < ai.length; iter++){
            BigInteger x = BigInteger.valueOf(i);
            result = result.add(ai[iter].multiply(x.pow(iter + 1)).mod(Q)).mod(Q);
        }
        return result;
    }

    public BigInteger getZi(int i){
        return g.modPow(getYi(i), P);
    }


    public static void main(String[] args) {
        Feldman_scheme feldman_scheme = new Feldman_scheme(5);
        System.out.println(feldman_scheme.getK());
        System.out.println(feldman_scheme.K);
        BigInteger g = feldman_scheme.generateG();
        System.out.println(feldman_scheme.generateG());
    }
}
