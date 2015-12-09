package org.openyu.commons.thread;

/**
 * 觸發的佇列,用作監聽,當有新加入的element時,就會調用doExecute(E e)
 * 
 * 轉發到真正處理的method
 * 
 * 比固定時間的輪循佇列更有效率
 */
public interface TriggerQueue<E> extends BaseRunnableQueue<E> {

}
