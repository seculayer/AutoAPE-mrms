package com.seculayer.util.io;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

/** A reusable {@link DataInput} implementation that reads from an in-memory
 * buffer.
 *
 * <p>This saves memory over creating a new DataInputStream and
 * ByteArrayInputStream each time data is read.
 *
 * <p>Typical usage is something like the following:<pre>
 *
 * DataInputBuffer buffer = new DataInputBuffer();
 * while (... loop condition ...) {
 *   byte[] data = ... get data ...;
 *   int dataLength = ... get data length ...;
 *   buffer.reset(data, dataLength);
 *   ... read buffer using DataInput methods ...
 * }
 * </pre>
 *  
 */
public class DataInputBuffer extends DataInputStream {
  private static class Buffer extends ByteArrayInputStream {
    public Buffer() {
      super(new byte[] {});
    }

    public void reset(byte[] input, int start, int length) {
      this.buf = input;
      this.count = start+length;
      this.mark = start;
      this.pos = start;
    }

    public byte[] getData() { return buf; }
    public int getPosition() { return pos; }
    public int getLength() { return count; }
  }

  private final Buffer buffer;
  
  /** Constructs a new empty buffer. */
  public DataInputBuffer() {
    this(new Buffer());
  }

  private DataInputBuffer(Buffer buffer) {
    super(buffer);
    this.buffer = buffer;
  }

  /** Resets the data that the buffer reads. */
  public void reset(byte[] input, int length) {
    buffer.reset(input, 0, length);
  }

  /** Resets the data that the buffer reads. */
  public void reset(byte[] input, int start, int length) {
    buffer.reset(input, start, length);
  }
  
  public byte[] getData() {
    return buffer.getData();
  }

  /** Returns the current position in the input. */
  public int getPosition() { return buffer.getPosition(); }

  /** Returns the length of the input. */
  public int getLength() { return buffer.getLength(); }

}
