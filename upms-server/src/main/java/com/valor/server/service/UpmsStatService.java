package com.valor.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UpmsStatService {

	private final Logger LOG = LoggerFactory.getLogger(UpmsStatService.class);

	private Map<String, AdStatModel> statisticsMap = new ConcurrentHashMap<String, AdStatModel>();

	@PostConstruct
	void init() throws Exception {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					print();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public void put(String api, long time) {
		AdStatModel stati = statisticsMap.get(api);
		if (stati == null) {
			statisticsMap.put(api, new AdStatModel(api, time, time, time, 1, time));
		} else {
			stati.setCount(stati.getCount() + 1);
			if (stati.getMinTime() > time)
				stati.setMinTime(time);
			if (stati.getMaxTime() < time)
				stati.setMaxTime(time);
			stati.setTotalTime(stati.getTotalTime() + time);
			stati.setAvgTime(stati.getTotalTime() / stati.getCount());
			statisticsMap.put(api, stati);
		}
	}

	private void print() {
		statisticsMap.values().forEach(statistic -> {
			AdStatModel stati = statisticsMap.remove(statistic.getApi());
			if (stati == null)
				return;
			LOG.info("--------------------------------------------------------------------");
			LOG.info("Adserver stat: {} : Count {},minTime {},maxTime {},avgTime{}", stati.getApi(), stati.getCount(),
					stati.getMinTime(), stati.getMaxTime(), stati.getAvgTime());
			LOG.info("-------------------------------------------------------------------");
		});
	}

	class AdStatModel {

		private String api;
		private long minTime;
		private long maxTime;
		private long totalTime;
		private long avgTime;
		private long count;

		public AdStatModel(String api, long minTime, long maxTime, long avgTime, long count, long totalTime) {
			super();
			this.api = api;
			this.minTime = minTime;
			this.maxTime = maxTime;
			this.avgTime = avgTime;
			this.totalTime = totalTime;
			this.count = count;
		}

		public String getApi() {
			return api;
		}

		public void setApi(String api) {
			this.api = api;
		}

		public long getMinTime() {
			return minTime;
		}

		public void setMinTime(long minTime) {
			this.minTime = minTime;
		}

		public long getMaxTime() {
			return maxTime;
		}

		public void setMaxTime(long maxTime) {
			this.maxTime = maxTime;
		}

		public long getAvgTime() {
			return avgTime;
		}

		public void setAvgTime(long avgTime) {
			this.avgTime = avgTime;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public long getTotalTime() {
			return totalTime;
		}

		public void setTotalTime(long totalTime) {
			this.totalTime = totalTime;
		}

	}

}
