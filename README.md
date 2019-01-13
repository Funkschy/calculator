# Code example for the "Writing interpreters in Kotlin" presentation

The main method is inside the cli package.
It simply reads a line from stdin which is then lexed, parsed and evaluated.

# Syntax

The interpreter support the four basic math operations, parentheses, operator precedence and variables. 

### Example
```
test = 5
test = test + 2 * 3
test * (2 + 3)
```
=> 55
