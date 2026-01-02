package net.xmercerweiss.budgeteer2.tests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import net.xmercerweiss.budgeteer2.data.ByteStream;


/**
 * Unit tests of the {@link net.xmercerweiss.budgeteer2.data.ByteStream} class
 * @author Xavier Mercerweiss
 * @version v2.0 2026-01-01
 */
public class ByteStreamTest
{
  private static final Byte[] FILLER_BYTES = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

  private static final Byte[][] VALID_BYTE_ARRAYS = {
    {1, 2, 3, 4, 5},
    {0},
    {},
    {7, 7, 7, 7, 7},
    {72, 101, 108, 108, 111, 10},
  };

  private static final String[] VALID_STRINGS = {
    "Hello",
    "Again",
    "Friend of a friend",
    "Dollars $",
    "Pounds £",
    "Yen ¥",
    "🎃",
  };

  @Test
  void Append_WithValidArrays_ReadsExpectedOrder()
  {
    for (Byte[] data : VALID_BYTE_ARRAYS)
    {
      ByteStream bytes = new ByteStream();
      bytes.append(FILLER_BYTES);
      bytes.append(data);
      assertArrayEquals(FILLER_BYTES, bytes.read(FILLER_BYTES.length));
      assertArrayEquals(data, bytes.read(data.length));
    }
  }

  @Test
  void Prepend_WithValidArrays_ReadsExpectedOrder()
  {
    for (Byte[] data : VALID_BYTE_ARRAYS)
    {
      ByteStream bytes = new ByteStream();
      bytes.prepend(FILLER_BYTES);
      bytes.prepend(data);
      assertArrayEquals(data, bytes.read(data.length));
      assertArrayEquals(FILLER_BYTES, bytes.read(FILLER_BYTES.length));
    }
  }

  @Test
  void Append_WithInts_ReadsEqual()
  {
    ByteStream bytes = new ByteStream();
    for (int i = 0; i < 10; i++)
      bytes.appendInt(i);
    for (int expected = 0; expected < 10; expected++)
    {
      int actual = bytes.readInt();
      assertEquals(expected, actual);
    }
  }

  @Test
  void Append_WithLong_ReadsEqual()
  {
    ByteStream bytes = new ByteStream();
    for (long l = 0; l < 10; l++)
      bytes.appendLong(l);
    for (long expected = 0; expected < 10; expected++)
    {
      long actual = bytes.readLong();
      assertEquals(expected, actual);
    }
  }

  @Test
  void Append_WithValidStrings_ReadsEqual()
  {
    for (String str : VALID_STRINGS)
    {
      ByteStream bytes = new ByteStream();
      bytes.appendString(str);
      assertEquals(str, bytes.readString());
    }
  }
}
