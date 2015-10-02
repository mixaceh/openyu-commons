package org.openyu.commons.commons.pool;

import static org.junit.Assert.*;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.SoftReferenceObjectPool;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SoftReferenceObjectPoolTest extends BaseTestSupporter {
	
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	private static SoftReferenceObjectPool<Object> pool;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pool = new SoftReferenceObjectPool<Object>(
				new PoolableObjectFactory<Object>() {

					int counter;

					public Object makeObject() throws Exception {
						return String.valueOf(counter++);
					}

					@Override
					public void destroyObject(Object paramT) throws Exception {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean validateObject(Object paramT) {
						return true;
					}

					@Override
					public void activateObject(Object paramT) throws Exception {
						// TODO Auto-generated method stub

					}

					@Override
					public void passivateObject(Object paramT) throws Exception {
						// TODO Auto-generated method stub

					}
				});
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// #issue
	// pool2-2.2 在發生oom時,並沒有清除pool裡的物件
	// 但pool-1.6是有的,所以先用pool-1.6版
	public void outOfMemory() throws Exception {

		Object obj = pool.borrowObject();
		pool.returnObject(obj);
		obj = null;
		assertEquals(1, pool.getNumIdle());

		try {
			// 模擬oom
			mockOutOfMemory();
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			//
			assertEquals(0, pool.getNumIdle());
			pool.borrowObject();
			pool.borrowObject();
			pool.borrowObject();
			pool.borrowObject();
			pool.borrowObject();
			assertEquals(0, pool.getNumIdle());
		}
	}
}
