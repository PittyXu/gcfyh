/**
 * .Net 的 Random 类 部分 Java 实现
 */
public class Random {

  private static final int MAX_VALUE = 0x7fffffff;
  private static final int MIN_VALUE = -2147483648;
  private static final int MSEED = 161803398;

  int inext;
  int inextp;
  int[] SeedArray = new int[56];

  public Random(int Seed) {
    int ii;
    int subtraction = (Seed == MIN_VALUE) ? MAX_VALUE : Math.abs(Seed);
    int mj = MSEED - subtraction;
    SeedArray[55] = mj;
    int mk = 1;
    for (int i = 1; i < 55; i++) {  //Apparently the range [1..55] is special (Knuth) and so we're wasting the 0'th position.
      ii = (21 * i) % 55;
      SeedArray[ii] = mk;
      mk = mj - mk;
      if (mk < 0) {
        mk += MAX_VALUE;
      }
      mj = SeedArray[ii];
    }
    for (int k = 1; k < 5; k++) {
      for (int i = 1; i < 56; i++) {
        SeedArray[i] -= SeedArray[1 + (i + 30) % 55];
        if (SeedArray[i] < 0) {
          SeedArray[i] += MAX_VALUE;
        }
      }
    }
    inext = 0;
    inextp = 21;
    Seed = 1;
  }

  private int InternalSample() {
    int retVal;
    int locINext = inext;
    int locINextp = inextp;

    if (++locINext >= 56) {
      locINext = 1;
    }
    if (++locINextp >= 56) {
      locINextp = 1;
    }

    retVal = SeedArray[locINext] - SeedArray[locINextp];

    if (retVal == MAX_VALUE) {
      retVal--;
    }
    if (retVal < 0) {
      retVal += MAX_VALUE;
    }

    SeedArray[locINext] = retVal;

    inext = locINext;
    inextp = locINextp;

    return retVal;
  }

  protected double Sample() {
    return (InternalSample() * (1.0 / MAX_VALUE));
  }

  public int Next(int maxValue) {
    return (int) (Sample() * maxValue);
  }
}




