package concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/6/23.
 */
public class SmoothBurstyRateLimiterExample4 {

  public static void main(String[] args) throws InterruptedException {
    // SmoothBursty允许一定程度的突发，但假设突然连了很大的流量，那么系统很可能抗不住这种突发。
//    因此需要一周平滑速率的限流工具，从而系统冷启动后慢慢的趋于平均固定速率（即刚开始速率小一些，然后慢慢趋于我们设置的固定速率）
//    类似漏桶的实现
//    漏桶算法强制一个常量的输出速率而不管输入数据流的突发性,当输入空闲时，该算法不执行任何动作.就像用一个底部开了个洞的漏桶接水一样,水进入到漏桶里,
// 桶里的水通过下面的孔以固定的速率流出,当水流入速度过大会直接溢出,可以看出漏桶算法能强行限制数据的传输速率.如下图所示:
//
//    后面两参数表示从冷启动速率过渡到平均速率的时间间隔
    RateLimiter limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
//
//    速率是梯形上升速率，也就是说冷启动会以一个比较大的速率慢慢到平均速率；然后趋于平均速率。
//    可以通过调节warmupPeriod参数实现一开始就是平滑固定速率
    for (int i = 0; i < 6; i ++) {
      System.out.println(System.currentTimeMillis());
      double waitTime = limiter.acquire();
      System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);
    }
    TimeUnit.SECONDS.sleep(1);
    System.out.println("--------------------------------------------");
    for (int i = 0; i < 10; i ++) {
      System.out.println(System.currentTimeMillis());
      double waitTime = limiter.acquire();
      System.out.println(System.currentTimeMillis() + ":acquire ticket, waitTime:" + waitTime);
    }

//
// “漏桶算法”能够强行限制数据的传输速率，而“令牌桶算法”在能够限制数据的平均传输数据外，还允许某种程度的突发传输。在“令牌桶算法”中，只要令牌桶中存在令牌，那么就允许突发地传输数据直到达到用户配置的上限，因此它适合于具有突发特性的流量。
  }
}
