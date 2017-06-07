package dev.tornaco.tasker.test;

/**
 * Created by Nick on 2017/6/7 13:30
 */

public class UnitTest {

    private String testPkg;
    private String clz;
    private String method;

    public UnitTest(String testPkg, String clz, String method) {
        this.testPkg = testPkg;
        this.clz = clz;
        this.method = method;
    }

    public UnitTest() {
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
}
