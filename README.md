# Homework 1
### [Main](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1): 
Write a simple program (one or two files, the content doesn't matter, but it must include a package), and add a Readme file with a description of:
- Compilation and running the program in the console
- Building an executable JAR file 
### Additional sub-tasks:
- **[Additionally 1:](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1_Subtasks/Subtask1)** Connect an external library and perform
the two previous steps 
- **[Additionally 2:](https://github.com/YULIYA2001/LeverXCource/tree/homework1-main/Homework1_Subtasks/Subtask2)** Enrich source code with different types
of classes: inner/nested, anonymous, and local classes, compile and analyze the result 
- **[Additionally 3:](https://github.com/YULIYA2001/LeverXCource/tree/homework1-subtask3-lambda/Homework1_Subtasks/Subtask3)** As in the previous example, replace all types
of classes with anonymous classes and lambda expressions implementing the same interface. Compile and analyze the result

# Homework 2
### [_Multithreading:_ Console store](#Link-to-folder-on-main-branch)
There is a product catalog — a list of objects with price and quantity in stock.
Several customers create orders at the same time using multithreading (Runnable or ExecutorService).
The warehouse is a shared resource (ConcurrentHashMap<Product, Integer>).
Several warehouse workers process orders taken from a BlockingQueue<Order>.

After all orders are processed, run analytics in parallel (parallelStream) to show:
- the total number of orders;
- the total profit;
- the top 3 best-selling products.
