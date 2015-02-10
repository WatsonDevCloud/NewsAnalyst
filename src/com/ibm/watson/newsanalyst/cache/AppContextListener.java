package com.ibm.watson.newsanalyst.cache;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
	
	private long TIMER_PERIOD = 10 * 60 * 1000; // 10 minutes

	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		// Your code here
		System.out.println("NewsAnalyst Listener has been shutdown");

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("News Cache update timer initialized to a period of " + TIMER_PERIOD + "ms");

		TimerTask newsTimer = new NewsTimerTask();

		Timer timer = new Timer();
		timer.schedule(newsTimer, 1000, TIMER_PERIOD);

	}

	class NewsTimerTask extends TimerTask {
		@Override
		public void run() {
			NewsCache.updateCache();
		}
	}

}