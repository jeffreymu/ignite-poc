package me.jeff.ignitepoc.cache;

import org.apache.ignite.configuration.CacheConfiguration;

public interface CacheConfigBuilder<K, T> {

    CacheConfiguration createCache(String cacheName, K k, T t);
}
