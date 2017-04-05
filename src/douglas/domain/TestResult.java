package douglas.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="test_result")
public class TestResult {

    public enum TestResultStatus {
        Passed, Unstable, Failed
    }

    public TestResult() {}

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description", columnDefinition="LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="test_result_status")
    private TestResultStatus testResultStatus;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestStep> testSteps = new ArrayList<>();

    @Column(name="test_id")
    private Long test;

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

    public TestResultStatus getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(TestResultStatus testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<TestStep> testSteps) {
        this.testSteps = testSteps;
    }
}