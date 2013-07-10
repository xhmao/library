package cn.xhmao.libs.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by xhmao on 7/10/13.
 */
public class BufferedMaskWriter extends BufferedWriter {
    private static int defaultCharBufferSize = 8192;

    private int mMask;

    public BufferedMaskWriter(Writer out, int sz, int mask) {
        super(out, sz);
        mMask = mask;
    }

    public BufferedMaskWriter(Writer out, int mask) {
        this(out, defaultCharBufferSize, mask);
    }

    @Override
    public void write(String s) throws IOException {
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            cs[i] ^= mMask;
        }
        super.write(String.valueOf(cs));
    }
}
