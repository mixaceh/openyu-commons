package org.openyu.commons.lock;

import java.util.concurrent.locks.Lock;

import org.openyu.commons.model.BaseModel;

/**
 * Distributed Lock
 *
 * 分散式鎖
 */
public interface DistLock extends BaseModel, Lock {

}
