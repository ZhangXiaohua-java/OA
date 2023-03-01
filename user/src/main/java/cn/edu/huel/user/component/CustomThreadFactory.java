package cn.edu.huel.user.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 张晓华
 * @date 2023-3-1
 */
@Component
public class CustomThreadFactory implements ThreadFactory {

	AtomicInteger aqs = new AtomicInteger(1);

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setName("oa-" + aqs.getAndIncrement());
		return thread;
	}


}
