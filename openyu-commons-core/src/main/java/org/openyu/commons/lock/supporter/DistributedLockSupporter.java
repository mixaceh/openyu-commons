package org.openyu.commons.lock.supporter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.lock.DistributedLock;
import org.openyu.commons.model.supporter.BaseModelSupporter;

public abstract class DistributedLockSupporter extends BaseModelSupporter implements DistributedLock {

	private static final long serialVersionUID = -160364842817139388L;

	protected transient final ReentrantLock lock = new ReentrantLock();

	public DistributedLockSupporter() {
	}

	@Override
	public void lock() {
		this.lock.lock();
		if (this.lock.getHoldCount() > 1) {
			return;
		}
		//
		boolean locked = false;
		try {
			doLock();
			locked = true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (!locked) {
				this.lock.unlock();
			}
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		this.lock.lockInterruptibly();
		if (this.lock.getHoldCount() > 1) {
			return;
		}
		//
		boolean locked = false;
		try {
			doLockInterruptibly();
			locked = true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (!locked) {
				this.lock.unlock();
			}
		}
	}

	@Override
	public boolean tryLock() {
		if (!this.lock.tryLock()) {
			return false;
		}
		if (this.lock.getHoldCount() > 1) {
			return true;
		}
		//
		boolean locked = false;
		try {
			locked = doTryLock();
		} catch (Exception e) {
			throw e;
		} finally {
			if (!locked) {
				this.lock.unlock();
			}
		}
		return locked;
	}

	@Override
	public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
		final long mark = System.nanoTime();
		if (!this.lock.tryLock(timeout, unit)) {
			return false;
		}
		if (this.lock.getHoldCount() > 1) {
			return true;
		}
		//
		boolean succeed = false;
		try {
			timeout = TimeUnit.NANOSECONDS.convert(timeout, unit) - (System.nanoTime() - mark);
			if (timeout >= 0) {
				succeed = doTryLock(timeout, TimeUnit.NANOSECONDS);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (!succeed) {
				this.lock.unlock();
			}
		}
		return succeed;
	}

	@Override
	public void unlock() {
		if (!this.lock.isHeldByCurrentThread()) {
			return;
		}
		if (this.lock.getHoldCount() > 1) {
			return;
		}
		//
		try {
			doUnlock();
		} catch (Exception e) {
			throw e;
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}

	protected abstract void doLock();

	protected abstract void doLockInterruptibly() throws InterruptedException;

	protected abstract boolean doTryLock();

	protected abstract boolean doTryLock(long timeout, TimeUnit unit) throws InterruptedException;

	protected abstract void doUnlock();

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("lock", lock);
		return builder.toString();
	}
}
