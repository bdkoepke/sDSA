package pw.swordfish.ib;

import nl.surfnet.ocra.OCRA;
import pw.swordfish.unsafe.SwallowException;

import java.io.*;
import java.util.Properties;
import java.util.function.Supplier;

public class sDSA {
    private final String ocraSuite;
    private final String key;
    private final String password;
    private final Supplier<Integer> counter;

    private sDSA(String ocraSuite, String key, String password, Supplier<Integer> counter) {
        this.ocraSuite = ocraSuite;
        this.key = key;
        this.password = password;
        this.counter = counter;
    }

    public static sDSA create(String propertiesFileName) throws IOException {
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

    public static sDSA sDSA(String ocraSuite, String key, String password, final int counter) {
        // no true closures for java ;(
        return new sDSA(ocraSuite, key, password, new Supplier<Integer>() {
            private int mutable_counter = counter;
            @Override
            public Integer get() {
                return mutable_counter++;
            }
        });
    }

    public int getResponse(int challenge) throws IOException {
        if (challenge < 0)
            throw new IllegalArgumentException("challenge");
        String counterHex;
        try {
            counterHex = Integer.toHexString(counter.get());
        } catch (SwallowException s) {
            throw s.<IOException>checkedGetSuppressed();
        }
        String challengeHex = Integer.toHexString(challenge);
        return Integer.parseInt(OCRA.generateOCRA(ocraSuite, key, counterHex, challengeHex, password, null, null));
    }
}
