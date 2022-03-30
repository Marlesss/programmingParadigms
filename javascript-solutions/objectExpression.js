"use strict";

function Const(value) {
    this.toString = function () {
        return value + "";
    }
    this.evaluate = function () {
        return value;
    }
}

function Variable(name) {
    this.toString = function () {
        return name;
    }
    this.evaluate = function (x, y, z) {
        return name === "x" ? x : (name === "y" ? y : (name === "z" ? z : Infinity));
    }
}

function UnaryOperation(expr, f, sign) {
    this.toString = function () {
        return expr.toString() + " " + sign;
    }
    this.evaluate = function (x, y, z) {
        return f(expr.evaluate(x, y, z));
    }
}

function Negate(expr) {
    return new UnaryOperation(expr, x => -x, "negate");
}

function BinaryOperation(expr1, expr2, f, sign) {
    this.toString = function () {
        return expr1.toString() + " " + expr2.toString() + " " + sign;
    }
    this.evaluate = function (x, y, z) {
        return f(expr1.evaluate(x, y, z), expr2.evaluate(x, y, z));
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

