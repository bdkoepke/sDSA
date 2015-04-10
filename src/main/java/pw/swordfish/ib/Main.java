package pw.swordfish.ib;

import java.io.IOException;

public class Main {

    private static String toResponseString(int sDSA) {
        int a = sDSA / 1000;
        return a + " " + (sDSA - (a * 1000));
    }

    public static void main(String... args) {
        if (args.length != 2) {
            System.out.println("Usage: java sDSA challenge0 challenge1");
            return;
        }

        int challenge = (Integer.parseInt(args[0]) * 1000) + Integer.parseInt(args[1]);
        sDSA sdsa;
        {
            final String propertiesFileName = "sDSA.properties";
            try {
                sdsa = sDSA.create(propertiesFileName);
            } catch (IOException e) {
                System.out.println("Could not read: " + propertiesFileName);
                return;
            }
        }

        final int response;
        try {
            response = sdsa.getResponse(challenge);
        } catch (IOException e) {
            System.out.println("Could not update counter in properties file.");
            return;
        }

        System.out.println(toResponseString(response));
    }
}