# Math Toolbox (Java)

## 📘 Description
**Math Toolbox** is a Java-based console application that allows users to perform symbolic and numerical mathematics directly from the command line. It is powered by the **Symja** library for symbolic math (calculus, algebra) and can be extended with other libraries for linear algebra or OCR-based image recognition.

## 🔹 Features
- **Derivatives**: Compute derivatives of functions with respect to `x`.  
  Example: `sin(x)*exp(x)` → `cos(x)*exp(x) + sin(x)*exp(x)`  
- **Integrals**: Compute indefinite integrals with respect to `x`.  
  Example: `x^2` → `x^3/3`  
- **Simplification**: Reduce algebraic expressions to simpler forms.  
  Example: `(x^2 + 2x + 1)` → `(x+1)^2`  
- **Equation Solving**: Solve equations for variable `x`.  
  Example: `x^2=4` → `{x = -2, x = 2}`  

## 🔹 User Experience
- The program runs in a **menu-driven format**.  
- Users choose an operation (derivative, integral, simplify, solve, or exit).  
- They then enter a mathematical expression, which the program evaluates and prints the result.  

## 🔹 Technical Details
- Written in **Java**  
- Uses **Symja (org.matheclipse.core)** for symbolic computation  
- Input is provided via the console (typed expressions)  
- Designed to be **lightweight and extendable**  

### Future Features (Planned)
- Linear algebra operations (matrix multiplication, determinants, eigenvalues)  
- Vector calculus (gradient, divergence, curl)  
- **OCR support (via Tesseract + Tess4J)** so the program can read screenshots of math problems and solve them automatically  

## 🔹 Example Run
```
==== Math Toolbox ====
1. Derivative
2. Integral
3. Simplify Expression
4. Solve Equation
0. Exit
Choose option: 2
Enter expression (use x as variable): sin(x)
Integral: -cos(x)
```
