import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static final Map<Integer,Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int amountOfThreads = 1000;
        String letters = "RLRFR";
        int lengthLetter = 100;

        ExecutorService executor = Executors.newFixedThreadPool(amountOfThreads);

        for (int i = 0; i < amountOfThreads; i++) {
                Callable<Integer> callable = () -> (int)countR(generateRoute(letters, lengthLetter));
                Future<Integer> future = executor.submit(callable);
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(future.get())) {
                    sizeToFreq.put(future.get(), sizeToFreq.get(future.get()) + 1);
                } else sizeToFreq.put(future.get(), 1);
            }
        }
        executor.shutdown();


        SortedMap<Integer,Integer> sortedSizeToFreq = new TreeMap<>(Comparator.reverseOrder());
        sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed())
                .forEach(e -> sortedSizeToFreq.put(e.getValue(), e.getKey()));

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)%nДругие значения:%n",
                sortedSizeToFreq.get(sortedSizeToFreq.firstKey()), sortedSizeToFreq.firstKey());

        for(Map.Entry<Integer, Integer> entry : sortedSizeToFreq.entrySet()){
            if(!Objects.equals(entry.getKey(), sortedSizeToFreq.firstKey())){
                System.out.printf("- %d (%d раз)%n", entry.getValue(), entry.getKey());
            }
        }

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static long countR(String route){
        return route
                .chars()
                .filter(c -> c == 'R')
                .count();
    }
}
