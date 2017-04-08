package douglas.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="test_result")
public class TestResult {

    public TestResult() {}

    public TestResult(String name, String description, String steps, Test.Status testResultStatus, Long test) {
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.testResultStatus = testResultStatus;
        this.test = test;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description", columnDefinition="LONGTEXT")
    private String description;

    @Column(name="steps", columnDefinition="LONGTEXT")
    private String steps;

    @Enumerated(EnumType.STRING)
    @Column(name="test_result_status")
    private Test.Status testResultStatus;

    @Column(name="test_id")
    private Long test;

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Test.Status getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(Test.Status testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
    }

}