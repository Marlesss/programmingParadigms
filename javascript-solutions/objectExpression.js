"use strict";

function UnlimitedOperation(f, sign, ...exprs) {
    // this.f = f;
    // this.sign = sign;
    // this.exprs = exprs;
    this.toString = function () {
        let res = "";
        for (let i = 0; i < args.length; i++) {
            res += exprs[i].toString() + " ";
        }
        return res + sign;
    }
    this.evaluate = function (x, y, z) {
        // println(exprs);
        let resArgs = []
        for (let i = 0; i < exprs.length; i++) {
            resArgs.push(exprs[i].evaluate(x, y, z));
        }
        return f(...resArgs);
    }
    this.prefix = function () {
        let res = sign;
        for (let i = 0; i < exprs.length; i++) {
            res += " " + exprs[i].prefix();
        }
        return "(" + res + ")";
    }
    this.postfix = function () {
        let res = "";
        for (let i = 0; i < exprs.length; i++) {
            res += exprs[i].postfix() + " ";
        }
        return "(" + res + sign + ")";
    }
}

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

function Mean(...args) {
    return new UnlimitedOperation(
        (...args) => {
            let sum = 0;
            for (let i = 0; i < args.length; i++) {
                sum += args[i];
            }
            return sum / args.length;
        }, "mean", ...args
    );
}

function Var(...args) {
    return new UnlimitedOperation(
        (...args) => {
            let sum = 0;
            let sumkv = 0;
            for (let i = 0; i < args.length; i++) {
                sum += args[i];
                sumkv += args[i] * args[i];
            }
            return sumkv / args.length - (sum / args.length) * (sum / args.length);
        }, "var", ...args
    );
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
            stack.push(makeBinaryOperation(val, expr1, expr2));
        } else {
            stack.push(makeUnaryOperation(val, stack.pop()));
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

function parseElems(str, i, parseBracket) {
    let exprs = [];
    let res;
    i = skipWhitespace(str, i);
    do {
        res = parseElem(str, i, parseBracket);
        exprs.push(res[0]);
        i = res[1];
        i = skipWhitespace(str, i);
    } while (elemStarts(str, i));
    return [exprs, i];
}

function makeUnlimitedOperation(op, ...exprs) {
    if (op === "mean") {
        return new Mean(...exprs);
    }
    if (op === "var") {
        return new Var(...exprs);
    }
    throw Error("Unknown unlimited operation");
}

function makeOperation(op, ...exprs) {
    if (op === "+" || op === "-" || op === "*" || op === "/") {
        return makeBinaryOperation(op, ...exprs);
    }
    if (op === "negate" || op === "cosh" || op === "sinh") {
        return makeUnaryOperation(op, ...exprs);
    }
    return makeUnlimitedOperation(op, ...exprs);
}

function makeUnaryOperation(op, ...exprs) {
    if (exprs.length !== 1) {
        throw Error("Wrong count of arguments");
    }
    if (op === "negate")
        return new Negate(...exprs);
    if (op === "cosh")
        return new Cosh(...exprs);
    if (op === "sinh")
        return new Sinh(...exprs);
    throw Error("Unknown unary operation");
}

function makeBinaryOperation(op, ...exprs) {
    if (exprs.length !== 2) {
        throw Error("Wrong count of arguments");
    }
    if (op === "+")
        return new Add(...exprs);
    if (op === "-")
        return new Subtract(...exprs);
    if (op === "*")
        return new Multiply(...exprs);
    if (op === "/")
        return new Divide(...exprs);
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
        res = parseElems(str, i, parseBracket);
        let exprs = res[0];
        i = res[1];
        expect(str, i, ")");
        i++;
        res = makeOperation(op, ...exprs);
        return [res, i];
    }
}

function elemStarts(str, i) {
    return i < str.length && (str[i] === "(" || str[i] === "x" || str[i] === "y" || str[i] === "z" || (str[i] === "-" && i + 1 < str.length && !isNaN(parseFloat(str[i + 1]))) || !isNaN(parseFloat(str[i])))
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
        let res = parseElems(str, i, parseBracket);
        let exprs = res[0];
        i = res[1];
        res = parseOp(str, i);
        let op = res[0];
        i = res[1];
        i = skipWhitespace(str, i);
        expect(str, i, ")");
        i++;
        res = makeOperation(op, ...exprs);
        return [res, i];
    }
}