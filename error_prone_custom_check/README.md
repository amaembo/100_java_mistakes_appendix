# Error Prone plugin sample

This project contains a sample of Error Prone static analysis plugin,
which reports computations of `Math.sqrt(x * x + y * y)`. Such computations
could be replaced with `Math.hypot(x, y)`. The implementation is described
in the appendix section A.1 of the book
"[100 Java Mistakes and How to Avoid Them](https://www.manning.com/books/100-java-mistakes-and-how-to-avoid-them)" by
Tagir Valeev.
