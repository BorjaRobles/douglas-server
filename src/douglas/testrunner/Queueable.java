package douglas.testrunner;

// Common interface for both the Test and the TestExecution class
// This enables us to queue both Test objects as well as the TestExecution
// object which we use as "dividers" in the queue to establish when
// a complete execution of all tests within a product is completed.
public interface Queueable {
}
