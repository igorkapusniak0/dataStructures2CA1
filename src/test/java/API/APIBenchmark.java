package API;

import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.openjdk.jmh.annotations.*;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(iterations=3)
@Warmup(iterations=2)
@Fork(value=1)
@State(Scope.Benchmark)
public class APIBenchmark {
    InputStream is = getClass().getResourceAsStream("/pillImage.png");
    private Image image = new Image(is,500,500,false,false);
    private Image blackAndWhite = API.convertToBlackAndWhite(image,Color.GREEN,0.2);
    int[] pixels = API.findWhite(blackAndWhite);



    @Benchmark
    public void benchmarkProcessImage(){
        Slider hue = new Slider();
        Slider brightness = new Slider();
        Slider saturation = new Slider();
        hue.setValue(100);
        brightness.setValue(1);
        saturation.setValue(1);
        API.processedImage(image,hue,brightness,saturation);
    }

    @Benchmark
    public void benchmarkConvertToBlackAndWhite() {
        Color pixelColour1 = Color.GREEN;
        Color pixelColour2 = Color.BLUE;
        double tolerance1 = 0.2;
        double tolerance2 = 0.2;
        blackAndWhite = API.convertToBlackAndWhite(image, pixelColour1, pixelColour2, tolerance1, tolerance2);
    }

    @Benchmark
    public void benchmarkFindWhite(){
        pixels = API.findWhite(blackAndWhite);
    }

    @Benchmark
    public void benchmarkGetSubImage(){
        API.getSubImage(image,25,25,100,100);
    }

    @Benchmark
    public void benchmarkUnionBySize(){
        for (int i = 0; i< pixels.length;i++){
            int right = i+1;
            if (i < 0 && right < 0 && right < pixels.length){
                API.unionBySize(pixels,i,right);
            }
        }
    }

    @Benchmark
    public void benchmarkGetSets(){
        API.getSets(pixels);
    }

    @Benchmark
    public void benchmarkCountUniqueSets(){
        API.countUniqueSets(pixels);
    }

    @Benchmark
    public void benchmarkRebuildImage(){
        API.rebuildImage(pixels);
    }

    @Benchmark
    public void benchmarkNoiseFilter(){
        API.noiseFilter(pixels,100);
    }


    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
                .include(APIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
