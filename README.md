# My Personal Project

## A learning tool for the Simplex Algorithm from Linear Programming

All **bolded** terms are defined in a glossary at the bottom of this page.

This application is intended to provide a resource for students studying linear programming (at UBC this is MATH 340) to step through and check their work at different steps of the "**Simplex Algorithm**" (SA). For those unfamiliar with linear programming, many problems in real life can be restated in the form of *"maximize a linear function* $f$ *subject to some number of linear constraints"*. See the appendix for a simple concrete example of this form of problem.

My motivation for creating this project is that I found the available online resources for solving **linear programs** (LPs) to be rather lackluster.
- Many of them were able to offer a total solution without stepping capability, but given the number of arithmetic operations in the SA, stepping through would be an incredibly useful feature for checking work. 
- Despite the fact that the SA is an algorithm, there are some degrees of freedom in what **pivot rule** is being used at each step of the algorithm; no online resources offer satisfactory features in this aspect. 
- There are many different ways that the steps of the algorithm can be represented (MANY different variations of **tableau** or matrix). All online solvers offer only 1 representation each, making their usage cumbersome if the tableau used in your classroom setting is different from the one in the solver.

So, my project is to build a robust learning tool for students studying linear programming. Its features include stepping capability through the simplex algorithm according to the user's choice of **pivot rule** and support for multiple common tableau/matrix formats.

## User Stories

All **bolded** terms are defined in a glossary at the bottom of this page.

As a user, I want to be able to:
- Initialize the number (arbitrary) of variables in my **linear program** (LP)
- Set the **objective function** of my LP
- Add constraints to my LP (arbitrary number)
- Check if a given solution is **feasible** (satisfies all constraints)
- Check the objective function value of a given solution
- Solve my LP by manually performing **pivot** steps
- Solve my LP by algorithmically performing pivoting steps
- View a list of previous pivoting states (steps in solving)
- Check if my LP has been solved (optimal)
- Be reminded to save my current SolutionState when quitting and have the option to do so or not
- On startup, choose to load a saved SolutionState or not

## Instructions for End User

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by...
Clicking the "add constraint" button to be prompted to add a new constraint to the LP
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by...
Clicking the "X" button next to any constraint in the LP menu to delete it
- You can locate my visual component by...
Looking at the load menu which is displayed upon application startup
- You can save the state of my application by...
Clicking the "Save" button which will be displayed upon pressing the "Quit" button
- You can reload the state of my application by...
Clicking the "Load from save" button which is displayed in the load menu on application startup

## Appendix

For example, if one were trying to maximize the profit for a farming operation where each acre of wheat yielded \$10 of profit, while each acre of corn yields \$5 of profit, we can let $x,y$ be the number of acres allocated to wheat and corn respectively. This yields the *objective function* we are trying to maximize:
$$f(x,y) = 10x + 5y$$

Of course, every farm has a limited amount of land. Perhaps water resources or fertilizer is limited as well! We can add the following simple constraints:

Only 100 acres of land are available:
$$x + y \leq 100$$

Wheat needs 5 units water/acre, corn needs 2 units water/acre, but we have only 300 units of water:
$$5x + 2y \leq 300$$

Finally, there are the implicit constraints of non-negativity:
$$x,y \geq 0$$

This is just a simple example of a problem faced in linear programming, but in general, there can be an arbitrary number of variables and constraints.

## Glossary of Terms

**Linear Program/LP** - An objective function in $n$ variables together with a set of $m$ constraints. Broadly speaking, an LP is a mathematical model for a linear optimization problem.

**Objective function** - A linear function in $n$ variables that is intended to be maximized (or minimized). 

**Simplex Algorithm/SA** - A family of methods for solving LPs by **pivot** steps according to some **pivot rule**.

**Tableau** - Each step of the SA can be represented as a matrix. A tableau is just a way to write the information in that matrix so that it is easier for a reader to understand. It usually takes the form of a labeled rectangular table.

**Pivot** - A sequence of matrix operations performed on the matrix representation of an LP to progress (ideally) towards a solution. Each pivot is not necessarily strictly determined, depending on the **pivot rule** used.

**Pivot rule** - A method used to choose the pivot at each step of the SA. There are a variety of pivot rules, with different pros and cons (computational cost, upper bound on pivots needed, preventing cycling).

**Feasible/Infeasible** - A solution (allocation of variables) is feasible if it satisfies all constraints. Otherwise, it is infeasible.

**Optimal solution** - The allocation of variables that maximizes the objective function while remaining feasible.