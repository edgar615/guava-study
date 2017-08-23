package cache;

import com.google.common.base.Ticker;
import com.google.common.cache.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@link CacheBuilder}
 */
public final class FileSystemCacheBuilder<K, V> {

    /**
     * {@link CacheBuilder#from(CacheBuilderSpec)}
     */
    public static FileSystemCacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
        return new FileSystemCacheBuilder<Object, Object>(CacheBuilder.from(spec));
    }

    /**
     * {@link CacheBuilder#from(String)}
     */
    public static FileSystemCacheBuilder<Object, Object> from(String spec) {
        return new FileSystemCacheBuilder<Object, Object>(CacheBuilder.from(spec));
    }

    /**
     * {@link CacheBuilder#newBuilder()}
     */
    public static FileSystemCacheBuilder<Object, Object> newBuilder() {
        return new FileSystemCacheBuilder<Object, Object>();
    }

    private final CacheBuilder<Object, Object> underlyingCacheBuilder;

    private RemovalListener<? super K, ? super V> removalListener;
    private File persistenceDirectory;

    private FileSystemCacheBuilder() {
        this.underlyingCacheBuilder = CacheBuilder.newBuilder();
    }

    private FileSystemCacheBuilder(CacheBuilder<Object, Object> cacheBuilder) {
        this.underlyingCacheBuilder = cacheBuilder;
    }

    /**
     * {@link CacheBuilder#concurrencyLevel(int)}
     */
    public FileSystemCacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
        underlyingCacheBuilder.concurrencyLevel(concurrencyLevel);
        return this;
    }

    /**
     * {@link CacheBuilder#expireAfterAccess(long, TimeUnit)}
     */
    public FileSystemCacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
        underlyingCacheBuilder.expireAfterWrite(duration, unit);
        return this;
    }

    /**
     * {@link CacheBuilder#expireAfterWrite(long, TimeUnit)}
     */
    public FileSystemCacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
        underlyingCacheBuilder.expireAfterWrite(duration, unit);
        return this;
    }

    /**
     * {@link CacheBuilder#refreshAfterWrite(long, TimeUnit)}
     */
    public FileSystemCacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
        underlyingCacheBuilder.refreshAfterWrite(duration, unit);
        return this;
    }

    /**
     * {@link CacheBuilder#initialCapacity(int)}
     */
    public FileSystemCacheBuilder<K, V> initialCapacity(int initialCapacity) {
        underlyingCacheBuilder.initialCapacity(initialCapacity);
        return this;
    }

    /**
     * {@link CacheBuilder#maximumSize(long)}
     */
    public FileSystemCacheBuilder<K, V> maximumSize(long size) {
        underlyingCacheBuilder.maximumSize(size);
        return this;
    }

    /**
     * {@link CacheBuilder#maximumWeight(long)}
     */
    public FileSystemCacheBuilder<K, V> maximumWeight(long weight) {
        underlyingCacheBuilder.maximumWeight(weight);
        return this;
    }

    /**
     * {@link CacheBuilder#recordStats()}
     */
    public FileSystemCacheBuilder<K, V> recordStats() {
        underlyingCacheBuilder.recordStats();
        return this;
    }

    /**
     * {@link CacheBuilder#softValues()}
     */
    public FileSystemCacheBuilder<K, V> softValues() {
        underlyingCacheBuilder.softValues();
        return this;
    }

    /**
     * {@link CacheBuilder#weakKeys()}
     */
    public FileSystemCacheBuilder<K, V> weakKeys() {
        underlyingCacheBuilder.weakKeys();
        return this;
    }

    /**
     * {@link CacheBuilder#weakValues()}
     */
    public FileSystemCacheBuilder<K, V> weakValues() {
        underlyingCacheBuilder.weakValues();
        return this;
    }

    /**
     * {@link CacheBuilder#ticker(Ticker)}
     */
    public FileSystemCacheBuilder<K, V> ticker(Ticker ticker) {
        underlyingCacheBuilder.ticker(ticker);
        return this;
    }

    /**
     * {@link CacheBuilder#weigher(Weigher)}
     */
    @SuppressWarnings("unchecked")
    public <K1 extends K, V1 extends V> FileSystemCacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
        underlyingCacheBuilder.weigher(weigher);
        return (FileSystemCacheBuilder<K1, V1>) this;
    }

    /**
     * {@link CacheBuilder#removalListener(RemovalListener)}
     */
    public <K1 extends K, V1 extends V> FileSystemCacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
        checkState(this.removalListener == null);
        @SuppressWarnings("unchecked")
        FileSystemCacheBuilder<K1, V1> castThis = (FileSystemCacheBuilder<K1, V1>) this;
        castThis.removalListener = checkNotNull(listener);
        return castThis;
    }

    /**
     * Sets a location for persisting files. This directory <b>must not be used for other purposes</b>.
     *
     * @param persistenceDirectory A directory which is used by this file cache.
     * @return This builder.
     */
    public FileSystemCacheBuilder<K, V> persistenceDirectory(File persistenceDirectory) {
        checkState(this.persistenceDirectory == null);
        this.persistenceDirectory = checkNotNull(persistenceDirectory);
        return this;
    }

    /**
     * {@link CacheBuilder#build()}
     */
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        if (persistenceDirectory == null) {
            return new FileSystemPersistingCache<K1, V1>(underlyingCacheBuilder, FileSystemCacheBuilder.<K1, V1>castRemovalListener(removalListener));
        } else {
            return new FileSystemPersistingCache<K1, V1>(underlyingCacheBuilder, persistenceDirectory, FileSystemCacheBuilder.<K1, V1>castRemovalListener(removalListener));
        }
    }

    /**
     * {@link CacheBuilder#build(CacheLoader)}
     */
    public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
        if (persistenceDirectory == null) {
            return new FileSystemLoadingPersistingCache<K1, V1>(underlyingCacheBuilder, loader, FileSystemCacheBuilder.<K1, V1>castRemovalListener(removalListener));
        } else {
            return new FileSystemLoadingPersistingCache<K1, V1>(underlyingCacheBuilder, loader, persistenceDirectory, FileSystemCacheBuilder.<K1, V1>castRemovalListener(removalListener));
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> RemovalListener<K, V> castRemovalListener(RemovalListener<?, ?> removalListener) {
        if (removalListener == null) {
            return null;
        } else {
            return (RemovalListener<K, V>) removalListener;
        }
    }

    @Override
    public String toString() {
        return "FileSystemCacheBuilder{" +
                "underlyingCacheBuilder=" + underlyingCacheBuilder +
                ", persistenceDirectory=" + persistenceDirectory +
                '}';
    }
}
