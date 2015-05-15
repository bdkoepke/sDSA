package pw.swordfish.unsafe;

import pw.swordfish.diag.Contract;

public class SwallowException extends RuntimeException {
    public SwallowException(Exception e) {
        super(e);
    }

    public <T extends Exception> T checkedGetSuppressed() {
        return checkedGetSuppressed(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends Exception> T checkedGetSuppressed(int index) {
        return (T)super.getSuppressed()[Contract.ensures(i -> i >= 0, index, "index")];
    }
}