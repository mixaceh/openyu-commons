package org.openyu.commons.thrift;

import static org.junit.Assert.*;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;

public class CommonsCoreServiceClientTest extends BaseTestSupporter
{

//	@Test
//	public void mockBooleanHelper_toBoolean()
//	{
//		try
//		{
////			TTransport transport = new TSocket("127.0.0.1", 9090);
////			TProtocol protocol = new TBinaryProtocol(transport);
//
//			TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 9090));
//			TProtocol protocol = new TCompactProtocol(transport);
//			CommonsCoreService.Client client = new CommonsCoreService.Client(protocol);
//			transport.open();
//			//
//			boolean result = false;
//			int count = 5;
//			long beg = System.currentTimeMillis();
//			for (int i = 0; i < count; i++)
//			{
//				result = client.booleanHelper_toBoolean("T", false);
//			}
//			long end = System.currentTimeMillis();
//			//System.out.println(count + " times: " + (end - beg) + " mills. ");
//			//
//			System.out.println("[" + Thread.currentThread().getName() + "] " + result);
//			ThreadHelper.sleep(5 * 60 * 1000);
//			transport.close();
//		}
//		catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//	}
//
//	@Test
//	public void mockBooleanHelper_toBoolean2() throws Exception
//	{
//		//127.0.0.1 blocking: 34,54,68
//		//127.0.0.1 nonblocking: 180,250
//		for (int i = 0; i < 500; i++)
//		{
//			Thread thread = new Thread(new Runnable()
//			{
//				public void run()
//				{
//					try
//					{
//						mockBooleanHelper_toBoolean();
//					}
//					catch (Exception ex)
//					{
//						ex.printStackTrace();
//					}
//				}
//			});
//			thread.setName("T-" + i);
//			thread.start();
//			//
//			ThreadHelper.sleep(randomInt(100));
//		}
//		//
//		ThreadHelper.sleep(5 * 60 * 1000);
//	}

}
