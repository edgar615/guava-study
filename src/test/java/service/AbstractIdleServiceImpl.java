package service;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.concurrent.Executor;

/**
 * AbstractIdleService在我们服务处于running状态时，不会做执行任何动作，
 * 我们仅仅只有在startup和shutdown的时候才执行一些动作，所以我们在实现这个方法时，
 * 只是简单的实现startUp() 和 shutDown() 这两个方法即可，
 * 在startUp方法中做一些比如初始化，注册等操作，在shutDown中做一些清理操作等。
 */
public class AbstractIdleServiceImpl extends AbstractIdleService {
  @Override
  protected void startUp() throws Exception {
    System.out.println("startup");
  }

  @Override
  protected void shutDown() throws Exception {
    System.out.println("shutDown");
  }

  @Test
  public void test1() {
    AbstractIdleService service = new AbstractIdleServiceImpl();
    service.addListener(new Listener() {
      @Override
      public void starting() {
        System.out.println("服务开始启动.....");
      }
      @Override
      public void running() {
        System.out.println("服务开始运行");;
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
    System.out.println(service.state());
    service.stopAsync();
  }
}
