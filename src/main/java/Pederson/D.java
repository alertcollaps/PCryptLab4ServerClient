package Pederson;

import java.math.BigInteger;
import java.util.Scanner;

public class D {
    static String preString = "[PEDERSON]:";
    static StringBuffer log = new StringBuffer();
    public static BigInteger SLAY(BigInteger[] yi, int[] xi){
        BigInteger[][] matrix = new BigInteger[yi.length][yi.length + 1];
        for (int indY = 0; indY < yi.length; indY++) {
            for (int i = 0; i < yi.length - 1; i++) {
                matrix[indY][i] = BigInteger.valueOf(xi[indY]).pow(i + 1);
            }
            matrix[indY][yi.length - 1] = BigInteger.valueOf(1);
            matrix[indY][yi.length] = yi[indY];
        }

        //Gauss
        for (int indY = 0; indY < matrix.length - 1; indY++) { //Triple sec
            BigInteger[] first1Stay = matrix[indY];
            BigInteger first1Value = first1Stay[indY].abs();
            for (int i = indY; i < matrix.length - 1; i++) {
                BigInteger[] first1 = new BigInteger[first1Stay.length];
                System.arraycopy(first1Stay, 0, first1, 0, first1.length);
                BigInteger[] first2 = matrix[i + 1];
                BigInteger first2Value = first2[indY].abs();

                BigInteger lcm = lcm(first1Value, first2Value);
                multiplyArray(first1, lcm.divide(first1Value));
                multiplyArray(first2, lcm.divide(first2Value));

                subtractArrays(first1, first2, indY);
            }
        }

        BigInteger a0 = matrix[matrix.length - 1][matrix[0].length - 1].divide(matrix[matrix.length - 1][matrix[0].length - 2]);
        return a0;
    }

    public static BigInteger Lagrange(BigInteger[] yi, int[] xi){
        BigInteger result = new BigInteger("1");
        BigInteger resultSum = new BigInteger("0");

        for (int j = 0; j < yi.length; j++){
            result = new BigInteger("1");
            BigInteger xj = BigInteger.valueOf(xi[j]);
            BigInteger numerator = BigInteger.valueOf(1);
            BigInteger deNumerator = BigInteger.valueOf(1);

            for (int k = 0; k < yi.length; k++){
                if (j == k){
                    continue;
                }
                BigInteger xk = BigInteger.valueOf(xi[k]);
                numerator = numerator.multiply(xk);
                deNumerator = deNumerator.multiply(xk.subtract(xj));
            }
            result = result.multiply(yi[j]).multiply(numerator).divide(deNumerator);
            resultSum = resultSum.add(result);
        }
        return resultSum;
    }

    public static void multiplyArray(BigInteger[] array, BigInteger count){
        for (int i = 0; i < array.length; i++){
            array[i] = array[i].multiply(count);
        }
    }
    public static void subtractArrays(BigInteger[] array1, BigInteger[] array2, int indY){
        boolean equal = array1[indY].equals(array2[indY]);
        for (int i = 0; i < array2.length; i++){
            array2[i] = equal ? array2[i].subtract(array1[i]) : array2[i].add(array1[i]);
        }
    }
    public static BigInteger lcm(BigInteger s, BigInteger s1){
        s = s.abs();
        s1 = s1.abs();
        // calculate multiplication of two bigintegers
        BigInteger mul = s.multiply(s1);

        // calculate gcd of two bigintegers
        BigInteger gcd = s.gcd(s1);

        // calculate lcm using formula: lcm * gcd = x * y
        return mul.divide(gcd);
    }

    public static void main(String[] args) {
        int t = 4;
        int n = 5;
        Client[] clients = new Client[n];
        Pederson_scheme pederson_scheme = new Pederson_scheme(t);
        pederson_scheme.chooseAi();
        log.append(preString + "Shamir secret key:" + pederson_scheme.getK() + "\n");
        BigInteger P = pederson_scheme.getP();
        log.append(preString + "Shamir P:" + pederson_scheme.getP() + "\n");
        int[] xi = pederson_scheme.initialiseClients(n);
        for (int i = 0; i < n; i++){ //Заполнение клиентов
            int xInd = xi[i];
            clients[i] = new Client(xInd, pederson_scheme.getP(), pederson_scheme.getCheckNumbers(), pederson_scheme.getG(), pederson_scheme.getH());
            clients[i].setYi(pederson_scheme.getYi(xInd), pederson_scheme.getSiGi(xInd));
        }

        //
        BigInteger[] yi = new BigInteger[t];
        for (int i = 0; i < t; i++){
            yi[i] = clients[i].getYi();
        }
        //Evaluate Key
        int num = 0;
        BigInteger K = new BigInteger("0");
        Scanner in = new Scanner(System.in);
        while (num != 3){
            System.out.print(preString +  "\nВыберите метод:\n1. GAUSS\n2. Lagrange\n3. Exit\n---->");
            num = in.nextInt();
            try {
                switch (num){
                    case 1:
                        K = SLAY(yi, xi).mod(P);
                        log.append(preString + "Choose Gauss" + "\n");
                        break;
                    case 2:
                        K = Lagrange(yi, xi).mod(P);
                        log.append(preString + "Choose Lagrange" + "\n");
                        break;
                    case 3:
                        break;
                    default:
                        System.out.print(preString +  "Error! Choose valid number" + "\n");
                        continue;
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }

            if (num == 3){
                break;
            }

            log.append(preString + "Evaluated shamir secret key:" + K + "\n");
            log.append(preString + "Равенство K == shamir.K:" + K.equals(pederson_scheme.getK()) + "\n");

            //Вывод логов
            System.out.println(log);
            for (int i = 0; i < n; i++){
                System.out.println(clients[i].getLog()); ;
            }
        }
        in.close();

    }
}
