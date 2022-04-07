package com.socialsirius.messenger.base.data.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;


/**
 * Created by REIGOR on 23.02.2016.
 */


public class ProgressTypedFile extends RequestBody {

    private static final int BUFFER_SIZE = 4096;
    Listener listener;
    long totalSize = 0;
    int previousPercent;
    String mimeType;
    File file;

    public ProgressTypedFile(String mimeType, File file, Listener listener) {
        super();
        this.mimeType =mimeType;
        this.file = file;
        this.listener = listener;
        totalSize = file.length();
    }




    @Override
    public MediaType contentType() {
        if (mimeType==null || "".equals(mimeType)){
            return MediaType.parse("multipart/form-data");
        }
        return MediaType.parse(mimeType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long total = 0;
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                total += read;
                int percentage = (int) ((total / (float) totalSize) * 100);
                if (percentage > previousPercent) {
                    if( this.listener!=null){
                        this.listener.onUpdateProgress(percentage);
                    }
                }
                previousPercent = percentage;
               // out.write(buffer, 0, read);
                sink.outputStream().write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    public interface Listener {
        void onUpdateProgress(int percentage);
    }
}