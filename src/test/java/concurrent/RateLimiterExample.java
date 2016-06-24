package concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/6/23.
 */
public class RateLimiterExample {

  //http://xiaobaoqiu.github.io/blog/2015/07/02/ratelimiter/
//  在 Wikipedia 上，令牌桶算法是这么描述的：
//
//  每秒会有 r 个令牌放入桶中，或者说，每过 1/r 秒桶中增加一个令牌
//  桶中最多存放 b 个令牌，如果桶满了，新放入的令牌会被丢弃
//  当一个 n 字节的数据包到达时，消耗 n 个令牌，然后发送该数据包
//  如果桶中可用令牌小于 n，则该数据包将被缓存或丢弃

//  令牌桶控制的是一个时间窗口内的通过的数据量，在 API 层面我们常说的 QPS、TPS，正好是一个时间窗口内的请求量或者事务量，只不过时间窗口限定在 1s 罢了。

//  令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，当桶里没有令牌可取时，则拒绝服务。
//  令牌桶的另外一个好处是可以方便的改变速度。 一旦需要提高速率，则按需提高放入桶中的令牌的速率。
//  一般会定时(比如100毫秒)往桶中增加一定数量的令牌， 有些变种算法则实时的计算应该增加的令牌的数量, 比如华为的专利"采用令牌漏桶进行报文限流的方法"(CN 1536815 A),提供了一种动态计算可用令牌数的方法， 相比其它定时增加令牌的方法， 它只在收到一个报文后，计算该报文与前一报文到来的时间间隔内向令牌漏桶内注入的令牌数， 并计算判断桶内的令牌数是否满足传送该报文的要求。

  public static void main(String[] args) throws InterruptedException {
    //令牌桶的实现
    //平滑突发限流（SmoothBursty）
    //桶容量为5，且每秒新增5个令牌，即每隔2000毫秒新增一个令牌
    RateLimiter limiter = RateLimiter.create(5);
    //limiter.acquire表示消费一个令牌，如果当前桶中有足够令牌则成功(返回值为0)
    //如果动作没有令牌则暂停一段时间，比如发令牌的间隔是200毫秒，则等待200毫秒后再去消费令牌
    //这种实现将突发请求速率平均为了固定请求速率
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());

    System.out.println("--------------------------------------------");
    limiter = RateLimiter.create(5);
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(5));
    //下面的方法将等待差不多1秒左右桶中才能有令牌
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(1));
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(1));

    System.out.println("--------------------------------------------");
    limiter = RateLimiter.create(5);
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(10));
    //下面的方法将等待差不多2秒左右桶中才能有令牌
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(1));
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire(1));


    System.out.println("--------------------------------------------");
    limiter = RateLimiter.create(2);
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    TimeUnit.SECONDS.sleep(5);
    //平滑突发限流（SmoothBursty）中有个参数：最大突发秒数maxBurstSeconds，默认值1秒
    //突发量/桶容量=速率*maxBurstSeconds

    //令牌桶算法允许将一段时间内没有消费的令牌暂存到令牌桶中，留待未来使用，并运行未来请求的这种突发
    //下面的方法前三个都能获取到令牌
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());


    //SmoothBursty运行一定程度的突发，会有人担心如果运行这种突发，假设突然连了很大的流量，那么系统很可能抗不住这种突发。
    //因此需要一周平滑速率的限流工具，从而系统冷启动后慢慢的趋于平均固定速率（即刚开始速率小一些，然后慢慢趋于我们设置的固定速率）
    //类似漏桶的实现
//    漏桶算法强制一个常量的输出速率而不管输入数据流的突发性,当输入空闲时，该算法不执行任何动作.就像用一个底部开了个洞的漏桶接水一样,水进入到漏桶里,桶里的水通过下面的孔以固定的速率流出,当水流入速度过大会直接溢出,可以看出漏桶算法能强行限制数据的传输速率.如下图所示:

    //后面两参数表示从冷启动速率过渡到平均速率的时间间隔
    System.out.println("--------------------------------------------");
    limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);

    //速率是梯形上升速率，也就是说冷启动会以一个比较大的速率慢慢到平均速率；然后趋于平均速率。
    //可以通过调节warmupPeriod参数实现一开始就是平滑固定速率
    for (int i = 0; i < 6; i ++) {
      System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    }
    TimeUnit.SECONDS.sleep(1);
    System.out.println("--------------------------------------------");
    for (int i = 0; i < 10; i ++) {
      System.out.println(System.currentTimeMillis() + ":" + limiter.acquire());
    }

//    “漏桶算法”能够强行限制数据的传输速率，而“令牌桶算法”在能够限制数据的平均传输数据外，还允许某种程度的突发传输。在“令牌桶算法”中，只要令牌桶中存在令牌，那么就允许突发地传输数据直到达到用户配置的上限，因此它适合于具有突发特性的流量。
  }
}
