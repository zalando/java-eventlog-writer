package de.zalando.zomcat.jobs.management;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import de.zalando.zomcat.jobs.management.impl.QuartzThreadPoolExecutorAdapter;

import junit.framework.Assert;

/**
 * Unit tests.
 *
 * @author  pribeiro
 */
public class QuartzThreadPoolExecutorAdapterTest {

    @Test
    public void testDefaultSettings() throws Exception {

        QuartzThreadPoolExecutorAdapter pool = new QuartzThreadPoolExecutorAdapter();
        try {
            pool.initialize();
        } finally {
            pool.shutdown(false);
        }
    }

    @Test(timeout = 60000)
    public void testRunJob() throws Exception {

        final QuartzThreadPoolExecutorAdapter pool = new QuartzThreadPoolExecutorAdapter();
        pool.setCorePoolSize(0);
        pool.setMaximumPoolSize(2);
        pool.setKeepAliveTime(0);
        pool.initialize();

        try {
            Assert.assertEquals(2, pool.blockForAvailableThreads());

            final CountDownLatch ran = new CountDownLatch(1);
            Assert.assertTrue(pool.runInThread(new Runnable() {

                        @Override
                        public void run() {
                            ran.countDown();
                        }
                    }));

            // wait for execution
            ran.await();

            Assert.assertEquals(0, ran.getCount());
        } finally {
            pool.shutdown(true);
        }
    }

    @Test(timeout = 60000)
    public void testRunMaxJobs() throws Exception {
        final QuartzThreadPoolExecutorAdapter pool = new QuartzThreadPoolExecutorAdapter();
        pool.setCorePoolSize(0);
        pool.setMaximumPoolSize(2);
        pool.setKeepAliveTime(0);
        pool.initialize();

        try {
            WaitJob job1 = new WaitJob();
            WaitJob job2 = new WaitJob();
            WaitJob job3 = new WaitJob();

            Assert.assertEquals(2, pool.blockForAvailableThreads());
            Assert.assertTrue(pool.runInThread(job1));

            Assert.assertEquals(1, pool.blockForAvailableThreads());
            Assert.assertTrue(pool.runInThread(job2));

            // thread pool is full. Unlock job1 and run other job
            job1.unlock();

            Assert.assertEquals(1, pool.blockForAvailableThreads());
            Assert.assertTrue(pool.runInThread(job3));

            // unlock the other jobs
            job2.unlock();
            job3.unlock();

        } finally {
            pool.shutdown(true);
        }
    }

    @Test(timeout = 60000)
    public void testBlockForAvailableThreads() throws Exception {
        final QuartzThreadPoolExecutorAdapter pool = new QuartzThreadPoolExecutorAdapter();
        pool.setCorePoolSize(0);
        pool.setMaximumPoolSize(1);
        pool.setKeepAliveTime(0);
        pool.initialize();

        final Semaphore semaphore = new Semaphore(0);
        final AtomicInteger counter = new AtomicInteger();

        try {
            Assert.assertEquals(1, pool.blockForAvailableThreads());
            Assert.assertTrue(pool.runInThread(new Runnable() {

                        @Override
                        public void run() {
                            semaphore.release();

                            try {

                                // Seems like a code smell, because we can't guarantee that method
                                // blockForAvailableThreads() will be called while this method is being executed
                                // (sleeping).
                                // Still, if there is something wrong on the thread pool this test will eventually fail
                                Thread.sleep(5000);
                                counter.incrementAndGet();
                            } catch (InterruptedException e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                    }));

            // wait for the job
            semaphore.acquire();

            Assert.assertEquals(1, pool.blockForAvailableThreads());
            Assert.assertEquals(1, counter.get());

            Assert.assertTrue(pool.runInThread(new Runnable() {

                        @Override
                        public void run() {
                            counter.incrementAndGet();
                            semaphore.release();
                        }
                    }));

            semaphore.acquire();

            // check if job ran
            Assert.assertEquals(2, counter.get());
        } finally {
            pool.shutdown(true);
        }
    }

    @Test(timeout = 60000)
    public void testWaitShutdownWithTaskRunning() throws Exception {
        shutdownWithTaskRunning(true);
    }

    @Test(timeout = 60000)
    public void testForceShutdownWithTaskRunning() throws Exception {
        shutdownWithTaskRunning(false);
    }

    private void shutdownWithTaskRunning(final boolean waitForJobsToComplete) throws Exception {
        final QuartzThreadPoolExecutorAdapter pool = new QuartzThreadPoolExecutorAdapter();
        pool.setCorePoolSize(0);
        pool.setMaximumPoolSize(1);
        pool.setKeepAliveTime(0);
        pool.setShutdownTimeout(1);
        pool.initialize();

        try {
            final CountDownLatch mainLock = new CountDownLatch(1);
            final CountDownLatch jobLock = new CountDownLatch(1);

            Assert.assertEquals(1, pool.blockForAvailableThreads());
            Assert.assertTrue(pool.runInThread(new Runnable() {

                        @Override
                        public void run() {

                            // unlock main thread
                            mainLock.countDown();
                            try {

                                // wait forever
                                jobLock.await();
                                Assert.fail();
                            } catch (InterruptedException e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                    }));

            // certify that the task is running
            mainLock.await();
        } finally {
            pool.shutdown(waitForJobsToComplete);
        }
    }

    private static final class WaitJob implements Runnable {

        private final CountDownLatch lock = new CountDownLatch(1);

        @Override
        public void run() {
            try {
                lock.await();
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
        }

        public void unlock() {
            lock.countDown();
        }
    }

}
