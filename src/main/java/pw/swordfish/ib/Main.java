package pw.swordfish.ib;

import pw.swordfish.diag.Contract;
import pw.swordfish.unsafe.SwallowException;

import java.io.*;
import java.util.Properties;

public class Main {

    private static String toResponseString(int sDSA) {
        int a = sDSA / 1000;
        return a + " " + (sDSA - (a * 1000));
    }

    private static sDSA sDSA(String propertiesFileName) throws IOException {
        Contract.ensuresNotNull(propertiesFileName, "propertiesFileName");
        final Properties p = new Properties();
        {
            InputStream i = new FileInputStream(propertiesFileName);
            p.load(i);
            i.close();
        }
        return new sDSA(p.getProperty("ocraSuite"), p.getProperty("key"), p.getProperty("password"), () -> {
            int counter = Integer.parseInt(p.getProperty("counter"));
            p.setProperty("counter", String.valueOf(counter + 1));
            try {
                OutputStream o = new FileOutputStream(propertiesFileName);
                p.store(o, null);
                o.close();
            } catch (IOException e) {
                throw new SwallowException(e);
            }
            return counter;
        });
    }

    private static int getResponse(sDSA self, int challenge) throws IOException {
        try {
            return self.getResponse(challenge);
        } catch (SwallowException e) {
            throw e.<IOException>checkedGetSuppressed();
        }
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
                sdsa = sDSA(propertiesFileName);
            } catch (IOException e) {
                System.out.println("Could not read: " + propertiesFileName);
                return;
            }
        }

        final int response;
        try {
            response = getResponse(sdsa, challenge);
        } catch (IOException e) {
            System.out.println("Could not update counter in properties file.");
            return;
        }

        System.out.println(toResponseString(response));
    }
}