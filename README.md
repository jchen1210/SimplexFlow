# My Personal Project

## A learning tool for the Simplex Algorithm from Linear Programming

This application is intended to provide a resource for students studying linear programming (at UBC this is MATH 340) to step through and check their work at different steps of the "**Simplex Algorithm**" (SA). For those unfamiliar with linear programming, many problems in real life can be restated in the form of *"maximize a linear function $f$ subject to some number of linear constraints"*. See the appendix for a simple concrete example of this form of problem.

My motivation for creating this project is that I found the available online resources for solving linear programs to be rather lackluster. Many of them were able to offer a total solution without stepping capability, but given the number of arithmetic operations in the SA, stepping through would be an incredibly useful feature for checking work. Furthermore, despite the fact that the SA is an algorithm, there are some degrees of freedom in what *pivot rule* is being used at each step of the algorithm; no online resources offer satisfactory features in this aspect. Finally, there are many different ways that the steps of the algorithm can be represented (MANY different variations of *tableau* or matrix). All online solvers offer only 1 representation each, making their usage cumbersome if the tableau used in your classroom setting is different from the one in the solver.

So, my project is to build a robust learning tool for students studying linear programming. Its features include stepping capability through the simplex algorithm according to the user's choice of *pivot rule* and support for multiple common tableau/matrix formats.


## Appendix

For example, if one were trying to maximize the profit for a farming operation where each acre of wheat yielded \$10 of profit, while each acre of corn yields \$5 of profit, we can let $x,y$ be the number of acres allocated to wheat and corn respectively. This yields the *objective function* we are trying to maximize:
$$f = 10x + 5y$$

Of course, every farm has a limited amount of land. Perhaps water resources or fertilizer is limited as well! We can add the following simple constraints:

Only 100 acres of land are available:
$$x + y \leq 100$$

Wheat needs 5 units water/acre, corn needs 2 units water/acre, but we have only 300 units of water:
$$5x + 2y \leq 300$$

This is just a simple example of a problem faced in linear programming, but in general, there can be an arbitrary number of variables and constraints.
 