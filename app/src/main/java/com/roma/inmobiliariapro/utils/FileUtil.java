package com.roma.inmobiliariapro.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static File from(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver() .openInputStream(uri);

        File tempFile = File.createTempFile(
                "temp",
                ".jpg",
                context.getCacheDir()
        );

        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];

        int read;

        while((read = inputStream.read(buffer)) != -1) {

            outputStream.write(buffer, 0, read);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }
}