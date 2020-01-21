package delegate;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class InstanceExecutor implements Runnable {

    private Thread thread;
    private Constructor<?> constructor;
    private Map<String, Object> variables;
    private CountDownLatch latch;

    public InstanceExecutor(Map<String, Object> variables, Constructor<?> constructor, CountDownLatch latch){
        this.constructor = constructor;
        this.variables = variables;
        this.latch = latch;
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {

            try{
                this.constructor.newInstance(variables);
                latch.countDown();
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
}
