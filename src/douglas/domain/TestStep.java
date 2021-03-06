package douglas.domain;

import javax.persistence.*;

@Entity
@Table(name="test_step")
public class TestStep {

    public TestStep() {}

    public TestStep(Long test, String action, String path, String value, Long metaLocationX, Long metaLocationY, String metaContent) {
        this.test = test;
        this.action = action;
        this.path = path;
        this.value = value;
        this.metaLocationX = metaLocationX;
        this.metaLocationY = metaLocationY;
        this.metaContent = metaContent;
    }

    public void setMeta(Long locationX, Long locationY, String content) {
        this.metaLocationX = locationX;
        this.metaLocationY = locationY;
        this.metaContent = content;
    }

    public enum Status {
        Passed, Unstable, Failed
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="test_step_status")
    private Status testStepStatus;

    @Column(name="action")
    private String action;

    @Column(name="path")
    private String path;

    @Column(name="value")
    private String value;

    @Column(name="metaLocationX")
    private Long metaLocationX;

    @Column(name="metaLocationY")
    private Long metaLocationY;

    @Column(name="metaContent")
    private String metaContent;

    @Column(name="suggestion", columnDefinition="LONGTEXT")
    private String suggestion;

    @Column(name="test_id")
    private Long test;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getTestStepStatus() {
        return testStepStatus;
    }

    public void setTestStepStatus(Status testStepStatus) {
        this.testStepStatus = testStepStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getMetaLocationX() {
        return metaLocationX;
    }

    public void setMetaLocationX(Long metaLocationX) {
        this.metaLocationX = metaLocationX;
    }

    public Long getMetaLocationY() {
        return metaLocationY;
    }

    public void setMetaLocationY(Long metaLocationY) {
        this.metaLocationY = metaLocationY;
    }

    public String getMetaContent() {
        return metaContent;
    }

    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Long getTest() {
        return test;
    }

    public void setTest(Long test) {
        this.test = test;
    }
}