package pw.swordfish.ib;

import org.ietf.rfc6287.OCRA;
import pw.swordfish.diag.Contract;

import java.util.function.Supplier;

public class sDSA {
    private final String ocraSuite;
    private final String key;
    private final String password;
    private final Supplier<Integer> counter;

    public sDSA(String ocraSuite, String key, String password, Supplier<Integer> counter) {
        this.ocraSuite = Contract.ensuresNotNull(ocraSuite, "ocraSuite");
        this.key = Contract.ensuresNotNull(key, "key");
        this.password = Contract.ensuresNotNull(password, "password");
        this.counter = Contract.ensuresNotNull(counter, "counter");
    }


    public int getResponse(int challenge) {
        Contract.ensures(i -> i >= 0, challenge, "challenge");
        String counterHex = Integer.toHexString(counter.get());
        String challengeHex = Integer.toHexString(challenge);
        return Integer.parseInt(OCRA.generateOCRA(ocraSuite, key, counterHex, challengeHex, password, null, null));
    }
}
