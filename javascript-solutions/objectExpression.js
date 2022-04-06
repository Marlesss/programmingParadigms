"use strict";


function UnlimitedOperation(f, sign, ...exprs) {
    this.f = f;
    this.sign = sign;
    this.exprs = exprs
}

UnlimitedOperation.prototype.toString = function () {
    let res = "";
    for (let i = 0; i < this.exprs.length; i++) {
        res += this.exprs[i].toString() + " ";
    }
    return res + this.sign;
}
UnlimitedOperation.prototype.evaluate = function (x, y, z) {
    let resArgs = []
    for (let i = 0; i < this.exprs.length; i++) {
        resArgs.push(this.exprs[i].evaluate(x, y, z));
    }
    return this.f(...resArgs);
}
UnlimitedOperation.prototype.prefix = function () {
    let res = this.sign;
    for (let i = 0; i < this.exprs.length; i++) {
        res += " " + this.exprs[i].prefix();
    }
    return "(" + res + ")";
}
UnlimitedOperation.prototype.postfix = function () {
    let res = "";
    for (let i = 0; i < this.exprs.length; i++) {
        res += this.exprs[i].postfix() + " ";
    }
    return "(" + res + sign + ")";
}

function Const(value) {
    this.value = value;
}

Const.prototype.evaluate = function () {
    return this.value
};
Const.prototype.toString = function () {
    return this.value + ""
};
Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;

function Variable(name) {
    this.name = name;
}

Variable.prototype.evaluate = function (x, y, z) {
    return this.name === "x" ? x : (this.name === "y" ? y : (this.name === "z" ? z : Infinity))
};
Variable.prototype.toString = function () {
    return this.name;
}
Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;


function Negate(...exprs) {
    UnlimitedOperation.call(this, (x) => -x, "negate", ...exprs);
}

Negate.prototype = Object.create(UnlimitedOperation.prototype);

function Add(...exprs) {
    UnlimitedOperation.call(this, (a, b) => a + b, "+", ...exprs);
}

Add.prototype = Object.create(UnlimitedOperation.prototype);

function Subtract(...exprs) {
    UnlimitedOperation.call(this, (a, b) => a - b, "-", ...exprs);
}

Subtract.prototype = Object.create(UnlimitedOperation.prototype);

function Multiply(...exprs) {
    UnlimitedOperation.call(this, (a, b) => a * b, "*", ...exprs);
}

Multiply.prototype = Object.create(UnlimitedOperation.prototype);

function Divide(...exprs) {
    UnlimitedOperation.call(this, (a, b) => a / b, "/", ...exprs);
}

Divide.prototype = Object.create(UnlimitedOperation.prototype);

function Cosh(...exprs) {
    UnlimitedOperation.call(this, Math.cosh, "cosh", ...exprs);
}

Cosh.prototype = Object.create(UnlimitedOperation.prototype);

function Sinh(...exprs) {
    UnlimitedOperation.call(this, Math.sinh, "sinh", ...exprs);
}

Sinh.prototype = Object.create(UnlimitedOperation.prototype);

function Mean(...exprs) {
    UnlimitedOperation.call(this,
        (...args) => {
            let sum = 0;
            for (let i = 0; i < args.length; i++) {
                sum += args[i];
            }
            return sum / args.length;
        }, "mean", ...exprs);
}

Mean.prototype = Object.create(UnlimitedOperation.prototype);

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