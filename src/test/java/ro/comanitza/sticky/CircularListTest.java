package ro.comanitza.sticky;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CircularListTest {

    @Test
    public void testCircularList() {

        CircularList<Integer> circularList = new CircularList<>();

        for (int i = 0; i < 10; i++) {
            circularList.add(i);
        }

        List<Integer> foundList = circularList.fetchList();

        Assert.assertEquals("Expected value not found", 10, foundList.size());

        circularList.clear();

        foundList = circularList.fetchList();

        Assert.assertTrue("Expected value not found", foundList.isEmpty());


        for (int i = 0; i < 30; i++) {
            circularList.add(i);
        }

        foundList = circularList.fetchList();

        Assert.assertEquals("Expected value not found", circularList.getCapacity(), foundList.size());

        Assert.assertEquals("Expected value not found", 20, foundList.get(0).intValue());
    }
}
