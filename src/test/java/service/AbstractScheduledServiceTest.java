package service;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.TimeUnit;

/**
 * AbstractScheduledService类用于在运行时处理一些周期性的任务。
 * 子类可以实现 runOneIteration()方法定义一个周期执行的任务，以及相应的startUp()和shutDown()方法。
 * 为了能够描述执行周期，你需要实现scheduler()方法。
 * 通常情况下，你可以使用AbstractScheduledService.Scheduler类提供的两种调度器：
 * newFixedRateSchedule(initialDelay, delay, TimeUnit) 和newFixedDelaySchedule(initialDelay, delay, TimeUnit)，
 * 类似于JDK并发包中ScheduledExecutorService类提供的两种调度方式。
 * 如要自定义schedules则可以使用 CustomScheduler类来辅助实现。一个实现类看起来应该是这样的
 */
public class AbstractScheduledServiceTest extends AbstractScheduledService {
  @Override
  protected void startUp() throws Exception {
  }
  @Override
  protected void shutDown() throws Exception {
  }
  @Override
  protected void runOneIteration() throws Exception {
    // //处理异常，这里如果抛出异常，会使服务状态变为failed同时导致任务终止
    try {
      System.out.println("do work....");
    } catch (Exception e) {
      //处理异常
    }
  }
  @Override
  protected Scheduler scheduler() {
    return Scheduler.newFixedDelaySchedule(1, 5, TimeUnit.SECONDS);
  }
  public static void main(String[] args) throws InterruptedException {
    AbstractScheduledServiceTest service = new AbstractScheduledServiceTest();
    service.addListener(new Listener() {
      @Override
      public void starting() {
        System.out.println("服务开始启动.....");
      }
      @Override
      public void running() {
        System.out.println("服务开始运行");
        ;
      }
      @Override
      public void stopping(State from) {
        System.out.println("服务关闭中");
      }
      @Override
      public void terminated(State from) {
        System.out.println("服务终止");
      }
      @Override
      public void failed(State from, Throwable failure) {
        System.out.println("失败，cause：" + failure.getCause());
      }
    }, MoreExecutors.directExecutor());
    service.startAsync().awaitRunning();
    System.out.println("服务状态为:" + service.state());
    Thread.sleep(10 * 1000);
    service.stopAsync().awaitTerminated();
    System.out.println("服务状态为:" + service.state());
  }
}