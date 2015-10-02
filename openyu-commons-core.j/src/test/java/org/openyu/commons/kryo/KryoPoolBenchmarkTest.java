package org.openyu.commons.kryo;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
//kryo-3.0.0
//import com.esotericsoftware.kryo.pool.KryoFactory;
//import com.esotericsoftware.kryo.pool.KryoPool;

import org.openyu.commons.hbase.ex.HzException;
import org.openyu.commons.kryo.FieldSerializerTest.DefaultTypes;
import org.openyu.commons.lang.NumberHelper;

public class KryoPoolBenchmarkTest {
	private static final int WARMUP_ITERATIONS = 10000;

	/** Number of runs. */
	private static final int RUN_CNT = 10;

	/**
	 * Number of iterations. Set it to something rather big for obtaining
	 * meaningful results
	 */
	// private static final int ITER_CNT = 200000;
	private static final int ITER_CNT = 10000;
	private static final int SLEEP_BETWEEN_RUNS = 100;

	// not private to prevent the synthetic accessor method
//	private static KryoFactory factory = new KryoFactory() {
//		@Override
//		public Kryo create() {
//			Kryo kryo = new Kryo();
//			kryo.register(DefaultTypes.class);
//			kryo.register(SampleObject.class);
//			return kryo;
//		}
//	};

//	private static KryoPool.Builder builder = new KryoPool.Builder(factory);
//	private static KryoPool pool = builder.build();

	private static ThreadLocal<Kryo> threadSafeKryos = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			return kryo;
		};
	};

	private static ThreadLocal<Kryo> kryoHolder = new ThreadLocal<Kryo>() {
		// KryoPool.Builder builder = new KryoPool.Builder(factory);
		// final KryoPool pool = builder.build();
		//
		// protected Kryo initialValue() {
		// // Kryo kryo = new Kryo();
		// Kryo kryo = pool.borrow();
		// return kryo;
		// };
	};

//	protected static Kryo openKryo() {
//		Kryo result = null;
//		try {
//			result = kryoHolder.get();
//			if (result == null) {
//				result = pool.borrow();
//				//
//				kryoHolder.set(result);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//
//	}

//	protected static void closeKryo() {
//		try {
//			Kryo kryo = kryoHolder.get();
//			if (kryo != null) {
//				kryoHolder.remove();
//				pool.release(kryo);
//			}
//		} catch (Exception ex) {
//			throw new HzException("Cannot close HzSession, general error", ex);
//		}
//	}

//	@Test
//	public void testWithoutPool() throws Exception {
//		// Warm-up phase: Perform 100000 iterations
//		runWithoutPool(1, WARMUP_ITERATIONS, false);
//		runWithoutPool(RUN_CNT, ITER_CNT, true);
//	}

//	@Test
//	public void testWithPool() throws Exception {
//		KryoPool.Builder builder = new KryoPool.Builder(factory);
//		// Warm-up phase: Perform 100000 iterations
//		runWithPool(builder, 1, WARMUP_ITERATIONS, false);
//		runWithPool(builder, RUN_CNT, ITER_CNT, true);
//	}
//
//	@Test
//	public void testWithPoolWithSoftReferences() throws Exception {
//		KryoPool.Builder builder = new KryoPool.Builder(factory)
//				.softReferences();
//		// Warm-up phase: Perform 100000 iterations
//		runWithPool(builder, 1, WARMUP_ITERATIONS, false);
//		runWithPool(builder, RUN_CNT, ITER_CNT, true);
//	}

	private void run(String description, Runnable runnable, final int runCount,
			final int iterCount, boolean outputResults) throws Exception {
		long avgDur = 0;
		long bestTime = Long.MAX_VALUE;

		for (int i = 0; i < runCount; i++) {
			long start = System.nanoTime();

			for (int j = 0; j < iterCount; j++) {
				runnable.run();
			}

			long dur = System.nanoTime() - start;
			dur = TimeUnit.NANOSECONDS.toMillis(dur);

			if (outputResults)
				System.out.format(">>> %s (run %d): %,d ms\n", description,
						i + 1, dur);
			avgDur += dur;
			bestTime = Math.min(bestTime, dur);
			systemCleanupAfterRun();
		}

		avgDur /= runCount;

		if (outputResults) {
			System.out
					.format("\n>>> %s (average): %,d ms", description, avgDur);
			System.out.format("\n>>> %s (best time): %,d ms\n\n", description,
					bestTime);
		}

	}

//	private void runWithoutPool(final int runCount, final int iterCount,
//			boolean outputResults) throws Exception {
//		run("Without pool", new Runnable() {
//			@Override
//			public void run() {
//				Kryo kryo = factory.create();
//				System.out.println("[" + Thread.currentThread().getId()
//						+ "] kryo = " + kryo);
//			}
//		}, runCount, iterCount, outputResults);
//	}

//	private void runWithPool(final KryoPool.Builder builder,
//			final int runCount, final int iterCount, boolean outputResults)
//			throws Exception {
//		final KryoPool pool = builder.build();
//		run("With pool " + builder.toString(), new Runnable() {
//			@Override
//			public void run() {
//				Kryo kryo = pool.borrow();
//				System.out.println("[" + Thread.currentThread().getId()
//						+ "] kryo = " + kryo);
//				pool.release(kryo);
//
//			}
//		}, runCount, iterCount, outputResults);
//	}

	private void systemCleanupAfterRun() throws InterruptedException {
		System.gc();
		Thread.sleep(SLEEP_BETWEEN_RUNS);
		System.gc();
	}

//	public void mockKryo(KryoPool pool) throws Exception {
//		int count = 1;
//		Kryo result = null;
//		for (int i = 0; i < count; i++) {
//			result = pool.borrow();
//			pool.release(result);
//		}
//		//
//		System.out.println("[" + Thread.currentThread().getName() + "] "
//				+ result);
//		Thread.sleep(3 * 60 * 1000);
//	}
//
//	public void mockThreadSafeKryo(KryoPool pool) throws Exception {
//		int count = 1;
//		Kryo result = null;
//		for (int i = 0; i < count; i++) {
//			result = threadSafeKryos.get();
//		}
//		//
//		System.out.println("[" + Thread.currentThread().getName() + "] "
//				+ result);
//		Thread.sleep(3 * 60 * 1000);
//	}

//	@Test
//	public void mockThreadSafeKryoPool() throws Exception {
//		int count = 1;
//		Kryo result = null;
//		for (int i = 0; i < count; i++) {
//			result = openKryo();
//			//System.out.println(result);
//			Thread.sleep(1 * 1000);
//			closeKryo();
//		}
//		//
//		System.out.println("[" + Thread.currentThread().getName() + "] "
//				+ result);
//		Thread.sleep(3 * 60 * 1000);
//	}
//
//	@Test
//	// #issus: from poll, kryo is not thread safe, must use threadLocal
//	public void mockKryoWithMultiThread() throws Exception {
//		KryoPool.Builder builder = new KryoPool.Builder(factory);
//		final KryoPool pool = builder.build();
//		//
//		for (int i = 0; i < 5; i++) {
//			Thread thread = new Thread(new Runnable() {
//				public void run() {
//					try {
//						// #issue
//						// mockKryo(pool);
//
//						// #fix 1, without pool
//						// mockThreadSafeKryo(pool);
//
//						// #fix 2, with pool
//						mockThreadSafeKryoPool();
//
//					} catch (Exception ex) {
//					}
//				}
//			});
//			thread.setName("T-" + i);
//			thread.start();
//			Thread.sleep(NumberHelper.randomInt(100));
//		}
//		//
//		Thread.sleep(1 * 60 * 60 * 1000);
//	}

	private static class SampleObject {
		private int intVal;
		private float floatVal;
		private Short shortVal;
		private long[] longArr;
		private double[] dblArr;
		private String str;

		public SampleObject() {
		}

		SampleObject(int intVal, float floatVal, Short shortVal,
				long[] longArr, double[] dblArr, String str) {
			this.intVal = intVal;
			this.floatVal = floatVal;
			this.shortVal = shortVal;
			this.longArr = longArr;
			this.dblArr = dblArr;
			this.str = str;
		}

		/** {@inheritDoc} */
		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;

			if (other == null || getClass() != other.getClass())
				return false;

			SampleObject obj = (SampleObject) other;

			return intVal == obj.intVal && floatVal == obj.floatVal
					&& shortVal.equals(obj.shortVal)
					&& Arrays.equals(dblArr, obj.dblArr)
					&& Arrays.equals(longArr, obj.longArr)
					&& (str == null ? obj.str == null : str.equals(obj.str));
		}
	}

}
