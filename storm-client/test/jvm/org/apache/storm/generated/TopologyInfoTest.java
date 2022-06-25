package org.apache.storm.generated;

import org.apache.storm.generated.ErrorInfo;
import org.apache.storm.generated.ExecutorSummary;
import org.apache.storm.generated.TopologyInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static org.mockito.Mockito.*;


@RunWith(Parameterized.class)
public class TopologyInfoTest {

    //TestedClass
    private TopologyInfo primaryTI;

    //test parameters
    private TopologyInfo secundaryTI;
    private TopologyInfo toCopyTI;
    private boolean expectedCopyNPE;
    private boolean expectedEqaulsNPE;
    private TopologyType toCopyType;
    private SecondTIType comparisonType;


    private void topologyConfig(TopologyInfo ti){
        ti.set_id("first_id");
        ti.set_name("first_name");
        ti.set_uptime_secs(42);
        ti.set_executors(null);
        ti.set_status("status_ok");
        ti.set_errors(null);
    }

    public TopologyInfoTest(TopologyType toCopy, SecondTIType comparison){

        this.primaryTI = new TopologyInfo();
        //configure the master topologyInfo class
        topologyConfig(this.primaryTI);

        configureDeepCopyParam(toCopy);

        configureEqualsParam(comparison);

    }

    public void configureDeepCopyParam(TopologyType copy){

        this.toCopyTI = new TopologyInfo();
        this.expectedCopyNPE = (copy == TopologyType.NULL);
        this.toCopyType = copy;

        switch(copy){
            case VALID: getValidTI(); break;

            case NOT_VALID: getNotValidTI(false); break;

            default: this.toCopyTI = null; break;
        }
    }

    private void getValidTI(){

        this.toCopyTI.set_id("copied_id");
        this.toCopyTI.set_name("copied_name");
        this.toCopyTI.set_uptime_secs(43);
        this.toCopyTI.set_executors(new ArrayList<>());
        this.toCopyTI.set_status("status_not_ok");
        this.toCopyTI.set_errors(new HashMap<>());

        this.toCopyTI.set_assigned_cpu(2);


    }

    private void getNotValidTI(boolean equals){
        if(!equals) {
            this.toCopyTI.set_id("copied_id");
            this.toCopyTI.set_errors(getInvalidMap());
            this.toCopyTI.set_executors(getInvalidList());
        }
        else{
            this.secundaryTI.set_id("copied_id");
            this.secundaryTI.set_errors(getInvalidMap());
            this.secundaryTI.set_executors(getInvalidList());
        }


    }

    private java.util.Map<java.lang.String, java.util.List<ErrorInfo>> getInvalidMap(){
        java.util.Map<java.lang.String, java.util.List<ErrorInfo>> mockedMap = mock(java.util.HashMap.class);
        when(mockedMap.entrySet()).thenReturn(null);
        when(mockedMap.size()).thenReturn(-5);
        return mockedMap;
    }

    private java.util.List<ExecutorSummary> getInvalidList(){
        java.util.List<ExecutorSummary> mockedMap = mock(java.util.ArrayList.class);
        when(mockedMap.size()).thenReturn(-5);
        return mockedMap;
    }

    public void configureEqualsParam(SecondTIType comparison){

        this.secundaryTI = new TopologyInfo();
        this.comparisonType = comparison;
        this.expectedEqaulsNPE = ( comparison == SecondTIType.NULL);

        switch(comparison){

            case SAME: topologyConfig(this.secundaryTI); break;

            case DIFFERENT: getDifferentTI(); break;

            case NOT_VALID: getNotValidTI(true);

            default: this.secundaryTI = null;
        }
    }

    private void getDifferentTI(){


        this.secundaryTI = new TopologyInfo();
        this.secundaryTI.set_id("different_id");
        this.secundaryTI.set_name("different_name");
        this.secundaryTI.set_uptime_secs(24);
        this.secundaryTI.set_executors(null);
        this.secundaryTI.set_status("alabarda");
        this.secundaryTI.set_errors(null);

    }

    @Test
    public void testDeepCopy(){



        try{
            this.primaryTI = new TopologyInfo(this.toCopyTI);;
            Assert.assertEquals(this.primaryTI.get_id(), this.toCopyTI.get_id());
            Assert.assertEquals(this.primaryTI.get_name(), this.toCopyTI.get_name());
            Assert.assertEquals(this.primaryTI.get_uptime_secs(), this.toCopyTI.get_uptime_secs());
            Assert.assertEquals(this.primaryTI.get_executors(), this.toCopyTI.get_executors());
            Assert.assertEquals(this.primaryTI.get_status(), this.toCopyTI.get_status());
            Assert.assertEquals(this.primaryTI.get_errors(), this.toCopyTI.get_errors());
        }
        catch(NullPointerException npw){

            Assert.assertTrue(this.expectedCopyNPE);

        }
        catch(Exception e){

            Assert.assertEquals( this.toCopyType , TopologyType.NOT_VALID);
        }
        finally{
            this.primaryTI = new TopologyInfo();
            topologyConfig(this.primaryTI);
        }
    }

    @Test
    public void testEquals(){

        this.primaryTI = new TopologyInfo();
        //configure the master topologyInfo class
        topologyConfig(this.primaryTI);

        try {
            boolean areEquals = this.primaryTI.equals(this.secundaryTI);

            if (areEquals) Assert.assertEquals(this.comparisonType, SecondTIType.SAME);

            else Assert.assertNotEquals(this.comparisonType, SecondTIType.SAME);
        }
        catch(NullPointerException npe){
            Assert.assertTrue( this.expectedEqaulsNPE );
        }
        catch(Exception e ){
            //shouldn't fail
            Assert.fail();
        }

    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //deepcopy param        equals param
                {TopologyType.VALID,    SecondTIType.SAME},
                {TopologyType.NOT_VALID,    SecondTIType.DIFFERENT},
                {TopologyType.NULL,    SecondTIType.NULL},
                {TopologyType.NULL,    SecondTIType.NOT_VALID}

        });
    }



    private enum TopologyType{
        VALID,
        NOT_VALID,
        NULL,
    }

    private enum SecondTIType{

        SAME,
        DIFFERENT,
        NOT_VALID,
        NULL
    }

}
