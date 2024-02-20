package API;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class APITest {

    @Test
    void find() {
        int[] testArray = {15, 11, 11, 2, 8, 13, 2, 2, 11, 11, 11, -11, 1, 8};
        int expectedResult1 = 11;
        boolean testPassed1 = (API.find(testArray, 12) == expectedResult1);
        assertTrue(testPassed1);

        int expectedResult2 = 0;
        boolean testPassed2 = (API.find(testArray, 0) == expectedResult2);
        assertTrue(testPassed2);

        int expectedResult3 = 11;
        boolean testPassed3 = (API.find(testArray, 11) == expectedResult3);
        assertTrue(testPassed3);
    }

    @Test
    void unionBySize() {
        int[] testArray = {-1, -1, -1, -1, -1};

        API.unionBySize(testArray, 0, 1);

        int root0 = API.find(testArray, 0);
        int root1 = API.find(testArray, 1);
        assertEquals(root0, root1);

        API.unionBySize(testArray, 0, 2);

        int root2 = API.find(testArray, 2);
        assertEquals(root1, root2);

        int expectedSize = -3;
        int setSize = testArray[root0];
        assertEquals(expectedSize, setSize);
    }


    @Test
    void noiseFilter() {
        int[] testArray = {-12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, -1, 19, -1, 19, -1};
        int[] correctArray = {-12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 19, 19, 19, 19, 19};
        testArray = API.noiseFilter(testArray, 2);
        for (int i = 0; i < testArray.length; i++) {
            assertEquals(testArray[i], correctArray[i]);
        }

    }

    @Test
    void countUniqueSets() {
        int[] testArray = {-1, 2, -1, -1, -1};
        int number = API.countUniqueSets(testArray);
        assertEquals(number, 4);
    }

    @Test
    void getSets() {
        int[] pixels = {-12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, -1, 19, -1, 19, -1};

        HashMap<Integer, LinkedList<Integer>> sets = API.getSets(pixels);

        assertNotNull(sets);
        assertEquals(4, sets.size());

        LinkedList<Integer> expectedSet1 = new LinkedList<>(Arrays.asList(0,0,1,2,3,4,5,6,7,8,9,10,11));
        LinkedList<Integer> expectedSet2 = new LinkedList<>(Arrays.asList(13,13));
        LinkedList<Integer> expectedSet3 = new LinkedList<>(Arrays.asList(15,15));
        LinkedList<Integer> expectedSet4 = new LinkedList<>(Arrays.asList(17,17));

        assertTrue(sets.containsKey(0));

        LinkedList<Integer> set1 = sets.get(0);
        LinkedList<Integer> set2 = sets.get(13);
        LinkedList<Integer> set3 = sets.get(15);
        LinkedList<Integer> set4 = sets.get(17);

        assertEquals(expectedSet1, set1);
        assertEquals(expectedSet2, set2);
        assertEquals(expectedSet3, set3);
        assertEquals(expectedSet4, set4);
    }
}