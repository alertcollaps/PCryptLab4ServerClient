package Pederson;

import java.math.BigInteger;

public class Client {
    private int i;
    private BigInteger yi;
    private BigInteger P;
    private BigInteger g;
    private BigInteger h;
    private BigInteger[] epsi = null;
    private BigInteger ziMust;
    private boolean agree = false;
    private StringBuffer log = new StringBuffer();

    private String preString;
    Client(){
        i = 0;
        yi = BigInteger.valueOf(0);
    }

    Client(int i, BigInteger P, BigInteger[] epsi, BigInteger g, BigInteger h){
        this.g = g;
        this.epsi = epsi;
        this.P = P;
        this.h = h;
        preString = "Client " + i + ":";
        log.append(preString + "Set i = " + i + "\n");
        this.i = i;

        BigInteger result = new BigInteger("1");
        BigInteger ii = BigInteger.valueOf(i);

        for (int iter = 0; iter < epsi.length; iter++){
            result = result.multiply(epsi[iter].modPow(ii.pow(iter), P)).mod(P);
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

    public void setYi(BigInteger yi, BigInteger sigI) {
        BigInteger res = new BigInteger("1");
        res = g.modPow(yi, P).multiply(h.modPow(sigI, P)).mod(P);
        if (!res.equals(ziMust)){
            agree = false;
            log.append(preString + "Failed yi. Check is False!!!" + "\n");
            return;
        }
        log.append(preString + "Set yi = " + yi + "\n");
        agree = true;
        this.yi = yi;
    }
}
