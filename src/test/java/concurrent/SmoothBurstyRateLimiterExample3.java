package concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/6/23.
 */
public class SmoothBurstyRateLimiterExample3 {

  public static void main(String[] args) throws InterruptedException {
    RateLimiter limiter = RateLimiter.create(2);
    System.out.println(System.currentTimeMillis());
    double waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);
    TimeUnit.SECONDS.sleep(5);
    //平滑突发限流（SmoothBursty）中有个参数：最大突发秒数maxBurstSeconds，默认值1秒
    //突发量/桶容量=速率*maxBurstSeconds

    //令牌桶算法允许将一段时间内没有消费的令牌暂存到令牌桶中，留待未来使用，应对未来请求的突发
    //下面的方法前三个都能获取到令牌
    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    //下面的方法将等待差不多200毫秒左右桶中才能有令牌
    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);
  }
}
