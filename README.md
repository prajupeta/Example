 Example to achieve doSomething() method to be called by one thread at a time in multithread environment across JVMs  .
 This program depends on external file to read the current state. 
 First thread which is accessing doSomething() looks for"Ready" state (read from file lock.txt)
 if not ready then waits in a loop with 1 second interval.
 If state is ready then changes state to "Wait" and performs the required job
 changes the state back to "Ready" (write to file)
 
 This approach one way of addressing the problem, one can use database or check for http://www.terracotta.org/
