package SHAMIR;

import java.math.BigInteger;

public class Client {
    private int i;
    private BigInteger yi;
    private StringBuffer log = new StringBuffer();
    private String preString;
    Client(){
        i = 0;
        yi = BigInteger.valueOf(0);
    }

    Client(int i, BigInteger yi){
        preString = "Client " + i + ":";
        log.append(preString + "Set yi = " + yi + "\n");
        this.i = i;
        this.yi = yi;
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

    public void setYi(BigInteger yi) {
        this.yi = yi;
    }
}
