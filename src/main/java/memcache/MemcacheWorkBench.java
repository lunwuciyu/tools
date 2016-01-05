package memcache;

import java.util.ResourceBundle;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemcacheWorkBench {

	private static MemcachedClient client;

	private static ResourceBundle bundle;

	private static Logger log = LoggerFactory
			.getLogger(MemcacheWorkBench.class);

	private static void configLoading() {
		if (bundle == null)
			bundle = ResourceBundle.getBundle("platform");
	}

	public static synchronized MemcachedClient getMemCachedClient() {
		configLoading();
		if (client == null) {
			String address = bundle.getString("memcached.server.address");
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					address);
			builder.setConnectionPoolSize(1);// nio线程默认为1
			try {
				client = builder.build();
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new CacheException("缓存创建出错", e);
			}
		}

		return client;
	}
}
