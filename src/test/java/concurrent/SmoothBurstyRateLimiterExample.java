package concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/6/23.
 */
public class SmoothBurstyRateLimiterExample {

  public static void main(String[] args) throws InterruptedException {
    //令牌桶的实现
    //平滑突发限流（SmoothBursty）
    //桶容量为5，且每秒新增5个令牌，即每隔200毫秒新增一个令牌
    RateLimiter limiter = RateLimiter.create(5);
    //limiter.acquire表示消费一个令牌，如果当前桶中有足够令牌则成功(返回值为0)
    //如果动作没有令牌则暂停一段时间，比如发令牌的间隔是200毫秒，则等待200毫秒后再去消费令牌
    //这种实现将突发请求速率平均为了固定请求速率
    System.out.println(System.currentTimeMillis());
    double waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
     waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

    System.out.println(System.currentTimeMillis());
    waitTime = limiter.acquire();
    System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);

  }
}
