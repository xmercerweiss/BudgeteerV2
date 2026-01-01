package net.xmercerweiss.budgeteer2.data;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

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

  // Public Methods
  public int size()
  {
    return data.size();
  }

  public boolean isEmpty()
  {
    return size() == 0;
  }

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

  public void append(int i)
  {
    data.add((byte) i);
  }

  public void prepend(int i)
  {
    data.push((byte) i);
  }

  public String readString()
  {
    int length;
    Byte head = read();
    if (head == null || (length = Byte.toUnsignedInt(head)) > size())
      throw noBytesError();
    StringBuilder mut = new StringBuilder();
    while (length > 0)
    {
      mut.appendCodePoint(read());
      length--;
    }
    return mut.toString();
  }

  public void writeString(String str)
  {
    IntStream codePoints = str.codePoints().limit(MAX_STR_LEN);
    MutableInt size = new MutableInt(0);
    codePoints.forEach(
      i -> {
        size.increment();
        append(i);
      }
    );
    prepend(size.get());
  }
}
