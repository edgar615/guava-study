package concurrent;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by Administrator on 2015/6/23.
 */
public class SmoothBurstyRateLimiterExample2 {

  public static void main(String[] args) throws InterruptedException {
    //令牌桶的实现
    //平滑突发限流（SmoothBursty）
    //桶容量为5，且每秒新增5个令牌，即每隔200毫秒新增一个令牌
    RateLimiter limiter = RateLimiter.create(5);
    System.out.println(System.currentTimeMillis());
    double waitTime = limiter.acquire(5);
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    //下面的方法将等待差不多1秒左右桶中才能有令牌
    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    //下面的方法将等待差不多200毫秒左右桶中才能有令牌
    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);
  }
}
