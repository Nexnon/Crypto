package crypto.models;

public enum OperationCode {
    FILL_UP(1),
    WITHDRAWAL(2),
    EXCHANGE(3);
    private final int code;
    OperationCode(final int code){
        this.code = code;
    }
}
