package expression.parser;

import expression.*;
import expression.exceptions.ParseException;

import java.math.BigInteger;
import java.util.Map;

public class ExpressionParser implements TripleParser {
    private final String mode;
    private final Map<String, Integer> priority = Map.of(
            "min", 1,
            "max", 1,
            "+", 2,
            "-", 2,
            "*", 3,
            "/", 3
    );

    public ExpressionParser() {
        this.mode = "u";
    }

    public ExpressionParser(String mode) {
        this.mode = mode;
    }

    public SuperExpression parse(String expression) {
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
        while (true) {
            parser.skipWhitespace();
            if (parser.eof() || parser.test(')')) {
                return first;
            }
            int operationPriority = getNextOperationPriority(parser);
            if (operationPriority <= minPriority) {
                return first;
            }
            String operation = parseBinaryOperation(parser);
//            System.err.println("parsing: first = " + first + ", operation = " + operation);
            SuperExpression second = parseExpression(parser, operationPriority);
            if (operation.equals("+")) {
                first = new Add(first, second);
            } else if (operation.equals("-")) {
                first = new Subtract(first, second);
            } else if (operation.equals("*")) {
                first = new Multiply(first, second);
            } else if (operation.equals("/")) {
                first = new Divide(first, second);
            } else if (operation.equals("min")) {
                first = new Min(first, second);
            } else if (operation.equals("max")) {
                first = new Max(first, second);
            } else {
                throw parser.error("Unknown binary operation: " + operation);
            }
        }
    }

    private String parseBinaryOperation(BaseParser parser) {
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

    private int getNextOperationPriority(BaseParser parser) {
        if (parser.test('+') || parser.test('-')) {
            return priority.get("+");
        }
        if (parser.test('*') || parser.test('/')) {
            return priority.get("*");
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
                return makeConst(sb);
            }
            return new Negate(parseValue(parser));
        }
        if (parser.between('0', '9')) {
            StringBuilder sb = new StringBuilder();
            getNums(parser, sb);
            return makeConst(sb);
        }

        return parseVariable(parser);
    }

    private Const makeConst(StringBuilder sb) {
        switch (mode) {
            case "i":
            case "u":
                return new Const(Integer.parseInt(sb.toString()));
            case "d":
                return new Const(Double.parseDouble(sb.toString()));
            case "bi":
                return new Const(BigInteger.valueOf(Long.parseLong(sb.toString())));
        }
        throw new ParseException("Unknown mode");
    }

    private void getNums(BaseParser parser, StringBuilder sb) {
        while (parser.between('0', '9')) {
            sb.append(parser.take());
        }
        if (parser.take('.')) {
            sb.append('.');
            while (parser.between('0', '9')) {
                sb.append(parser.take());
            }
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
