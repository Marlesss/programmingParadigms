package expression.generic;

public class EvaluateException extends ExpressionException {
    public EvaluateException(String message) {
        super(message);
    }

    public EvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvaluateException(Throwable cause) {
        super(cause);
    }
}
