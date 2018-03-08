package work.java;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.security.SecureRandom;
import java.util.LinkedList;

/**
 * Created by Adam on 2018-03-06.
 */
public class TaskFactory {

    public static LinkedList<Task> exercises = new LinkedList<>();
    public static int capacity = 10;

    public static void main(String[] args) {
        try {
            ProducerTask p1 = new ProducerTask("p1");
            ProducerTask p2 = new ProducerTask("p2");

            ConsumerTask c1 = new ConsumerTask("c1");
            ConsumerTask c2 = new ConsumerTask("c2");
            ConsumerTask c3 = new ConsumerTask("c3");
            ConsumerTask c4 = new ConsumerTask("c4");

            p1.getThread().start();
            p2.getThread().start();
            c1.getThread().start();
            c2.getThread().start();
            c3.getThread().start();
            c4.getThread().start();

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

    ConsumerTask(String name) {
        this.name = name;

        thread = new Thread(() -> {
            try {
                consume();
            } catch(InterruptedException | ScriptException e) {
                e.printStackTrace();
            }
        });
    }

    void consume() throws InterruptedException, ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        while (true) {
            synchronized (this) {
                while (TaskFactory.exercises.size() == 0) {
                    wait();
                }

                if (!TaskFactory.exercises.isEmpty()) {
                    Task task = TaskFactory.exercises.removeFirst();
                    System.out.println(name + ": Solved " + task.getCalculation() + " = " +  engine.eval(task.getCalculation()));

                    notify();
                }

                Thread.sleep(1000);
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

    ProducerTask(String name) {
        this.name = name;

        thread = new Thread(() -> {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    Task produce() throws InterruptedException{

        while (true) {
            synchronized (this) {
                String expression = "";
                SecureRandom random = new SecureRandom();

                while (TaskFactory.exercises.size() == TaskFactory.capacity) {
                    System.out.println(TaskFactory.exercises.size() + " exercises left, waiting");
                    wait();
                }

                expression += generateNumber(expression, random);
                expression += operations[Math.abs(random.nextInt()) % operations.length];
                expression = generateNumber(expression, random);

                TaskFactory.exercises.add(new Task(expression));
                System.out.println(name + ": Produced task " + expression);

                notify();

                Thread.sleep(100);
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
