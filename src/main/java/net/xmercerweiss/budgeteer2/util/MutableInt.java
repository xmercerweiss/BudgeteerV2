package net.xmercerweiss.budgeteer2.util;


/**
 * A mutable wrapper class for integer values
 * @author Xavier Mercerweiss
 * @version v2.0 2025-12-31
 */
public class MutableInt
{
  // Instance Fields
  private int datum;

  // Constructors
  public MutableInt(int i)
  {
    datum = i;
  }

  // Public Methods
  public int get()
  {
    return datum;
  }

  public void set(int i)
  {
    datum = i;
  }

  public void plus(int i)
  {
    datum += i;
  }

  public void minus(int i)
  {
    datum -= i;
  }

  public void increment()
  {
    datum++;
  }

  public void decrement()
  {
    datum--;
  }
}
