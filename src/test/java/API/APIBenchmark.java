package API;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class APIBenchmark {

    private int[] pixels;

    // Setup your test data
    @Setup
    public void setup() {
        pixels = new int[1000]; // Adjust size for your benchmarking needs
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (int) (Math.random() * 2000) - 1000; // Random numbers between -1000 and 1000
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkCountUniqueSets() {
        countUniqueSets(pixels);
    }

    public static int countUniqueSets(int[] pixels) {
        int number = 0;
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] < 0) {
                number++;
            }
        }
        return number;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(APIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
