package coll;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.PrimitiveSink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edgar on 2017/7/13.
 *
 * @author Edgar  Date 2017/7/13
 */
public class BloomFilterTest {

  @Test
  public void test() {
//Creating the BloomFilter
    BloomFilter bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), 1000);

//Putting elements into the filter
//A BigInteger representing a key of some sort
    bloomFilter.put("hello".getBytes());

//Testing for element in set
    System.out.println(bloomFilter.mightContain("hello".getBytes()));
    System.out.println(bloomFilter.mightContain("hello2".getBytes()));
  }

  private BloomFilter<BigInteger> bloomFilter;
  private Random random;
  private int numBits;
  private List<BigInteger> stored;
  private List<BigInteger> notStored;

  @Before
  public void setUp() {
    numBits = 128;
    random = new Random();
    stored = new ArrayList<BigInteger>();
    notStored = new ArrayList<BigInteger>();
    loadBigIntList(stored, 1000);
    loadBigIntList(notStored, 100);
  }

  @Test
  public void testMayContain() {
    setUpBloomFilter(stored.size());
    int falsePositiveCount = 0;
    for (BigInteger bigInteger : notStored) {
      boolean mightContain = bloomFilter.mightContain(bigInteger);
      boolean isStored = stored.contains(bigInteger);
      if (mightContain && !isStored) {
        falsePositiveCount++;
      }
    }
    System.out.println(falsePositiveCount);
//    Assert.assertThat(falsePositiveCount < 5, is(true));
  }

  @Test
  public void testMayContainGoOverInsertions() {
    setUpBloomFilter(50);
    int falsePositiveCount = 0;
    for (BigInteger bigInteger : notStored) {
      boolean mightContain = bloomFilter.mightContain(bigInteger);
      boolean isStored = stored.contains(bigInteger);
      if (mightContain && !isStored) {
        falsePositiveCount++;
      }
    }
    System.out.println(falsePositiveCount);
//    assertThat(falsePositiveCount, is(notStored.size()));
  }

  private void setUpBloomFilter(int numInsertions) {
    bloomFilter = BloomFilter.create(new BigIntegerFunnel(), numInsertions);
    addStoredBigIntegersToBloomFilter();
  }

  private BigInteger getRandomBigInteger() {
    return new BigInteger(numBits, random);
  }

  private void addStoredBigIntegersToBloomFilter() {
    for (BigInteger bigInteger : stored) {
      bloomFilter.put(bigInteger);
    }
  }

  private void loadBigIntList(List<BigInteger> list, int count) {
    for (int i = 0; i < count; i++) {
      list.add(getRandomBigInteger());
    }
  }

  private class BigIntegerFunnel implements Funnel<BigInteger> {
    @Override
    public void funnel(BigInteger from, PrimitiveSink into) {
      into.putBytes(from.toByteArray());
    }
  }
}
