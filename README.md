
# GLogger library for Java  

In this library, logs events will shown in JSON format. 
Operation logs are intended to use on method specific level, means that:
<ol>
<li>An operation log must be created at the first line of a method,</li>
<li>Get its parameters while the method continues to run,</li>
<li>Log the result of the method, and the parameters the object has gathered</li>
</ol>

An operation log can be initialized calling the startOperation method on GLogger class 
and can be terminated via one of its termination methods;
<ul>
<li>succeed()</li>
<li>warn()</li>
<li>trace()</li>
<li>fail()</li>
</ul>
The logger will create a log with TRACE level initially and by default an INFO log when gets completed.
<br>
Exit log level can be overwritten while creating the operation log. Accepted levels are, TRACE, DEBUG and INFO.
<br>

An example usage of operation logs is as follows:
<pre>
private GLogger logger = new GLogger(Bar.class);
public void foo(bar) {
      OperationLog operation = logger.startOperation("sampleOperation");
     // ...
     try {
     	   // Add fields to current operation.
         operation.addField("someField", "someValue").addField("someOtherField", 5);
         // ...
         operation.succeed(); // Log fields with INFO level and total elapsed time.
     } catch(Exception e) {
         operation.warn(e); // Log fields and given exception with WARN level.
     } catch(Throwable t) {
         operation.fail(t); // Log fields and given exception with ERROR level.
     }
 }
</pre>