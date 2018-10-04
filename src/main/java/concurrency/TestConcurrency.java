package concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConcurrency {
	
	static final Logger logger = LoggerFactory.getLogger(TestConcurrency.class);
	private static final int MAX_THREAD_POOL_SIZE = 5;
	
	public static void main(String[] args) throws Exception {
		String[] ucns = new String[20];
		for(int i=0; i<ucns.length; i++) {
			ucns[i] = Integer.toString((100 * i));
		}
		
		logger.info("Starting recon process for {} UCNs with max thread pool size {}...", ucns.length, MAX_THREAD_POOL_SIZE);
		ExecutorService taskExecutor = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
		Map<String, Future<Integer>> tasks = new HashMap<>();
		int counter = 1;
		for(String ucn : ucns) {
			tasks.put(ucn, taskExecutor.submit(new ReconTask(counter++, ucn)));
		}
		taskExecutor.shutdown();
		logger.info("{}", "Task executor will shutdown after pending tasks completed. Will no longer accept new tasks.");

		counter = 0;
		for(String ucn : ucns) {
			counter += tasks.get(ucn).get();
		}
		
		logger.info("All tasks completed. Final count: {}", counter);
		
	}

	static class ReconTask implements Callable<Integer> {
	
		private String ucn;
		private int counter;
		
		public ReconTask(int counter, String ucn) {
			logger.info("{}. Recon task added for UCN: {}", counter, ucn);
			this.ucn = ucn;
			this.counter = counter;
		}
		
		@Override
		public Integer call() throws InterruptedException {
			logger.info("{}. Executing process for UCN: {}", counter, ucn);
			Thread.sleep(Math.round(1000 * Math.random()));
			logger.info("{}. Getting snapshot for UCN: {}", counter, ucn);
			Thread.sleep(Math.round(1000 * Math.random()));
			logger.info("{}. Reconciling for UCN: {}", counter, ucn);
			Thread.sleep(Math.round(1000 * Math.random()));
			logger.info("{}. Process completed successfully for UCN: {}", counter, ucn);
			return Integer.valueOf(1); //Return 1 regrdless of pass/fail
		}
	}
}
