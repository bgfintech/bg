package weixin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.caucho.hessian.client.HessianProxyFactory;

public class StressTest {

	private final static String url1 = "XXXXXXX";

	public static void main(String args[]) throws Throwable{  
          
        HessianProxyFactory hpf = new HessianProxyFactory();  
        hpf.setChunkedPost(false);  
        //final UserManagerRemote userManagerRemote = (UserManagerRemote)hpf.create(url1);  
  
// 创建收集信息list  
        final List<StopWatch> list = new ArrayList<StopWatch>(10000);  
//线程数量  
        int count = 150;      
        final long start1 = System.currentTimeMillis();  
//创建循环栅栏，第一版我是创建了一个监控线程  
        final CyclicBarrier barrier = new CyclicBarrier(count, new Runnable(){  
                @Override  
                public void run() {  
                    long end1 = System.currentTimeMillis();  
//分析收集到的信息  
                    analyseStopWatch(list, (end1-start1));  
                }  
            }  
        );  
        for(int i=0; i<count; i++){  
            Thread t = new Thread(  
                new Runnable(){  
                    @Override  
                    public void run() {  
                        for(int j=0; j<100; j++){  
//创建收集信息对象  
                            StopWatch sw = new StopWatch();  
                            long startTime = System.currentTimeMillis();  
                            sw.setStartTime(startTime);  
                            try {  
                                //userManagerRemote.get(****L);  
                                sw.setStatus(true);  
                            } catch (Throwable e) {  
                                sw.setStatus(false);  
                            }  
                            long endTime = System.currentTimeMillis();  
                            sw.setEndTime(endTime);  
                            sw.setElapsedTime(endTime-startTime);  
//将收集信息对象添加到list中  
                            list.add(sw);  
                        }  
                        try {  
                            barrier.await();  
                        } catch (InterruptedException e) {  
                            e.printStackTrace();  
                        } catch (BrokenBarrierException e) {  
                            e.printStackTrace();  
                        }  
                    }  
                      
                }  
            );  
            if(i < 29){  
                t.setDaemon(true);  
            }  
            t.start();  
        }  
          
    }	private static void analyseStopWatch(List<StopWatch> stopWatchs,
			long totalSpend) {

		System.out.println("size = " + stopWatchs.size());
		Collections.sort(stopWatchs, new Comparator<StopWatch>() {
			@Override
			public int compare(StopWatch o1, StopWatch o2) {
				Long elapsedTime1 = o1.getElapsedTime();
				Long elapsedTime2 = o2.getElapsedTime();
				return elapsedTime1.compareTo(elapsedTime2);
			}
		});
		int size = stopWatchs.size();
		long min = 0;
		for (StopWatch sw : stopWatchs) {
			if (sw.getElapsedTime() > 0) {
				min = sw.getElapsedTime();
				break;
			}
		}
		System.out.println("spend time min = " + min + "MS   |   max = "
				+ stopWatchs.get(size - 1).getElapsedTime() + "MS");
		int failCount = 0;
		long spendTime = 0L;
		for (StopWatch sw : stopWatchs) {
			spendTime += sw.getElapsedTime();
			if (!sw.isStatus()) {
				failCount += 1;
			}
		}
		System.out.println("total spend time = " + totalSpend + "MS");
		System.out.println("total request count = " + size);
		double d1 = totalSpend;
		double d2 = size;
		double averageST = d1 / d2;
		System.out.println("average spend time = " + spendTime / size + "MS");
		System.out.println("Transaction Per Second = " + (1000 / averageST));
		System.out.println("total fail count = " + failCount);
	}

}