package douglas.web.controller;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.domain.TestStep;
import douglas.persistence.ProductDao;
import douglas.persistence.TestDao;
import douglas.persistence.TestResultDao;
import douglas.testrunner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QueueProcessor {

    private TestDao testDao;
    private TestResultDao testResultDao;
    private ProductDao productDao;
    private TestQueue testQueue;


    @Autowired
    public QueueProcessor(TestDao testDao, TestResultDao testResultDao, ProductDao productDao, TestQueue testQueue) {
        this.testDao = testDao;
        this.testResultDao = testResultDao;
        this.productDao = productDao;
        this.testQueue = testQueue;
    }

    @Transactional
    public void saveTestResultAndUpdateTest(Test currentTest, TestResult testResult) {
        testDao.update(currentTest);
        testResultDao.save(testResult);
    }


    @Transactional
    public List<Test> getTestsOfproduct(String id) {
        return productDao.allTestsInProduct(id);
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 5_000) //every 5 seconds, initial delay 1 second
    public void scheduledProcessing() throws InterruptedException {

        // While the testqueue isn't empty
        while(!testQueue.isEmpty()) {

            // Get next item
            Queueable currentItem = testQueue.nextItem();

            // If the next item is a test
            if (currentItem instanceof Test) {
                Test currentTest = (Test) currentItem;

                Test returnedTest = TestRunner.run(currentTest);

                // Create a TestResult based on the executed test
                TestResult result = new ResultGenerator().generate(returnedTest);

                // Save everything
                saveTestResultAndUpdateTest(returnedTest, result);

            } else {
                // Else we have a TestExecution which marks the end of a queued product test run
                // when we reach a TestExecution object we pick the productId out of it
                // and check whether or not all of the tests within the product passed
                // based on this do we create an entry in a file which can be read by
                // a CI server.

                TestExecution currentExecution = (TestExecution)currentItem;
                String idOfProduct = currentExecution.getProductId();
                
                // Pick out product id
                List<Test> testsInProduct = this.getTestsOfproduct(idOfProduct);

                boolean failed = false;
                String status = Test.Status.Passed.name();

                // For each test in the product, check if all tests passed
                for(Test test : testsInProduct) {
                    if(Test.Status.Unstable.equals(test.getTestStatus())) {
                        if(!failed) {
                            status = Test.Status.Unstable.name();
                        }
                    }
                    if(Test.Status.Failed.equals(test.getTestStatus())) {
                        failed = true;
                        status = Test.Status.Failed.name();
                    }
                }

                String currentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()).toString();

                // Write status of test run to file for now
                try {
                    String content = "Tests executed for product with ID: " + idOfProduct + " - Status: "+ status +" - " +currentTime + "\n";

                    File file = new File(System.getProperty("user.home") + "/douglas-results.txt");

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(content);
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
