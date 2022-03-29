"use strict";

const cnst = x => () => x;
const variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : (name === "z" ? z : Infinity));
// const variable = name => (x, y, z) => {
//     let arr = [];
//     arr["x"] = x;
//     arr["y"] = y;
//     arr["z"] = z;
//     return arr[name];
// }
const binaryOperation = f => (expr1, expr2) => (x, y, z) => f(expr1(x, y, z), expr2(x, y, z));
const unaryOperation = f => expr1 => (x, y, z) => f(expr1(x, y, z));
const add = binaryOperation((a, b) => a + b);
const subtract = binaryOperation((a, b) => a - b);
const multiply = binaryOperation((a, b) => a * b);
const divide = binaryOperation((a, b) => a / b);
const negate = unaryOperation(a => -a);
const pi = cnst(Math.PI);
const e = cnst(Math.E);
const sinh = unaryOperation(Math.sinh)
const cosh = unaryOperation(Math.cosh)

let myexpr = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            variable("x"),
            cnst(2)
        )
    ),
    cnst(1)
)

for (let x = 0; x <= 10; x++) {
    println(x + " " + myexpr(x))
}