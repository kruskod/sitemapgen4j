package com.redfin.sitemapgenerator.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Denis Krusko
 * @author e-mail: kruskod@gmail.com
 *         Created on 16/10/14.
 */
public class SpaceTrimWriter extends FilterWriter {
    private boolean isStartSpace = true;
    private boolean lastCharWasSpace;

    public SpaceTrimWriter(Writer out) {
        super(out);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < len; i++)
            write(cbuf[i]);
    }

    public void write(String str, int off, int len) throws IOException {
        for (int i = off; i < len; i++)
            write(str.charAt(i));
    }

    public void write(int c) throws IOException {
        if (c == ' ')
            lastCharWasSpace = true;
        else if ('\t' == c) {
            c = ' ';
            lastCharWasSpace = true;
        }
        else {
            if (lastCharWasSpace) {
                if (!isStartSpace)
                    out.write(' ');
                lastCharWasSpace = false;
            }
            isStartSpace = false;
            out.write(c);
        }
    }
}
