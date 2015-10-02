package org.openyu.commons.thread;

/**
 * 觸發的佇列,用作監聽,當有新加入的element時,就會呼叫process(e)
 * 
 * 轉發到真正處理的method
 * 
 * 比固定時間的loop更有效率
 */
public interface TriggerQueue<E> extends BaseRunnable {

	/**
	 * 加入元素
	 * 
	 * @param e
	 * @return
	 */
	boolean offer(E e);

	// /**
	// * 處理真正要處理的邏輯
	// *
	// * @param e
	// */
	// void process(E e);
}
