package Feldman;

import java.math.BigInteger;

public class Client {
    private int i;
    private BigInteger yi;
    private BigInteger P;
    private BigInteger g;
    private BigInteger[] gai = null;
    private BigInteger ziMust;
    private boolean agree = false;
    private StringBuffer log = new StringBuffer();

    private String preString;
    Client(){
        i = 0;
        yi = BigInteger.valueOf(0);
    }

    Client(int i, BigInteger P, BigInteger[] gai, BigInteger g){
        this.g = g;
        this.gai = gai;
        this.P = P;
        preString = "Client " + i + ":";
        log.append(preString + "Set i = " + i + "\n");
        this.i = i;

        BigInteger result = new BigInteger("1");
        BigInteger ii = BigInteger.valueOf(i);

        for (int iter = 0; iter < gai.length; iter++){
            result = result.multiply(gai[iter].modPow(ii.pow(iter), P)).mod(P);
        }
        ziMust = result;
    }

    public String getLog(){
        return log.toString();
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public BigInteger getYi() {
        log.append(preString + "Request:Get yi = " + yi + "\n");
        return yi;
    }

    public void setYi(BigInteger yi, BigInteger zi) {
        if (!zi.equals(g.modPow(yi, P))){
            agree = false;
            log.append(preString + "Failed yi. Check is False!!!" + "\n");
            return;
        }
        log.append(preString + "Set yi = " + yi + "\n");
        agree = true;
        this.yi = yi;
    }
}
