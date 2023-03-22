package Pederson;

import SHAMIR.Shamir_scheme;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class Pederson_scheme extends Shamir_scheme {
    protected BigInteger Q;
    protected String preString = "[Pederson_scheme]:";
    private BigInteger g;
    private BigInteger h;
    protected BigInteger[] siGi;
    private BigInteger[] gai;
    private BigInteger[] epsm;

    Pederson_scheme(){
        super();
    }
    Pederson_scheme(int t){
        super(t);
        siGi = new BigInteger[t];

        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 1];
        rnd.nextBytes(temp);
        Q = new BigInteger(P.subtract(BigInteger.valueOf(1)).toString());
        Q = BigInteger.probablePrime(2040, rnd);
        K = K.mod(Q);
        BigInteger k = new BigInteger("2");
        while (!Q.multiply(k).add(BigInteger.valueOf(1)).isProbablePrime(100)){
            k = k.add(BigInteger.valueOf(1));
        }
        P = Q.multiply(k).add(BigInteger.valueOf(1));
        chooseAi();
        g = generateG();
        epsm = new BigInteger[t];
        epsm[0] = g.modPow(K, P).multiply(h.modPow(siGi[0], P)).mod(P);
        for (int iter = 1; iter < t; iter++){
            epsm[iter] = g.modPow(ai[iter-1], P).multiply(h.modPow(siGi[iter], P)).mod(P);
        }

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
        for (int i = 0; i < t; i++){
            rnd.nextBytes(tempNumber);
            if (Arrays.equals(tempNumber, new byte[tempNumber.length])){ //Проверка на нулевой массив
                i--;
                continue;
            }
            siGi[i] = new BigInteger(tempNumber).abs().mod(Q);
        }
    }

    public BigInteger generateG(){
        SecureRandom rnd = new SecureRandom();
        int sizeA = sizeP / 8;
        byte[] temp = new byte[sizeA - 2];

        g = new BigInteger("3");
        while (g.modPow(P.subtract(BigInteger.valueOf(1)).divide(Q), P).compareTo(Q) != -1){
            g = g.add(BigInteger.valueOf(1));
        }
        g = g.modPow(P.subtract(BigInteger.valueOf(1)).divide(Q), P);
        rnd.nextBytes(temp);
        while (Arrays.equals(temp, new byte[sizeA - 2])){
            rnd.nextBytes(temp);
        }
        h = new BigInteger(temp).abs().mod(Q);
        return g;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger[] getCheckNumbers(){
        return epsm;
    }

    public BigInteger getH() {
        return h;
    }

    public BigInteger getSiGi(int i){

        BigInteger result = new BigInteger("0");
        result = result.add(siGi[0]);
        for (int iter = 1; iter < siGi.length; iter++){
            BigInteger x = BigInteger.valueOf(i);
            result = result.add(siGi[iter].multiply(x.pow(iter)));
        }
        return result.mod(Q);
    }

    public BigInteger getZi(int i){
        return g.modPow(getYi(i), P);
    }


    public static void main(String[] args) {
        Pederson_scheme feldman_scheme = new Pederson_scheme(5);
        System.out.println(feldman_scheme.getK());
        System.out.println(feldman_scheme.K);
        BigInteger g = feldman_scheme.generateG();
        System.out.println(feldman_scheme.generateG());
    }
}
