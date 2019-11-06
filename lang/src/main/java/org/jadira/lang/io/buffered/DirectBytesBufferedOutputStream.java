/*
 *  Copyright 2013 Chris Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.lang.io.buffered;

import jdk.internal.ref.Cleaner;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A BufferedOutputStream where the buffer is provided by a NIO DirectByteBuffer. Use this class as an alternative to {@link java.io.BufferedOutputStream}
 */
public class DirectBytesBufferedOutputStream extends AbstractBufferedOutputStream {

    private ByteBuffer buf;

    /**
     * Creates a new instance with the default buffer size
     * @param out OutputStream to be decorated
     * @see java.io.BufferedOutputStream#BufferedOutputStream(OutputStream)
     */
    public DirectBytesBufferedOutputStream(OutputStream out) {
        this(out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Creates a new instance with the given buffer size in bytes.
     * @param out OutputStream to be decorated
     * @param size The size of the buffer in bytes
     * @see java.io.BufferedOutputStream#BufferedOutputStream(OutputStream, int)
     */
    public DirectBytesBufferedOutputStream(OutputStream out, int size) {
        super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size may not be less than or equal to zero");
        }
        buf = ByteBuffer.allocateDirect(size);
    }

    /**
     * Creates a new instance with the given buffer size in bytes.
     * @param out OutputStream to be decorated
     * @param size The size of the buffer in bytes
     * @param useNativeByteOrder If true, native byte ordering will be used
     * @see java.io.BufferedOutputStream#BufferedOutputStream(OutputStream, int)
     */
    public DirectBytesBufferedOutputStream(OutputStream out, int size, boolean useNativeByteOrder) {
        this(out, size);
        buf.order(ByteOrder.nativeOrder());
    }

    /**
     * Creates a new instance with the given buffer size in bytes.
     * @param out OutputStream to be decorated
     * @param size The size of the buffer in bytes
     * @param byteOrder Indicates the byte order to be used.
     * @see java.io.BufferedOutputStream#BufferedOutputStream(OutputStream, int)
     */
    public DirectBytesBufferedOutputStream(OutputStream out, int size, ByteOrder byteOrder) {
        this(out, size);
        buf.order(byteOrder);
    }

    @Override
    protected byte[] bytes() {
        return buf.array();
    }

    @Override
    protected void put(byte b) {
        put(b);
    }

    @Override
    protected void put(int count, byte[] b, int off, int len) {
        if (off == 0) {
            buf.put(b, count, len);
        } else {
            byte[] bc = new byte[len];
            System.arraycopy(b, off, bc, 0, len);
            buf.put(bc, count, len);
        }
    }

    @Override
    protected int limit() {
        return buf.limit();
    }

    @SuppressWarnings("restriction")
    @Override
    protected void clean() {

        if (buf == null) {
            return;
        }

        Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buf).cleaner();

        if (cleaner != null) {
            cleaner.clean();
        }
    }
}
