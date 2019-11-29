# Document

## Syntax

* The program is a set of instructions.
* Instruction can be everything included in a block (i.e. everything in `{ ... }`), an if statement or a while statement.
* As said a block is a set of instructions in curly brackets.
* A line is a sequence terminated by a `;`.
* A sequence is a set of expressions separated by `,`.
* An expression is everything involving assignments, operation or comparisons between values.

### Operations

* Boolean operation can be performed using `and`, `or` keywords.
* Boolean comparisons needs one of `[<|<=|>|>=|==|!=]` operators.
* Arithmetic operations are `[+|-|*|/|%|^]` where `%` is modulo and `^` is power.
* Every expression can contain brackets to apply precedence.
* Unary operators are `-` (interpreted as `0 - <expression>`) and `not` for boolean negation.

### Variables

* A variable is a word that it's not a keyword and matches `[a-zA-Z]\\S*`.
* To assign a scalar variable you use the `=` operator: `variable = 9;`.
* To access a scalar variable you add a `$` to variable name: `$variable + 9;` .
* To create an object you use the `:= (<field> => <value>)` operator: `object := (field => 7);`.
* To nest object you use the `:=>` operator: `object := (nested :=> (field => 7));`.
* You can also assign to your base name object a value with the `=>` operator: `object := (=> 7);`. By doing this `$object;` will evaluate as 7.
* To access object you can use the `.` operator and the `$` operator: `$object.field;`.
* You can also add and modify object's field with the `.` operator: `object.field = 9;`.

### Instructions

* When assigning a variable you can also use an `if` expression: `var = if $condition { 7;} else { 8;};`.
* When assigning a variable you can also use an `while` expression: `var = while $condition { 7;};`. (this is very useless)
* If and while also work as instructions
