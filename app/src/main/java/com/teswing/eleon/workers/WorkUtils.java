package com.teswing.eleon.workers;

import android.content.Context;
import android.net.Uri;

import com.teswing.eleon.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorkUtils {

    static Uri getRepository(Context applicationContext) {
        File repDir = new File(applicationContext.getFilesDir(), Constants.JSON_REPOSITORY_PATH);
        if (!repDir.exists()) {
            repDir.mkdir();
        }
        File repFile = new File(repDir, Constants.JSON_REPOSITORY_FILE);
        if(!repFile.exists()){
            try (FileWriter writer = new FileWriter(repFile)){
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(repFile);
    }
}
