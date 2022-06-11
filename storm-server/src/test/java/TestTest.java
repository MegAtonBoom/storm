import org.apache.storm.Thrift;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.junit.Test;


public class TestTest extends junit.framework.TestCase {


    @Test
    public void testArray() throws Exception {
        TopologyBuilder tb=new TopologyBuilder();
        tb.setSpout("1", new TestWordSpout(true), 5);
        assertFalse(false);


    }


}