package org.openyu.commons.thread;

public class ThreadTest
{

	public static void main(String[] args)
	{
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					System.out.print("T");
				}
			}
		});

		thread.start();
		System.out.println("end");

	}

}
