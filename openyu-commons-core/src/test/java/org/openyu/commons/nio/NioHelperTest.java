package org.openyu.commons.nio;

import java.nio.channels.SelectionKey;

import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class NioHelperTest extends BaseTestSupporter {

	@Test
	public void interestOps() {
		System.out.println("OP_READ: " + SelectionKey.OP_READ); // 1
		System.out.println("OP_WRITE: " + SelectionKey.OP_WRITE);// 4
		System.out.println("OP_CONNECT: " + SelectionKey.OP_CONNECT);// 8
		System.out.println("OP_ACCEPT: " + SelectionKey.OP_ACCEPT);// 16
		//
		int ops = 0;
		// System.out.println(ops & SelectionKey.OP_READ);
		if ((ops & SelectionKey.OP_READ) == 0) {
			ops = ops | SelectionKey.OP_READ;
			System.out.println("ops: " + ops);// 1, OP_READ
		}
		//
		if ((ops & SelectionKey.OP_WRITE) == 0) {
			ops = ops | SelectionKey.OP_WRITE;
			System.out.println("ops: " + ops);// 5,OP_READ+OP_WRITE
		}
		//
		if ((ops & SelectionKey.OP_CONNECT) == 0) {
			ops = ops | SelectionKey.OP_CONNECT;
			System.out.println("ops: " + ops);// 13,OP_READ+OP_WRITE+OP_CONNECT
		}
		//
		if ((ops & SelectionKey.OP_ACCEPT) == 0) {
			ops = ops | SelectionKey.OP_ACCEPT;
			System.out.println("ops: " + ops);// 29,OP_READ+OP_WRITE+OP_CONNECT+OP_ACCEPT
		}
	}
}
