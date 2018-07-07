package com.archangel_design.core_wars.utils.bugs;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BugLoader {

    private static final String BUG_FOLDER = "bugs/";

    public static HashMap<String, String> loadBugs() {
        HashMap<String, String> result = new HashMap<>();
        File folder = new File(BUG_FOLDER);
        List<File> fileList = Arrays.asList(folder.listFiles());
        fileList.forEach(f -> result.put(f.getName(), f.getAbsolutePath()));

        return result;
    }

    public static BugEntity loadBug(String name) {
        File f = new File(BUG_FOLDER + name);
        BugEntity bug = new BugEntity();
        bug.setPath(f.getAbsolutePath())
                .setName(name);

        return bug;
    }
}
