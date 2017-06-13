package dev.tornaco.tasker.test;

/**
 * Created by Nick on 2017/6/7 13:30
 */

public class Module {

    private String testPkg;
    private String clz;
    private String method;

    private String title;
    private String description;

    public Module(String testPkg, String clz, String method, String title, String description) {
        this.testPkg = testPkg;
        this.clz = clz;
        this.method = method;
        this.title = title;
        this.description = description;
    }

    public Module() {
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTestPkg() {
        return testPkg;
    }

    public void setTestPkg(String testPkg) {
        this.testPkg = testPkg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Module{" +
                "testPkg='" + testPkg + '\'' +
                ", clz='" + clz + '\'' +
                ", method='" + method + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
