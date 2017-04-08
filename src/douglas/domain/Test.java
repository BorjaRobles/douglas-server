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

    public Test() {}

    public Test(String name, Long section) {
        this.name = name;
        this.section = section;
    }

    public enum Status {
        Unstable, Failed, Passed
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description", columnDefinition="LONGTEXT")
    private String description;

    @Column(name="active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name="test_status")
    private Status testStatus;

    @OneToMany(mappedBy="test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults = new ArrayList<>();

    @OneToMany(mappedBy="test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestStep> testSteps = new ArrayList<>();

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Status getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Status testStatus) {
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

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<TestStep> testSteps) {
        this.testSteps = testSteps;
    }


}