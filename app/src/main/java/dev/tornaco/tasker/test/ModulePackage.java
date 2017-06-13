package dev.tornaco.tasker.test;

import java.util.ArrayList;

/**
 * Created by Nick on 2017/6/8 9:56
 */

public class ModulePackage {

    private String pkgName;
    private String packageTitle;
    private String packageDescription;
    private ArrayList<Module> modules;

    public ModulePackage(String pkgName, String packageTitle,
                         String packageDescription, ArrayList<Module> modules) {
        this.pkgName = pkgName;
        this.packageTitle = packageTitle;
        this.packageDescription = packageDescription;
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ModulePackage{" +
                "pkgName='" + pkgName + '\'' +
                ", packageTitle='" + packageTitle + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", modules=" + modules +
                '}';
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }
}
