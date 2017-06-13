package dev.tornaco.tasker.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import dev.tornaco.tasker.utils.Enforcer;

/**
 * Created by Nick on 2017/6/7 15:50
 */

public class PackageModuleParser {

    public static ArrayList<Module> parse(String raw) throws Throwable {
        try {
            Enforcer.enforceNoNull(raw);
            Gson gson = new Gson();
            return gson.fromJson(raw, new TypeToken<ArrayList<Module>>() {
            }.getType());
        } catch (Throwable e) {
            throw new IllegalStateException("Fail parse modules", e);
        }
    }
}
