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

        List<TestStep> resultSteps = new ArrayList<>();
        for(TestStep step : testResult.getTestSteps()) {
            step.setParent(testResult.getId());
            resultSteps.add(step);
        }

        testResult.setTestSteps(resultSteps);
        testResultDao.save(testResult);
        // HER SKAL MÅSKE GØRES LIDT SÅ DEN GEMMER DET HELE RIGTIGT OG MED DE RIGTIGE REFERENCER I TESTSTATUS ENTITETEN
    }


    @Transactional
    public List<Test> getTestsOfproduct(String id) {
        return productDao.allTestsInProduct(id);
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 5_000) //every 5 seconds, initial delay 1 second
    public void scheduledProcessing() throws InterruptedException {

        Test testtest = testDao.findById("3");
        testQueue.add(testtest);

        // While the testqueue isn't empty
        while(!testQueue.isEmpty()) {

            // Get next item
            Queueable currentItem = testQueue.nextItem();

            // If the next item is a test
            if (currentItem instanceof Test) {
                Test currentTest = (Test) currentItem;

                TestResult result = TestRunner.run(currentTest);

                // Mark the test based on the test result status
                if (TestResult.TestResultStatus.Passed.equals(result.getTestResultStatus())) {
                    currentTest.setTestStatus(Test.TestStatus.Passed);
                } else if (TestResult.TestResultStatus.Failed.equals(result.getTestResultStatus())) {
                    currentTest.setTestStatus(Test.TestStatus.Failed);
                } else {
                    currentTest.setTestStatus(Test.TestStatus.Unstable);
                }

                // Fetch the newest meta and apply it to the actual test
                // we thereby have up-to-date information about the test
                Test updatedTest = new MetaHandler().transferMeta(currentTest, result);
                
                // Save everything
                saveTestResultAndUpdateTest(updatedTest, result);

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
                String status = Test.TestStatus.Passed.name();

                // For each test in the product, check if all tests passed
                for(Test test : testsInProduct) {
                    if(Test.TestStatus.Unstable.equals(test.getTestStatus())) {
                        if(!failed) {
                            status = Test.TestStatus.Unstable.name();
                        }
                    }
                    if(Test.TestStatus.Failed.equals(test.getTestStatus())) {
                        failed = true;
                        status = Test.TestStatus.Failed.name();
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
