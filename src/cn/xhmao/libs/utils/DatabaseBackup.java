package cn.xhmao.libs.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

import cn.xhmao.libs.io.BufferedMaskReader;
import cn.xhmao.libs.io.BufferedMaskWriter;

/**
 * Created by xhmao on 7/9/13.
 */
public class DatabaseBackup {
    private final static String TAG = DatabaseBackup.class.getSimpleName();

    private final static String BACKUP_FILE = "status.dat";

    private String mState = Environment.getExternalStorageState();
    private Context mContext;
    private SQLiteDatabase mDb;

    public DatabaseBackup(Context context, SQLiteDatabase db) {
        mContext = context;
        mDb = db;
    }

    private boolean writable() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        if (Environment.MEDIA_MOUNTED.equals(mState)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(mState)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
        }

        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    private boolean readable() {
        return Environment.MEDIA_MOUNTED.equals(mState)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(mState);
    }

    public void backup(int[] types) {
        if (writable()) {
            File backup = new File(mContext.getFilesDir(), BACKUP_FILE);
            Writer writer = null;
            try {
                writer = new BufferedMaskWriter(new FileWriter(backup), 0xff);
                Exporter.dump(mDb, writer, types);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
    }

    public void restore() {
        if (readable()) {
            File backup = new File(mContext.getFilesDir(), BACKUP_FILE);
            if (backup.exists()) {
                Reader reader = null;
                try {
                    reader = new BufferedMaskReader(new FileReader(backup), 0xff);
                    Importer.importDB(mDb, reader);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {

                        }
                    }
                }
            }
        }
    }
}
