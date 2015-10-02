package org.openyu.commons.thread;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

public class ArrayBlockingQueueTest {

	@Test
	// offer 不會阻塞
	public void offer() {
		ArrayBlockingQueue<Integer> elements = new ArrayBlockingQueue<Integer>(
				5);
		boolean result = elements.offer(1);
		System.out.println("offer: " + result);
		result = elements.offer(2);
		System.out.println("offer: " + result);
		result = elements.offer(3);
		System.out.println("offer: " + result);
		result = elements.offer(4);
		System.out.println("offer: " + result);
		result = elements.offer(5);
		System.out.println("offer: " + result);
		System.out.println("5...");
		//
		result = elements.offer(6);
		System.out.println("offer: " + result);
		//
		System.out.println("size: " + elements.size());
	}

	@Test
	// poll 不會阻塞
	public void poll() throws Exception {
		ArrayBlockingQueue<Integer> elements = new ArrayBlockingQueue<Integer>(
				5);
		Integer e = elements.poll();
		System.out.println(e);
		//
		System.out.println("size: " + elements.size());

	}

	@Test
	// put 會阻塞
	public void put() throws Exception {
		final ArrayBlockingQueue<Integer> elements = new ArrayBlockingQueue<Integer>(
				5);
		//
		Thread thread = new Thread(new Runnable() {
			public void run() {
				ThreadHelper.sleep(5 * 1000);
				try {
					Integer e = elements.take();
					System.out.println("take: " + e);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		//
		elements.put(1);
		elements.put(2);
		elements.put(3);
		elements.put(4);
		elements.put(5);
		System.out.println("5...");
		//
		elements.put(6);
		System.out.println("6...");
		//
		System.out.println("size: " + elements.size());

	}

	@Test
	// take 會阻塞
	public void take() throws Exception {
		ArrayBlockingQueue<Integer> elements = new ArrayBlockingQueue<Integer>(
				5);
		Integer e = elements.take();
		System.out.println(e);
		//
		System.out.println("size: " + elements.size());

	}

}
