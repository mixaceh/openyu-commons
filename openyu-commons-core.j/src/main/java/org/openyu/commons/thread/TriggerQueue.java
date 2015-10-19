package org.openyu.commons.thread;

/**
 * 觸發的佇列,用作監聽,當有新加入的element時,就會呼叫process(e)
 * 
 * 轉發到真正處理的method
 * 
 * 比固定時間的loop更有效率
 */
public interface TriggerQueue<E> extends BaseRunnableQueue<E> {

//	/**
//	 * 加入元素
//	 * 
//	 * remove to BaseRunnableQueue.offer
//	 * 
//	 * @param e
//	 * @return
//	 */
//	boolean offer(E e);
}
