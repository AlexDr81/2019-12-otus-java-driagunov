package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGc {
    public static List<String> nameGC;
    public static Map<String, Integer> countMapGC;
    public static Map<String, Long> durationMapGC;
    public static void main( String... args ) throws Exception {

        System.out.println( "Starting pid: " + ManagementFactory.getRuntimeMXBean().getName() );
        System.out.println("availableProcessors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory()/1024/1024+"mb");
        System.out.println("totalMemory: " + Runtime.getRuntime().totalMemory()/1024/1024+"mb");
        System.out.println("freeMemory: " + Runtime.getRuntime().freeMemory()/1024/1024+"mb");

        nameGC = new ArrayList<>();
        countMapGC = new HashMap<>();
        durationMapGC = new HashMap<>();

        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        List<Integer> list = new ArrayList<>();

        int size = 1_000_000;
        int loopCounter = 1200;

        Benchmark mbean = new Benchmark(size, loopCounter, list);
        mbean.run();

        if (countMapGC.size() != 0){
            for (String name: nameGC){
                System.out.println("Name GC: " + name + " - number of starts: " + countMapGC.get(name) + " sum duration: " + durationMapGC.get(name));
            }
        }

        System.out.println( "Total time:" + ( System.currentTimeMillis() - beginTime ) / 1000.0 + "s");
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for ( GarbageCollectorMXBean gcbean : gcbeans ) {
            System.out.println( "GC name:" + gcbean.getName() );
            nameGC.add(gcbean.getName());
            countMapGC.put(gcbean.getName(),0);
            durationMapGC.put(gcbean.getName(), 0L);
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback ) -> {
                if ( notification.getType().equals( GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION ) ) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from( (CompositeData) notification.getUserData() );
                    String gcName = info.getGcName();
                    //String gcAction = info.getGcAction();
                    //String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    int countGC = countMapGC.get(gcName);
                    countGC++;
                    countMapGC.put( gcName,countGC );
                    float cleaningTime = countGC / (startTime/1000.0F/60.0F);

                    durationMapGC.put( gcName,durationMapGC.get(gcName) + duration );
                    System.out.println( "start:" + startTime/1000.0 + "s Name:" + gcName + "(" + countGC + ") cleaning time: "
                            + cleaningTime + " 1/min SumDuration " + durationMapGC.get(gcName) / 1000.0 + "s");
                }
            };
            emitter.addNotificationListener( listener, null, null );
        }
    }
}
