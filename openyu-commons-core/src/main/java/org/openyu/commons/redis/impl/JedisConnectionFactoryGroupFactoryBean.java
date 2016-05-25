package org.openyu.commons.redis.impl;

import org.openyu.commons.redis.supporter.JedisConnectionFactoryFactorySupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionFactoryGroupFactoryBean
		extends JedisConnectionFactoryFactorySupporter<JedisConnectionFactory[]> {

	private static final long serialVersionUID = 6531223919329351922L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(JedisConnectionFactoryGroupFactoryBean.class);

	private JedisPoolConfig[] jedisPoolConfigs;
	
	private JedisConnectionFactory[] jedisConnectionFactorys;

	public JedisConnectionFactoryGroupFactoryBean(JedisPoolConfig[] jedisPoolConfigs) {
		this.jedisPoolConfigs = jedisPoolConfigs;
	}

	public JedisConnectionFactoryGroupFactoryBean() {
		this(new JedisPoolConfig[] { new JedisPoolConfig() });
	}

	public JedisConnectionFactory[] getJedisConnectionFactorys() {
		return jedisConnectionFactorys;
	}

	public void setJedisConnectionFactorys(JedisConnectionFactory[] jedisConnectionFactorys) {
		this.jedisConnectionFactorys = jedisConnectionFactorys;
	}

	public JedisConnectionFactory[] createJedisConnectionFactorys() throws Exception {
		JedisConnectionFactory[] result = null;
		try {
			int size = this.jedisPoolConfigs.length;
			result = new JedisConnectionFactory[size];
			//
			for (int index = 0; index < size; index++) {
				JedisConnectionFactory jedisConnectionFactory = createJedisConnectionFactory(index,
						this.jedisPoolConfigs[index]);
				result[index] = jedisConnectionFactory;
			}

		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during createJedisConnectionFactorys()").toString(),
					e);
			try {
				result = (JedisConnectionFactory[]) shutdownJedisConnectionFactorys();
			} catch (Exception sie) {
				throw sie;
			}
			throw e;
		}
		return result;
	}

	protected JedisConnectionFactory shutdownJedisConnectionFactory() throws Exception {
		return null;
	}

	/**
	 * 關閉
	 *
	 * @return
	 */
	protected JedisConnectionFactory[] shutdownJedisConnectionFactorys() throws Exception {
		try {
			if (this.jedisConnectionFactorys != null) {
				for (int i = 0; i < this.jedisConnectionFactorys.length; i++) {
					JedisConnectionFactory oldInstance = this.jedisConnectionFactorys[i];
					// oldInstance.close();
					oldInstance.destroy();
					this.jedisConnectionFactorys[i] = null;
				}
				//
				this.jedisConnectionFactorys = null;
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during shutdownJedisConnectionFactorys()").toString(),
					e);
			throw e;
		}
		return this.jedisConnectionFactorys;
	}

	/**
	 * 重啟
	 *
	 * @return
	 */
	protected JedisConnectionFactory[] restartJedisConnectionFactorys() throws Exception {
		try {
			if (this.jedisConnectionFactorys != null) {
				this.jedisConnectionFactorys = shutdownJedisConnectionFactorys();
				this.jedisConnectionFactorys = createJedisConnectionFactorys();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during restartJedisConnectionFactorys()").toString(),
					e);
			throw e;
		}
		return this.jedisConnectionFactorys;
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		if (this.jedisConnectionFactorys != null) {
			LOGGER.info(new StringBuilder().append("Inject from setJedisConnectionFactorys()").toString());
		} else {
			LOGGER.info(new StringBuilder().append("Using createJedisConnectionFactorys()").toString());
			this.jedisConnectionFactorys = createJedisConnectionFactorys();
		}
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		this.jedisConnectionFactorys = shutdownJedisConnectionFactorys();
	}

	@Override
	public JedisConnectionFactory[] getObject() throws Exception {
		return jedisConnectionFactorys;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.jedisConnectionFactorys != null) ? this.jedisConnectionFactorys.getClass()
				: JedisConnectionFactory[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
