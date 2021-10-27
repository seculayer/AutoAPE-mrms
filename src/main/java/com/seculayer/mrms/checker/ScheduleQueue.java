package com.seculayer.mrms.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class ScheduleQueue {
	private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private ArrayBlockingQueue<Map<String, Object>> queue = new ArrayBlockingQueue<Map<String, Object>>(10000);
	
	public void push(Map<String, Object> data) {
		try {
			queue.put(data);
		} catch (InterruptedException e) {
			logger.error("Queue Push Error!", e);
		}
	}
	
	public Map<String, Object> pop() {
		Map<String, Object> data = null;
		try {
			data = queue.take();
		} catch (InterruptedException e) {
			logger.error("Queue Pop Error!", e);
		}
		return data;
	}

	public synchronized void clear() {
		this.queue.clear();
	}

	public int size() {
		return queue.size();
	}
}
