package org.openyu.commons.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import org.openyu.commons.thread.ThreadHelper;

public class CollectionTest
{

	/*
	* @see ArrayList #1
	* @see LinkedList #3
	* @see Vector #2
	*/
	@Test
	public void arrayList()
	{
		List<Integer> list = new ArrayList<Integer>();
		int count = 1000000;
		long beg = System.currentTimeMillis();
		//add
		for (int i = 0; i < count; i++)
		{
			list.add(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 47 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		//get
		beg = System.currentTimeMillis();
		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			list.get(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 11 mills.
		System.out.println("get " + count + " times: " + (end - beg) + " mills. ");

		//iterator
		beg = System.currentTimeMillis();
		for (Iterator<Integer> it = list.iterator(); it.hasNext();)
		{
			it.next();
		}
		end = System.currentTimeMillis();
		//1000000 times: 21 mills.
		System.out.println("it " + count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	public void linkedList()
	{
		List<Integer> list = new LinkedList<Integer>();
		int count = 1000000;
		long beg = System.currentTimeMillis();
		//add
		for (int i = 0; i < count; i++)
		{
			list.add(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		//get
		beg = System.currentTimeMillis();
		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			list.get(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 877812 mills. 超慢,所以當用LinkedList時,不要用get(i)取
		System.out.println("get " + count + " times: " + (end - beg) + " mills. ");

		//iterator
		beg = System.currentTimeMillis();
		for (Iterator<Integer> it = list.iterator(); it.hasNext();)
		{
			it.next();
		}
		end = System.currentTimeMillis();
		//1000000 times: 14 mills.
		System.out.println("it " + count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	//thread safe
	public void vector()
	{
		List<Integer> list = new Vector<Integer>();
		int count = 1000000;
		long beg = System.currentTimeMillis();
		//add
		for (int i = 0; i < count; i++)
		{
			list.add(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 67 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		//get
		beg = System.currentTimeMillis();
		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			list.get(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 46 mills.
		System.out.println("get " + count + " times: " + (end - beg) + " mills. ");

		//iterator
		beg = System.currentTimeMillis();
		for (Iterator<Integer> it = list.iterator(); it.hasNext();)
		{
			it.next();
		}
		end = System.currentTimeMillis();
		//1000000 times: 91 mills.
		System.out.println("it " + count + " times: " + (end - beg) + " mills. ");
	}

	/*
	 * @see java.util.Collection
	 * @see LinkedList
	 * @see PriorityQueue
	 * @see java.util.concurrent.LinkedBlockingQueue
	 * @see java.util.concurrent.BlockingQueue
	 * @see java.util.concurrent.ArrayBlockingQueue
	 * @see java.util.concurrent.LinkedBlockingQueue
	 * @see java.util.concurrent.PriorityBlockingQueue
	 */
	/*
	 不要使用原始的add()和remove()，在Queue中會丟出exception， 
	以offer()和poll()來代替
	
	方法	傳回值	說明
	offer(E o)	boolean	加入物件
	peek()	E	取得物件，若空傳回null
	element()	E	取得物件，若空傳回例外
	poll()	E	取得物件，並移除該物件，若空傳回null
	remove()	E	取得物件，並移除該物件，若空傳回例外
	
	Queue Interface Structure
	Throws exception	Returns special value
	Insert	add(e)		offer(e)
	Remove	remove()	poll()
	Examine	element()	peek()
	*/
	@Test
	public void queueLinkedList()
	{
		Queue<Integer> queue = new LinkedList<Integer>();
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.offer(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		//poll
		beg = System.currentTimeMillis();
		Integer value = null;
		while ((value = queue.poll()) != null)
		{}
		end = System.currentTimeMillis();
		//1000000 times: 14 mills. 
		System.out.println("poll " + count + " times: " + (end - beg) + " mills. ");

		//再加一次,因poll讀取時已經移除元素
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.offer(i);
		}
		end = System.currentTimeMillis();

		//iterator for
		beg = System.currentTimeMillis();
		for (Iterator<Integer> it = queue.iterator(); it.hasNext();)
		{
			value = it.next();
		}
		end = System.currentTimeMillis();
		//1000000 times: 20 mills. 
		System.out.println("it " + count + " times: " + (end - beg) + " mills. ");

		//iterator while
		beg = System.currentTimeMillis();
		Iterator<Integer> it = queue.iterator();
		while (it.hasNext())
		{
			value = it.next();
		}
		end = System.currentTimeMillis();
		//1000000 times: 20 mills. 
		System.out.println("it " + count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	//ConcurrentModificationException
	public void exceptionByVector()
	{
		final List<Integer> queue = new Vector<Integer>();
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.add(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		new Thread(new Runnable()
		{
			public void run()
			{
				int count = 1000000;
				long beg = System.currentTimeMillis();
				for (Integer value : queue)
				{}
				long end = System.currentTimeMillis();
				//1000000 times: ... mills. 掛了,concurrentModificationException
				System.out.println("it " + count + " times: " + (end - beg) + " mills. ");

			}
		}).start();

		//
		count = 1000000;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.add(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		ThreadHelper.sleep(5 * 1000);
	}

	@Test
	//ConcurrentModificationException
	public void exceptionByLinkedList()
	{
		final List<Integer> list = new LinkedList<Integer>();
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			list.add(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		new Thread(new Runnable()
		{
			public void run()
			{
				int count = 1000000;
				long beg = System.currentTimeMillis();
				for (Integer value : list)
				{}
				long end = System.currentTimeMillis();
				//1000000 times: ... mills. 掛了,concurrentModificationException
				System.out.println("it " + count + " times: " + (end - beg) + " mills. ");

			}
		}).start();

		//
		count = 1000000;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			list.add(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 31 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		ThreadHelper.sleep(5 * 1000);
	}

	@Test
	//fix ConcurrentModificationException
	public void exceptionFixByQueue()
	{
		final Queue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.offer(i);
		}
		long end = System.currentTimeMillis();
		//1000000 times: 60 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		new Thread(new Runnable()
		{
			public void run()
			{
				int count = 1000000;
				long beg = System.currentTimeMillis();
				for (Integer value : queue)
				{}
				long end = System.currentTimeMillis();
				//1000000 times: 24 mills.
				System.out.println("it " + count + " times: " + (end - beg) + " mills. ");
			}
		}).start();

		//
		count = 1000000;
		beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			queue.offer(i);
		}
		end = System.currentTimeMillis();
		//1000000 times: 47 mills.
		System.out.println("add " + count + " times: " + (end - beg) + " mills. ");

		ThreadHelper.sleep(5 * 1000);
	}
}
