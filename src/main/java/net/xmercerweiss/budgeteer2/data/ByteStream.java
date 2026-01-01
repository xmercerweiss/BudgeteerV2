package net.xmercerweiss.budgeteer2.data;

import java.util.*;
import java.nio.charset.*;

import net.xmercerweiss.budgeteer2.util.*;


/**
 * A mutable collection of raw binary data
 * @author Xavier Mercerweiss
 * @version v2.0 2025-12-31
 */
public class ByteStream
{
  // Class Constants
  public static final int MAX_STR_LEN = 256;
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
  public Byte read()
  {
    return data.poll();
  }

  public Byte[] read(int numOfBytes)
  {
    if (size() < numOfBytes)
      throw noBytesError();
    Byte[] bytes = new Byte[numOfBytes];
    for (int i = 0; i < numOfBytes; i++)
      bytes[i] = read();
    return bytes;
  }

  public void append(byte b)
  {
    data.add(b);
  }

  public void append(int i)
  {
    append((byte) i);
  }

  public void append(Byte[] bytes)
  {
    for (byte b : bytes)
      append(b);
  }

  public void appendInt(int i)
  {
    for (int j = 0; j < 4; j++)
    {
      System.out.printf("0x%02X\n", i & 0xFF);
      i >>>= 8;
      append(i & 0xFF);
    }
  }

  public void appendLong(long l)
  {
    byte mask = (byte) 0xFF;
    for (int i = 0; i < 8; i++)
    {
      l >>>= 8;
      append((byte) (l & mask));
    }
  }

  public void appendString(String str)
  {
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

  public void prepend(byte b)
  {
    data.push(b);
  }

  public void prepend(int i)
  {
    prepend((byte) i);
  }

  public void prepend(Byte[] bytes)
  {
    for (int i = bytes.length - 1; i >= 0; i--)
      prepend(bytes[i]);
  }

  public int readInt()
  {
    if (size() < 4)
      throw noBytesError();
    int out = 0;
    for (int i = 0; i < 4; i++)
    {
      out += read();
      out <<= 8;
    }
    return out;
  }

  public long readLong()
  {
    if (size() < 8)
      throw noBytesError();
    long out = 0;
    for (int i = 0; i < 8; i++)
    {
      out += read();
      out <<= 8;
    }
    return out;
  }

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

  public int size()
  {
    return data.size();
  }

  public boolean isEmpty()
  {
    return size() == 0;
  }

  public Byte[] toArray()
  {
    Byte[] out = new Byte[size()];
    data.toArray(out);
    return out;
  }
}
