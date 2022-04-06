"use strict";

function Const(value) {
    this.toString = function () {
        return value + "";
    }
    this.evaluate = function () {
        return value;
    }

    this.prefix = function () {
        return value + "";
    }
    this.postfix = function () {
        return value + "";
    }
}

function Variable(name) {
    this.toString = function () {
        return name;
    }
    this.evaluate = function (x, y, z) {
        return name === "x" ? x : (name === "y" ? y : (name === "z" ? z : Infinity));
    }

    this.prefix = function () {
        return name;
    }

    this.postfix = function () {
        return name;
    }
}

function UnaryOperation(expr, f, sign) {
    this.toString = function () {
        return expr.toString() + " " + sign;
    }
    this.evaluate = function (x, y, z) {
        return f(expr.evaluate(x, y, z));
    }
    this.prefix = function () {
        return "(" + sign + " " + expr.prefix() + ")";
    }

    this.postfix = function () {
        return "(" + expr.postfix() + " " + sign + ")";
    }

}

function Negate(expr) {
    return new UnaryOperation(expr, x => -x, "negate");
}

// :NOTE: no prototype use
// :NOTE: a class for each arity is insufficient

function BinaryOperation(expr1, expr2, f, sign) {
    this.toString = function () {
        return expr1.toString() + " " + expr2.toString() + " " + sign;
    }
    this.evaluate = function (x, y, z) {
        return f(expr1.evaluate(x, y, z), expr2.evaluate(x, y, z));
    }
    this.prefix = function () {
        return "(" + sign + " " + expr1.prefix() + " " + expr2.prefix() + ")";
    }
    this.postfix = function () {
        return "(" + expr1.postfix() + " " + expr2.postfix() + " " + sign + ")";
    }
}

function Add(expr1, expr2) {
    return new BinaryOperation(expr1, expr2, (a, b) => a + b, "+");
}

function Subtract(expr1, expr2) {
    return new BinaryOperation(expr1, expr2, (a, b) => a - b, "-");
}

function Multiply(expr1, expr2) {
    return new BinaryOperation(expr1, expr2, (a, b) => a * b, "*");
}

function Divide(expr1, expr2) {
    return new BinaryOperation(expr1, expr2, (a, b) => a / b, "/");
}

function Cosh(expr1) {
    return new UnaryOperation(expr1, Math.cosh, "cosh");
}

function Sinh(expr1) {
    return new UnaryOperation(expr1, Math.sinh, "sinh");
}


function split(str) {
    let result = []
    let start = false;
    for (let i = 0; i < str.length; i++) {
        if (str[i] === ' ') {
            start = false;
        } else {
            if (start) {
                result[result.length - 1] += str[i];
            } else {
                start = true;
                result.push(str[i]);
            }
        }
    }
    return result;
}

function parse(str) {
    let stack = [];
    // for (let val of str.split(/ +/)) {  // is str.split(...) available???
    for (let val of split(str)) {
        if (val === '') {
            continue;
        }
        if (!isNaN(parseFloat(val))) {
            stack.push(new Const(parseFloat(val)));
        } else if (val === "x" || val === "y" || val === "z") {
            stack.push(new Variable(val));
        } else if (val === "+" || val === "-" || val === "*" || val === "/") {
            let expr2 = stack.pop();
            let expr1 = stack.pop();
            if (val === "+") {
                stack.push(new Add(expr1, expr2));
            } else if (val === "-") {
                stack.push(new Subtract(expr1, expr2));
            } else if (val === "*") {
                stack.push(new Multiply(expr1, expr2));
            } else if (val === "/") {
                stack.push(new Divide(expr1, expr2));
            }
        } else if (val === "negate") {
            stack.push(new Negate(stack.pop()));
        } else if (val === "cosh") {
            stack.push(new Cosh(stack.pop()));
        } else if (val === "sinh") {
            stack.push(new Sinh(stack.pop()));
        }
    }
    return stack.pop();
}

function skipWhitespace(str, i) {
    for (; i < str.length && str[i] === ' '; i++) ;
    return i;
}

function expect(str, i, sign) {
    if (!(i < str.length && str[i] === sign)) {
        throw Error("Expect " + sign + " got " + str[i]);
    }
}

function parseOp(str, i) {
    if (i >= str.length) {
        throw Error("Unexpected EOF");
    }
    if (str[i] === '+' || str[i] === '-' || str[i] === '*' || str[i] === '/') {
        if (i + 1 < str.length && isNaN(parseFloat(str[i + 1])))
            return [str[i], i + 1];
    } else if (!('a' <= str[i] && str[i] <= 'z')) {
        throw Error("Expect operation");
    } else {
        let j;
        for (j = i; j < str.length && 'a' <= str[j] && str[j] <= 'z'; j++) ;
        if (j < str.length && isNaN(parseFloat(str[i + 1])))
            return [str.substring(i, j), j];
    }
    throw Error("Expect operation");
}

function parseElem(str, i, parseBracket) {
    i = skipWhitespace(str, i);
    if (i >= str.length) {
        throw Error("Unexpected EOF");
    }
    let res;
    if (str[i] === '(') {
        return parseBracket(str, i);
    }
    if (str[i] === 'x' || str[i] === 'y' || str[i] === 'z') {
        return [new Variable(str[i]), i + 1]
    }
    let j;
    for (j = i; j < str.length && (j === i && (str[j] === '-' || str[j] === '+') || !isNaN(parseFloat(str[j]))); j++) ;
    let num = parseFloat(str.substring(i, j));
    if (!isNaN(num)) {
        return [new Const(num), j];
    }
    throw Error("Expect bracket, number or variable, got " + str[i]);
}

function makeUnaryOperation(op, expr) {
    if (op === "negate")
        return new Negate(expr);
    if (op === "cosh")
        return new Cosh(expr);
    if (op === "sinh")
        return new Sinh(expr);
    throw Error("Unknown unary operation");
}

function makeBinaryOperation(op, first, second) {
    if (op === "+")
        return new Add(first, second);
    if (op === "-")
        return new Subtract(first, second);
    if (op === "*")
        return new Multiply(first, second);
    if (op === "/")
        return new Divide(first, second);
    throw Error("Unknown binary operation");

}

function parsePrefix(str) {
    // println("parsePrefix: " + str);
    let res = parseElem(str, 0, parseBracket);
    let i = res[1];
    i = skipWhitespace(str, i);
    if (i < str.length) {
        throw Error("Expected for EOF");
    }
    return res[0];

    function parseBracket(str, i) {
        i = skipWhitespace(str, i);
        expect(str, i, "(");
        i++;
        i = skipWhitespace(str, i);
        let res = parseOp(str, i);
        let op = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        res = parseElem(str, i, parseBracket);
        let first = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        if (str[i] === ')') {
            i++;
            res = makeUnaryOperation(op, first);
            return [res, i];
        }
        res = parseElem(str, i, parseBracket);
        let second = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        expect(str, i, ")");
        i++;
        res = makeBinaryOperation(op, first, second);
        return [res, i];
    }
}

function elemStarts(str, i) {
    return i < str.length && (str[i] === "(" || str[i] === "x" || str[i] === "y" || str[i] === "z" || str[i] === "-" || !isNaN(parseFloat(str[i])))
}

function parsePostfix(str) {
    // println("parsePrefix: " + str);
    let res = parseElem(str, 0, parseBracket);
    let i = res[1];
    i = skipWhitespace(str, i);
    if (i < str.length) {
        throw Error("Expected for EOF");
    }
    return res[0];

    function parseBracket(str, i) {
        i = skipWhitespace(str, i);
        expect(str, i, "(");
        i++;
        i = skipWhitespace(str, i);
        let res = parseElem(str, i, parseBracket);
        let first = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        if (!elemStarts(str, i)) {
            res = parseOp(str, i);
            let op = res[0];
            i = res[1];
            i = skipWhitespace(str, i);
            expect(str, i, ")");
            i++;
            res = makeUnaryOperation(op, first);
            return [res, i];
        }
        res = parseElem(str, i, parseBracket);
        let second = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        res = parseOp(str, i);
        let op = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        expect(str, i, ")");
        i++;
        res = makeBinaryOperation(op, first, second);
        return [res, i];
    }
}