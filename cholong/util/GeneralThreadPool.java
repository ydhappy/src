package cholong.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lineage.share.GameSetting;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class GeneralThreadPool {
	private static GeneralThreadPool _instance;

	private static final int SCHEDULED_CORE_POOL_SIZE = 10;

	private Executor _executor; 
	private ScheduledExecutorService _scheduler; 
	private ScheduledExecutorService _pcScheduler;
	
	private final int _pcSchedulerPoolSize = 1 + GameSetting.MAX_ONLINE_USERS / 20; 

	public static GeneralThreadPool getInstance() {
		if (_instance == null) {
			_instance = new GeneralThreadPool();
		}
		return _instance;
	}

	private GeneralThreadPool() {
		if (GameSetting.THREAD_P_TYPE_GENERAL == 1) {
			_executor = Executors
					.newFixedThreadPool(GameSetting.THREAD_P_SIZE_GENERAL);
		} else if (GameSetting.THREAD_P_TYPE_GENERAL == 2) {
			_executor = Executors.newCachedThreadPool();
		} else {
			_executor = null;
		}
		int i = 2 + Runtime.getRuntime().availableProcessors() * 4;
		final int instantPoolSize = Math.max(1, i / 3);
		_scheduler = Executors
				.newScheduledThreadPool(SCHEDULED_CORE_POOL_SIZE,
						new PriorityThreadFactory("GerenalSTPool",
								Thread.NORM_PRIORITY));
		_pcScheduler = Executors.newScheduledThreadPool(_pcSchedulerPoolSize,
				new PriorityThreadFactory("PcMonitorSTPool",
						Thread.NORM_PRIORITY));

	}

	public void execute(Runnable r) {
		if (_executor == null) {
			Thread t = new Thread(r);
			t.start();
		} else {
			_executor.execute(r);
		}
	}

	public void execute(Thread t) {
		t.start();
	}

	public void state() {

	}

	public void purge() {
	}

	public ScheduledFuture<?> schedule(Runnable r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r,
			long initialDelay, long period) {
		return _scheduler.scheduleAtFixedRate(r, initialDelay, period,
				TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> scheduleAtFixedRateLong(Runnable r,
			long initialDelay, long period) {
		return _scheduler.scheduleAtFixedRate(r, initialDelay, period,
				TimeUnit.MILLISECONDS);
	}

	private class PriorityThreadFactory implements ThreadFactory {
		private final int _prio;

		private final String _name;

		private final AtomicInteger _threadNumber = new AtomicInteger(1);

		private final ThreadGroup _group;

		public PriorityThreadFactory(String name, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(_name);
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			return t;
		}

		public ThreadGroup getGroup() {
			return _group;
		}
	}

	public final class CRejectedExecutionHandler implements RejectedExecutionHandler{
		@Override

		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (executor.isShutdown())
				return;

			if (r == null)
				return;
			if (Thread.currentThread().getPriority() > Thread.NORM_PRIORITY)
				new Thread(r).start();
			else
				r.run();
		}
	}
	private static final class ThreadPoolExecuteWrapper extends ExecuteWrapper {
		private ThreadPoolExecuteWrapper(Runnable runnable) {
			super(runnable);
		}

		protected long getMaximumRuntimeInMillisecWithoutWarning() {
			return 10000;
		}
	}
}
