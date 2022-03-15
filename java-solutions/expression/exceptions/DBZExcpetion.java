package expression.exceptions;

public class DBZExcpetion extends EvaluateException {

    public DBZExcpetion(String message) {
        super(message);
    }

    public DBZExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public DBZExcpetion(Throwable cause) {
        super(cause);
    }
}
