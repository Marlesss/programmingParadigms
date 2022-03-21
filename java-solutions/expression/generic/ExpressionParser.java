package expression.generic;

import java.util.Map;

public class ExpressionParser<T extends Number> {
    private final Additive<T> neutral;
    private final boolean exceptionCheck;
    private final Map<String, Integer> priority = Map.of(
            "min", 1,
            "max", 1,
            "+", 2,
            "-", 2,
            "*", 3,
            "/", 3
    );

    public ExpressionParser(boolean exceptionCheck, Additive<T> neutral) {
        this.neutral = neutral;
        this.exceptionCheck = exceptionCheck;
    }

    public SuperExpression<T> parse(String expression) {
//        System.err.println("GOT:      " + expression);
        CharSource source = new StringSource(expression);
        BaseParser parser = new BaseParser(source);
        SuperExpression<T> result = parseExpression(parser, 0);
//        System.err.println("RETURNED: " + result);
        parser.skipWhitespace();
        if (parser.eof()) {
            return result;
        }
        throw parser.error("Expected end-of-string");
    }


    private SuperExpression<T> parseExpression(BaseParser parser, int minPriority) {
        // System.err.println("Need to parse: " + ((StringSource) parser.source).rest());
        SuperExpression<T> first = parseValue(parser);
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
            SuperExpression<T> second = parseExpression(parser, operationPriority);
            if (operation.equals("+")) {
                first = new Add<>(first, second, exceptionCheck);
            } else if (operation.equals("-")) {
                first = new Subtract<>(first, second, exceptionCheck);
            } else if (operation.equals("*")) {
                first = new Multiply<>(first, second, exceptionCheck);
            } else if (operation.equals("/")) {
                first = new Divide<>(first, second, exceptionCheck);
            } else if (operation.equals("min")) {
                first = new Min<>(first, second);
            } else if (operation.equals("max")) {
                first = new Max<>(first, second);
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

    private SuperExpression<T> parseValue(BaseParser parser) {
        parser.skipWhitespace();
        if (parser.take('(')) {
            SuperExpression<T> result = parseExpression(parser, 0);
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
            return new Negate<>(parseValue(parser), exceptionCheck);
        }
        if (parser.take('a')) {
            parser.expect('b');
            parser.expect('s');
            parser.skipWhitespace();
            parser.expect('(');
            SuperExpression<T> value = parseValue(parser);
            parser.skipWhitespace();
            parser.expect(')');
            return new Abs<>(value, exceptionCheck);
        }
        if (parser.take('c')) {
            parser.expect("ount");
            if (parser.test('x') || parser.test('y') || parser.test('z') || parser.between('0', '9')) {
                throw parser.error("Expected for Whitespace");
            }
            return new Count<>(parseValue(parser));
        }
//        if (parser.take('l')) {
//            parser.expect('0');
//            if (parser.test('x') || parser.test('y') || parser.test('z') || parser.between('0', '9')) {
//                throw parser.error("Expected for Whitespace");
//            }
//            return new LZerores(parseValue(parser));
//        }
//        if (parser.take('t')) {
//            parser.expect('0');
//            if (parser.test('x') || parser.test('y') || parser.test('z') || parser.between('0', '9')) {
//                throw parser.error("Expected for Whitespace");
//            }
//            return new TZeroes(parseValue(parser));
//        }

        if (parser.between('0', '9')) {
            StringBuilder sb = new StringBuilder();
            getNums(parser, sb);
            return makeConst(sb);
        }

        return parseVariable(parser);
    }

    private Const<T> makeConst(StringBuilder sb) {
        return new Const<>(neutral.parseConst(sb));
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

    private SuperExpression<T> parseVariable(BaseParser parser) {
        parser.skipWhitespace();
        if (parser.take('x')) {
            return new Variable<>("x");
        }
        if (parser.take('y')) {
            return new Variable<>("y");
        }
        if (parser.take('z')) {
            return new Variable<>("z");
        }
        throw parser.error("Expected variable");
    }
}
