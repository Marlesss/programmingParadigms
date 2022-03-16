package expression.exceptions;

import expression.SuperExpression;
import expression.Const;
import expression.TripleExpression;
import expression.Variable;

import java.util.Map;

public class ExpressionParser implements TripleParser {
    private final Map<String, Integer> priority = Map.of(
            "min", 1,
            "max", 1,
            "+", 2,
            "-", 2,
            "*", 3,
            "/", 3
    );

    public ExpressionParser() {

    }

    public TripleExpression parse(String expression) {
//        System.err.println("GOT:      " + expression);
        CharSource source = new StringSource(expression);
        BaseParser parser = new BaseParser(source);
        SuperExpression result = parseExpression(parser, 0);
//        System.err.println("RETURNED: " + result);
        parser.skipWhitespace();
        if (parser.eof()) {
            return result;
        }
        throw parser.error("Expected end-of-string");
    }

    private SuperExpression parseExpression(BaseParser parser, int minPriority) {
        // System.err.println("Need to parse: " + ((StringSource) parser.source).rest());
        SuperExpression first = parseValue(parser);
        boolean wasWhitespace;
        while (true) {
            wasWhitespace = parser.skipWhitespace();
            if (parser.eof() || parser.test(')')) {
                return first;
            }
            int operationPriority = getNextOperationPriority(parser, !(first instanceof Const || first instanceof Variable) || wasWhitespace);
            if (operationPriority <= minPriority) {
                return first;
            }
            String operation = parseBinaryOperation(parser, !(first instanceof Const || first instanceof Variable) || wasWhitespace);
            wasWhitespace = parser.skipWhitespace();
//            System.err.println("parsing: first = " + first + ", operation = " + operation);
            SuperExpression second = parseExpression(parser, operationPriority);
            if (operation.equals("+")) {
                first = new CheckedAdd(first, second);
            } else if (operation.equals("-")) {
                first = new CheckedSubtract(first, second);
            } else if (operation.equals("*")) {
                first = new CheckedMultiply(first, second);
            } else if (operation.equals("/")) {
                first = new CheckedDivide(first, second);
            } else if (operation.equals("min")) {
                first = new CheckedMin(first, second);
            } else if (operation.equals("max")) {
                first = new CheckedMax(first, second);
            } else {
                throw parser.error("Unknown binary operation: " + operation);
            }
            if (operation.length() > 1 && second instanceof Variable && !wasWhitespace) {
                throw parser.error("Expected for Whitespace");
            }
        }
    }

    private String parseBinaryOperation(BaseParser parser, boolean wasWhitespace) {
        parser.skipWhitespace();
        if (parser.take('+')) {
            return "+";
        }
        if (parser.take('-')) {
            return "-";
        }
        if (parser.take('*')) {
            return "*";
        }
        if (parser.take('/')) {
            return "/";
        }
        if (!wasWhitespace) {
            throw parser.error("Expected for Whitespace");
        }
        if (parser.take('m')) {
            if (parser.take('a')) {
                parser.expect('x');
                return "max";
            }
            if (parser.take('i')) {
                parser.expect('n');
                return "min";
            }
        }
        throw parser.error("Expected binary operation, took: " + parser.take());
    }

    private int getNextOperationPriority(BaseParser parser, boolean wasWhitespace) {
        if (parser.test('+') || parser.test('-')) {
            return priority.get("+");
        }
        if (parser.test('*') || parser.test('/')) {
            return priority.get("*");
        }
        if (!wasWhitespace) {
            throw parser.error("Expected for Whitespace");
        }
        if (parser.test('m')) {
            return priority.get("min");
        }
        return 0;
    }

    private SuperExpression parseValue(BaseParser parser) {
        parser.skipWhitespace();
        if (parser.take('(')) {
            SuperExpression result = parseExpression(parser, 0);
            parser.skipWhitespace();
            parser.expect(')');
            return result;
        }
        if (parser.take('-')) {
            parser.skipWhitespace();
            if (parser.between('0', '9')) {
                StringBuilder sb = new StringBuilder().append('-');
                getNums(parser, sb);
                return new Const(Integer.parseInt(sb.toString()));
            }
            return new CheckedNegate(parseValue(parser));
        }
        if (parser.take('a')) {
            parser.expect('b');
            parser.expect('s');
            parser.skipWhitespace();
            parser.expect('(');
            SuperExpression value = parseValue(parser);
            parser.skipWhitespace();
            parser.expect(')');
            return new CheckedAbs(value);
        }
        if (parser.take('l')) {
            parser.expect('0');
            if (parser.test('x') || parser.test('y') || parser.test('z') || parser.between('0', '9')) {
                throw parser.error("Expected for Whitespace");
            }
            return new LZerores(parseValue(parser));
        }
        if (parser.take('t')) {
            parser.expect('0');
            if (parser.test('x') || parser.test('y') || parser.test('z') || parser.between('0', '9')) {
                throw parser.error("Expected for Whitespace");
            }
            return new TZeroes(parseValue(parser));
        }

        if (parser.between('0', '9')) {
            StringBuilder sb = new StringBuilder();
            getNums(parser, sb);
            return new Const(Integer.parseInt(sb.toString()));
        }

        return parseVariable(parser);
    }

    private void getNums(BaseParser parser, StringBuilder sb) {
        while (parser.between('0', '9')) {
            sb.append(parser.take());
        }
    }

    private SuperExpression parseVariable(BaseParser parser) {
        parser.skipWhitespace();
        if (parser.take('x')) {
            return new Variable("x");
        }
        if (parser.take('y')) {
            return new Variable("y");
        }
        if (parser.take('z')) {
            return new Variable("z");
        }
        throw parser.error("Expected variable");
    }
}
