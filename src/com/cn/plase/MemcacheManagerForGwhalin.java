package com.cn.plase;
import java.util.Date;

import com.danga.MemCached.*;
 
public class MemcacheManagerForGwhalin {
 
	// 构建缓存客户端 
	private static MemCachedClient cachedClient;
	// 单例模式实现客户端管理类
	private static MemcacheManagerForGwhalin INSTANCE = new MemcacheManagerForGwhalin();
 
	private MemcacheManagerForGwhalin() {
		cachedClient = new MemCachedClient();
		
		// 初始化SockIOPool，管理memcached的连接池
		SockIOPool pool = SockIOPool.getInstance();
 
		// 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器。（这里应该设置为多个不同的服务器）
		String[] servers = { "192.168.1.100:11200", "192.168.1.102:11200"
		// 也可以使用域名 "server3.mydomain.com:1624"
		};
 
		pool.setServers(servers);
		pool.setFailover(true);
		pool.setInitConn(10); // 设置初始连接
		pool.setMinConn(5);// 设置最小连接
		pool.setMaxConn(250); // 设置最大连接
		pool.setMaxIdle(1000 * 60 * 60 * 3); // 设置每个连接最大空闲时间3个小时
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setAliveCheck(true);
		pool.initialize();
	}
 
	/**
	 * 获取缓存管理器唯一实例
	 * 
	 * @return
	 */
	public static MemcacheManagerForGwhalin getInstance() {
		return INSTANCE;
	}
 
	public void add(String key, Object value) {
		cachedClient.set(key, value);
	}
 
	public void add(String key, Object value, int milliseconds) {
		cachedClient.set(key, value, milliseconds);
	}
 
	public void remove(String key) {
		cachedClient.delete(key);
	}
 
	@SuppressWarnings("deprecation")
	public void remove(String key, int milliseconds) {
		cachedClient.delete(key, milliseconds, new Date());
	}
 
	public void update(String key, Object value, int milliseconds) {
		cachedClient.replace(key, value, milliseconds);
	}
 
	public void update(String key, Object value) {
		cachedClient.replace(key, value);
	}
 
	public Object get(String key) {
		return cachedClient.get(key);
	}
 
	public static void main(String[] args) {
		//将对象加入到memcached缓存
		cachedClient.add("keke", "This is a test String");
		//从memcached缓存中按key值取对象
		Object result  = cachedClient.get("keke");
		System.out.println(result);
	}
	
}
