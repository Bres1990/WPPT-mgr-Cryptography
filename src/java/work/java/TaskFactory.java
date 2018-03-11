package work.java;

import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Adam Poterałowicz
 */
public class TaskFactory {

    public static LinkedList<Task> exercises = new LinkedList<>();
    public static int capacity = 10;

    public static void main(String[] args) {
        try {
            Lock l = new ReentrantLock();
            Condition start = l.newCondition();
            Condition p1AfterStart = l.newCondition();
            Condition p2Afterp1 = l.newCondition();
            Condition other = l.newCondition();

            ProducerTask p1 = new ProducerTask("p1", l, start, p1AfterStart);
            ProducerTask p2 = new ProducerTask("p2", l, p2Afterp1, other);

            ConsumerTask c1 = new ConsumerTask("c1", l, other, other);
            ConsumerTask c2 = new ConsumerTask("c2", l, other, other);
            ConsumerTask c3 = new ConsumerTask("c3", l, other, other);
            ConsumerTask c4 = new ConsumerTask("c4", l, other, other);

            p1.getThread().start();
            p2.getThread().start();
            c1.getThread().start();
            c2.getThread().start();
            c3.getThread().start();
            c4.getThread().start();

            l.lock();
            try {
                start.signalAll();
            } finally {
                l.unlock();
            }

            // producers finish before consumers
            p1.getThread().join();
            p2.getThread().join();
            c1.getThread().join();
            c2.getThread().join();
            c3.getThread().join();
            c4.getThread().join();

            if (!p1.getThread().isAlive() || !p2.getThread().isAlive()) {
                System.out.println("Producers are dead");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

@Getter
@AllArgsConstructor
class Task {
    String calculation;
}

@Getter
class ConsumerTask {
    String name;
    Thread thread;

    ConsumerTask(String name, Lock lock, Condition toWaitFor, Condition toSignalOn) {
        this.name = name;

        thread = new Thread(new SequencedRunnable(name, lock, toWaitFor, toSignalOn, this, null));
    }

    void consume() throws InterruptedException, ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        while (true) {
            synchronized (this) {
                if (TaskFactory.exercises.size() == 0) {
                    System.out.println(name + ": No tasks left, waiting");
                    wait();
                }

                if (!TaskFactory.exercises.isEmpty()) {
                    Task task = TaskFactory.exercises.removeFirst();
                    System.out.println(name + ": Solved " + task.getCalculation() + " = " +  engine.eval(task.getCalculation()));

                    notifyAll();
                    Thread.sleep(1000);
                }
            }
        }
    }
}

@Getter
class ProducerTask {
    private String[] digits = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] operations = {"+", "-", "*", "/"};
    String name;
    Thread thread;

    ProducerTask(String name, Lock lock, Condition toWaitFor, Condition toSignalOn) {
        this.name = name;

        thread = new Thread(new SequencedRunnable(name, lock, toWaitFor, toSignalOn, null, this));
    }

    Task produce() throws InterruptedException{

        while (true) {
            synchronized (this) {
                String expression = "";
                SecureRandom random = new SecureRandom();

                while (TaskFactory.exercises.size() > TaskFactory.capacity/2) {
                    System.out.println(name + ": There's still plenty of exercises on a list, waiting");
                    wait();
                }

                expression += generateNumber(expression, random);
                expression += operations[Math.abs(random.nextInt()) % operations.length];
                expression = generateNumber(expression, random);

                TaskFactory.exercises.add(new Task(expression));
                System.out.println(name + ": Produced task " + expression);

                notifyAll();

                Thread.sleep(1000);
            }
        }

    }

    private String generateNumber(String expression, SecureRandom random) {
        for (int i = 0; i < 4; i++) {
            expression += digits[Math.abs(random.nextInt()) % digits.length];
            while (i == 0 && expression.charAt(i) == '0') {
                expression = expression.substring(0, expression.length()-2);
                expression += digits[Math.abs(random.nextInt()) % digits.length];
            }
        }

        return expression;
    }
}

@AllArgsConstructor
class SequencedRunnable implements Runnable {

    private final String name;
    private final Lock sync;
    private final Condition toWaitFor;
    private final Condition toSignalOn;
    @Nullable
    private ConsumerTask consumerTask;
    @Nullable
    private ProducerTask producerTask;

    @Override
    public void run() {
        sync.lock();
        try {
            if (toWaitFor != null)
                System.out.println(name + " Waiting");
                toWaitFor.await();

            if (toSignalOn != null)
                System.out.println(name + " Signalling");
                toSignalOn.signalAll();

            if (consumerTask != null) {
                consumerTask.consume();
            } else {
                producerTask.produce();
            }
        } catch (InterruptedException | ScriptException e) {
            e.printStackTrace();
        } finally {
            sync.unlock();
        }
    }
}
