package cn.xhmao.libs.utils;

import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Importer {
    private static final String TAG = Importer.class.getSimpleName();

    public static void importDB(SQLiteDatabase db, Reader reader) throws IOException, ParseException {
        BufferedReader bufferedReader;
        if (!BufferedReader.class.isInstance(reader)) {
            bufferedReader = new BufferedReader(reader);
        } else {
            bufferedReader = (BufferedReader) reader;
        }

        String line;

        line = bufferedReader.readLine();
        if (line == null) {
            return;
        }
//        int version = getVersion(line);
//        Log.d(TAG, "version = " + version);
//        db.setVersion(version);
        db.beginTransaction();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#") || line.startsWith("CREATE TABLE")) {
                continue;
            }
//            Log.d(TAG, "executing: " + line);
            db.execSQL(line);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        LogUtils.d(TAG, "====importDB====finish ====");
    }

    private static int getVersion(String line) throws ParseException {
        Pattern pattern = Pattern.compile("#\\s*version:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Failed to parse verion from line: " + line, 0);
        }

        try {
            return Integer.valueOf(matcher.group(1));
        } catch (Exception e) {
            throw new ParseException("Failed to parse verion from line: " + line, 0);
        }
    }
}
