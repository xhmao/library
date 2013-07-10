package cn.xhmao.libs.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by xhmao on 7/10/13.
 */
public class BufferedMaskReader extends BufferedReader {
    private static int defaultCharBufferSize = 8192;

    private BufferedReader mIn;

    public BufferedMaskReader(Reader in, int sz, int mask) {
        super(in, sz);
        try {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = super.readLine()) != null) {
                sb.append(line);
            }
            char[] cs = sb.toString().toCharArray();
            for (int i = 0; i < cs.length; i++) {
                cs[i] ^= mask;
            }
            mIn = new BufferedReader(new StringReader(String.valueOf(cs)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedMaskReader(Reader in, int mask) {
        this(in, defaultCharBufferSize, mask);
    }

    public String readLine() throws IOException {
        if (mIn != null) {
            return mIn.readLine();
        }
        return null;
    }

    public void close() throws IOException {
        synchronized (lock) {
            if (mIn == null) {
                return;
            }

            mIn.close();
            mIn = null;
        }
        super.close();
    }
}
