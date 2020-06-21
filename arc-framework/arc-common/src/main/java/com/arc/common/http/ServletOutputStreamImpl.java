package com.arc.common.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServletOutputStreamImpl extends ServletOutputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final DataOutputStream output = new DataOutputStream(baos);

    private byte[] byteArray = null;

    public byte[] getByteArray() {
        if (this.byteArray != null) {
            return byteArray;
        }

        byte[] sourceByteArray = this.baos.toByteArray();
        this.byteArray = new byte[sourceByteArray.length];
        System.arraycopy(sourceByteArray, 0, this.byteArray, 0, sourceByteArray.length);

        return this.byteArray;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
    }

    @Override
    public void write(int b) throws IOException {
        this.output.write(b);
    }

    @Override
    public void print(String s) throws IOException {
        this.output.writeChars(s);
    }

    @Override
    public void print(boolean b) throws IOException {
        this.output.writeBoolean(b);
    }

    @Override
    public void print(char c) throws IOException {
        this.output.writeChar(c);
    }

    @Override
    public void print(int i) throws IOException {
        this.output.writeInt(i);
    }

    @Override
    public void print(long l) throws IOException {
        this.output.writeLong(l);
    }

    @Override
    public void print(float f) throws IOException {
        this.output.writeFloat(f);
    }

    @Override
    public void print(double d) throws IOException {
        this.output.writeDouble(d);
    }

    @Override
    public void println() throws IOException {
        this.output.writeChars("\r\n");
    }

    @Override
    public void println(String s) throws IOException {
        this.print(s);
        this.println();
    }

    @Override
    public void println(boolean b) throws IOException {
        this.print(b);
        this.println();
    }

    @Override
    public void println(char c) throws IOException {
        this.print(c);
        this.println();
    }

    @Override
    public void println(int i) throws IOException {
        this.print(i);
        this.println();
    }

    @Override
    public void println(long l) throws IOException {
        this.print(l);
        this.println();
    }

    @Override
    public void println(float f) throws IOException {
        this.print(f);
        this.println();
    }

    @Override
    public void println(double d) throws IOException {
        this.print(d);
        this.println();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.output.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.output.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        this.output.flush();
    }

    @Override
    public void close() throws IOException {
        this.closeQuietly(this.output);
        this.closeQuietly(this.baos);
    }

    private void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

}
