package douglas.domain;

import douglas.testrunner.Queueable;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name="test")
public class Test implements Queueable {

    private Test() {}

    public enum TestStatus {
        Running, Unstable, Failed, Passed
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description", length = 1000)
    private String description;

    @Column(name="steps", length = 10000)
    private String steps;

    @Column(name="active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name="test_status")
    private TestStatus testStatus;

    @OneToMany(mappedBy="test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults = new ArrayList<>();

    @Column(name="section_id")
    private Long section;

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

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) throws UnsupportedEncodingException {
        this.steps = new String(Base64.getDecoder().decode(steps), "UTF-8");
    }

    public void setDecodedSteps(String steps) {
        this.steps = steps;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public Long getSection() {
        return section;
    }

    public void setSection(Long section) {
        this.section = section;
    }
}