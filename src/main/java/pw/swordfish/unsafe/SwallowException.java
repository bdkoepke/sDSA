package pw.swordfish.unsafe;

public class SwallowException extends RuntimeException {
    public SwallowException(Exception e) {
        super(e);
    }

    public <T extends Exception> T checkedGetSuppressed() {
        return checkedGetSuppressed(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends Exception> T checkedGetSuppressed(int index) {
        assert (index >= 0);
        return (T)super.getSuppressed()[index];
    }
}