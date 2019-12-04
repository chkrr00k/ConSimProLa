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
* You can use `->` and `<-` to push and pop the first element from an array. Note that this is the only way to make an array grow as they are fixed in size.

### Variables

* A variable is a word that it's not a keyword and matches `[a-zA-Z]\\S*`.
* To assign a scalar variable you use the `=` operator: `variable = 9;`.
* To access a scalar variable you add a `$` to variable name: `$variable + 9;` .
* To create an object you use the `:=` operator. The syntax is similar to the json one: `object := {field : 5, inner:{veryinner:5}, array:[3,4,5]};`. You can also use numeric values with that operator.
* To access object you can use the `.` operator and the `$` operator: `$object.field;`.
* You can also add and modify object's field with the `.` operator: `object.field = 9;`. To assign those remember to use the `:=` operator is you want to assign as objects.

### Pointers
* To pass an object to a function you better extract its position via the `&` operator, but it's not necessary and you can still use the `$` operator. `$function(&object);`.
* You can always extract the object with this operator safely.
* The operator loads the selected variable on the stack for the next operation;

### Instructions

* When assigning a variable you can also use an `if` expression: `var = if $condition { 7;} else { 8;};`.
* When assigning a variable you can also use an `while` expression: `var = while $condition { 7;};`. (this is very useless).
* Note that the if/while assignment works only with integers and on the first scope.
* If and while also work as instructions as expected.
* The `when` keyword is to use with the follow syntax: `when{<condition> then {<instructions>}... <condition> then {<instructions>}}`
* It's defined the `for` with the following syntax: `for <ident> in <complex> {<instructions>}`, where complex can be an object (cycling on first level fields), an array (cycling on elements) or a function or a value (cycling only one time on those values).  
From inside the for loop you can modify external values even the ones you are cycling to, but it will not modify the cycle loop.
* You can use the function declaration via `fun <name>(<params>){<instructions>}`. To terminate the function use the `return <value|variable>` keyword. If you don't specify a return value a predictable random one will be returned. Note that functions _crystallize the environment at creation_ so if **a = 9 and then you declare the function f and then change the a on the main scope, the a inside the function will still evaluates as 9**.
* At any time you can `import <function name>` to import a function from the native standard library. Scope applies on current environment.

### Streams

* In direct assignments (`=`) you can also call a `stream` to operate on an array as a stream of operation. Syntax is `stream <array> <operations>`. Operations can be either `map` to map the element to something else or `filter` to filter elements to terminate a stream either `collect` it to have an array back or `reduce` to a single number. Example: `stream arr filter (a) { return $a > 3; } then reduce (a, b) { return $a + $b };` or `stream dd filter(a) { t = $a > 3; return t; } then map(e) { return ($e + 1);} then collect;`.  
From inside the lambdas you can modify external values even the ones you are cycling to, but it will not modify the cycle loop.

## Examples
An implementation of the Naive Sort:  
```
fun swap(input, p1, p2){
 tmp = $input[$p1];
 input[$p1] = $input[$p2];
 input[$p2] = $tmp;
 return &input;
}
fun posMax(v, n){
 i = 0;
 max = 0;
 while $i < $n {
  if $v[$max] < $v[$i]{
   max = $i;
  }
 i = $i + 1;
 }
 return $max;
}
fun naive(v, n){
 p = 0;
 while $n > 1 {
  p = $posMax(&v, $n);
  if $p < $n - 1 {
   v = $swap(&v, $p, $n - 1);
  }
  n = $n - 1;
 }
 return &v;
}
a := [5,4,3,2,1];
expected = $naive(&a, size a);
```
				