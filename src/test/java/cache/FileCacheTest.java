package cache;

import com.google.common.cache.Cache;

/**
 * Created by Edgar on 2017/8/23.
 *
 * @author Edgar  Date 2017/8/23
 */
public class FileCacheTest {
  public static void main(String[] args) {
    Cache<String, String> stringCache =
            FileSystemCacheBuilder.newBuilder()
                    .maximumSize(10L)
                    .softValues()
                    .build();
    for (int i = 0; i < 20; i++) {
      stringCache.put("k" + i, "v" + i);
    }
    System.out.println(stringCache.asMap());
  }
}
