package net.xmercerweiss.budgeteer2.data;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import net.xmercerweiss.budgeteer2.util.*;


/**
 * A mutable collection of raw binary data
 * @author Xavier Mercerweiss
 * @version v2.0 2026-01-01
 */
public class ByteStream
{
  // Class Constants
  public static final int MAX_STR_LEN = 255;
  public static final Charset CHARSET = StandardCharsets.UTF_8;

  private static final String HEX_FMT = "0x%02X";

  // Static Methods
  private static NoSuchElementException noBytesError()
  {
    return new NoSuchElementException(
      "Attempted to read more bytes than were present in ByteStream"
    );
  }

  // Instance Fields
  private final LinkedList<Byte> data;

  // Constructors
  /**
   * Constructs a new ByteStream containing the binary data of a file at a given path if it exists
   * @param filepath A filepath, may be null. An invalid or null path will cause an empty stream to be returned
   */
  public ByteStream(String filepath)
  {
    this();
    try (FileInputStream fileStream = new FileInputStream(filepath))
    {
      int i;
      while ((i = fileStream.read()) > -1)
        append(i);
    }
    catch (IOException _) {}
  }

  /**
   * Constructs a new empty ByteStream
   */
  public ByteStream()
  {
    data = new LinkedList<>();
  }

  // Override Methods
  @Override
  public String toString()
  {
    String[] hexBytes = data.stream()
      .map(b -> HEX_FMT.formatted(Byte.toUnsignedInt(b)))
      .toArray(String[]::new);
    return Arrays.toString(hexBytes);
  }

  // Public Methods

  /**
   * Removes and returns one byte of data from the front of this stream,
   * or null if this stream is empty
   * @return A Byte object, may be null
   */
  public Byte read()
  {
    return data.poll();
  }

  /**
   * Removes and returns a given number of bytes from the front of this stream,
   * if present
   * @param n The number of bytes to be read
   * @throws NoSuchElementException If this stream contains fewer bytes than requested
   * @return An array of Byte objects, not null
   */
  public Byte[] read(int n)
  {
    if (size() < n)
      throw noBytesError();
    Byte[] bytes = new Byte[n];
    for (int i = 0; i < n; i++)
      bytes[i] = read();
    return bytes;
  }

  /**
   * Removes and returns a 4-byte integer from the start of this stream
   * @return An integer
   * @throws NoSuchElementException If this stream contains fewer than 4 bytes
   */
  public int readInt()
  {
    if (size() < 4)
      throw noBytesError();
    long out = 0;
    int place = 0;
    for (int i = 0; i < 4; i++)
    {
      long mask = (long) read() << place;
      out |= mask;
      place += 8;
    }
    return (int) out;
  }

  /**
   * Removes and returns an 8-byte integer from the start of this stream
   * @return A long
   * @throws NoSuchElementException If this stream contains fewer than 8 bytes
   */
  public long readLong()
  {
    if (size() < 8)
      throw noBytesError();
    long out = 0;
    int place = 0;
    for (int i = 0; i < 8; i++)
    {
      long mask = (long) read() << place;
      out |= mask;
      place += 8;
    }
    return out;
  }

  /**
   * Removes and returns a string from the start of this stream
   * <br><br>
   * The byte at the start of the stream is interpreted as the length of the
   * encoded string in bytes. That number of bytes will then be read, decoded as
   * UTF-8, and returned
   * <br><br>
   * For example, {@code 0x04 0x4A 0x61 0x76 0x61} would be interpreted as:
   * <table>
   *   <tr>
   *     <td>{@code 0x04}</td>
   *     <td>A length of 4 bytes</td>
   *   </tr>
   *   <tr>
   *     <td>{@code 0x4A}</td>
   *     <td>J</td>
   *   </tr>
   *   <tr>
   *     <td>{@code 0x61}</td>
   *     <td>a</td>
   *   </tr>
   *   <tr>
   *     <td>{@code 0x76}</td>
   *     <td>v</td>
   *   </tr>
   *   <tr>
   *     <td>{@code 0x61}</td>
   *     <td>a</td>
   *   </tr>
   * </table>
   * Returning the string {@code "Java"}
   * @return A string, non null
   * @throws NoSuchElementException If this stream is empty or contains fewer bytes than necessary
   */
  public String readString()
  {
    int length;
    Byte head = read();
    if (head == null || (length = Byte.toUnsignedInt(head)) > size())
      throw noBytesError();
    byte[] bytes = new byte[length];
    for (int i = 0; i < length; i++)
      bytes[i] = read();
    return new String(bytes, CHARSET);
  }

  /**
   * Appends a given byte to the end of this stream
   * @param b A byte
   */
  public void append(byte b)
  {
    data.add(b);
  }

  /**
   * Appends the least significant byte of an integer to the end of this stream
   * @param i An integer
   */
  public void append(int i)
  {
    append((byte) (i & 0xFF));
  }

  /**
   * Appends an array of bytes to the end of this stream
   * @param bytes An array of bytes, not null
   * @throws NullPointerException If given an array equal to null
   */
  public void append(Byte[] bytes)
  {
    if (bytes == null)
      throw new NullPointerException();
    for (byte b : bytes)
      append(b);
  }

  /**
   * Appends a 4-byte integer to the end of this stream
   * @param i An integer
   */
  public void appendInt(int i)
  {
    for (int j = 0; j < 4; j++)
    {
      append(i & 0xFF);
      i >>>= 8;
    }
  }

  /**
   * Appends an 8-byte integer to the end of this stream
   * @param l A long
   */
  public void appendLong(long l)
  {
    byte mask = (byte) 0xFF;
    for (int i = 0; i < 8; i++)
    {
      append((byte) (l & mask));
      l >>>= 8;
    }
  }

  /**
   * Appends a string of characters to the end of this stream
   * <br><br>
   * The string is encoded as UTF-8 and, if necessary, truncated to 255 bytes. The
   * length of the string in bytes is written to the end of this stream, followed by
   * the encoded string
   * @param str A string, non null
   * @throws NullPointerException If given a string equal to null
   */
  public void appendString(String str)
  {
    if (str == null)
      throw new NullPointerException();
    int length = str.length();
    byte[] bytes = str.getBytes(CHARSET);
    while (bytes.length > MAX_STR_LEN)
    {
      str = str.substring(0, length - 1);
      bytes = str.getBytes(CHARSET);
    }
    append(bytes.length);
    for (byte b : bytes)
      append(b);
  }

  /**
   * Appends a given byte to the start of this stream
   * @param b A byte
   */
  public void prepend(byte b)
  {
    data.push(b);
  }

  /**
   * Appends the least significant byte of an integer to the start of this stream
   * @param i An integer
   */
  public void prepend(int i)
  {
    prepend((byte) (i & 0xFF));
  }

  /**
   * Appends an array of bytes to the end of this stream. The bytes will be read in
   * the order they as appear in the array
   * @param bytes An array of bytes, not null
   * @throws NullPointerException If given an array equal to null
   */
  public void prepend(Byte[] bytes)
  {
    if (bytes == null)
      throw new NullPointerException();
    for (int i = bytes.length - 1; i >= 0; i--)
      prepend(bytes[i]);
  }

  /**
   * @return The number of bytes within this stream
   */
  public int size()
  {
    return data.size();
  }

  /**
   * @return Whether this stream contains 0 bytes
   */
  public boolean isEmpty()
  {
    return size() == 0;
  }

  /**
   * Returns the contents of this stream as an array; this stream is not mutated in the process
   * @return An array of Byte objects
   */
  public Byte[] toArray()
  {
    Byte[] out = new Byte[size()];
    data.toArray(out);
    return out;
  }
}
