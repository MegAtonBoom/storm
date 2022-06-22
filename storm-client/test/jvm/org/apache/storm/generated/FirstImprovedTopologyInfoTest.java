package org.apache.storm.generated;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static org.mockito.Mockito.*;


@RunWith(Parameterized.class)
public class FirstImprovedTopologyInfoTest {

    //TestedClass
    private TopologyInfo primaryTI;

    //test parameters
    private TopologyInfo secundaryTI;
    private TopologyInfo toCopyTI;
    private boolean expectedCopyNPE;
    private boolean expectedEqaulsNPE;
    private TopologyType toCopyType;
    private SecondTIType comparisonType;


    private void firstTopologyConfig(TopologyInfo ti){
        ti.set_id("first_id");
        ti.set_name("first_name");
        ti.set_uptime_secs(42);
        ti.set_executors(null);
        ti.set_status("status_ok");
        ti.set_errors(null);
    }

    private void secondTopologyConfig(TopologyInfo ti){
        ti.setFieldValue(TopologyInfo._Fields.ID, "first_id");
        ti.setFieldValue(TopologyInfo._Fields.NAME, "first_name");
        ti.setFieldValue(TopologyInfo._Fields.UPTIME_SECS, 42);
        ti.setFieldValue(TopologyInfo._Fields.EXECUTORS, null);
        ti.setFieldValue(TopologyInfo._Fields.STATUS, "status_ok");
        ti.setFieldValue(TopologyInfo._Fields.ERRORS, null);
    }

    public FirstImprovedTopologyInfoTest(TopologyType toCopy, SecondTIType comparison){

        this.primaryTI = new TopologyInfo();
        //configure the master topologyInfo class
        firstTopologyConfig(this.primaryTI);

        configureDeepCopyParam(toCopy);

        configureEqualsParam(comparison);

    }

    public void configureDeepCopyParam(TopologyType copy){

        this.toCopyTI = new TopologyInfo();
        this.expectedCopyNPE = (copy == TopologyType.NULL);
        this.toCopyType = copy;

        switch(copy){
            case VALID: getValidTI(); break;

            case NOT_VALID: getNotValidTI(); break;

            default: this.toCopyTI = null; break;
        }
    }

    public void configureEqualsParam(SecondTIType comparison){

        this.secundaryTI = new TopologyInfo();
        this.comparisonType = comparison;
        this.expectedEqaulsNPE = ( comparison == SecondTIType.NULL);

        switch(comparison){

            case SAME: secondTopologyConfig(this.secundaryTI); break;

            case DIFFERENT: getDifferentTI(); break;

            default: this.secundaryTI = null;
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

    private void getNotValidTI(){
        this.toCopyTI.set_id("copied_id");
        this.toCopyTI.set_errors(getInvalidMap());
        this.toCopyTI.set_executors(getInvalidList());


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



    private void getDifferentTI(){

        this.secundaryTI = new TopologyInfo();
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.ID, "different_id");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.NAME, "different_name");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.UPTIME_SECS, 24);
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.EXECUTORS, null);
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.STATUS, "alabarda");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.ERRORS, null);

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
            firstTopologyConfig(this.primaryTI);
        }
    }

    @Test
    public void testEquals(){

        this.primaryTI = new TopologyInfo();
        //configure the master topologyInfo class
        firstTopologyConfig(this.primaryTI);

        try {
            boolean areEquals = this.primaryTI.equals(this.secundaryTI);

            if (areEquals) Assert.assertEquals(this.comparisonType, SecondTIType.SAME);

            else Assert.assertNotEquals(this.comparisonType, SecondTIType.SAME);
        }
        catch(Exception e ){
            e.printStackTrace();
        }

    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //deepcopy param        confronto           copyNPE   confrontoNPE
                {TopologyType.VALID,    SecondTIType.SAME},
                {TopologyType.NOT_VALID,    SecondTIType.DIFFERENT},
                {TopologyType.NULL,    SecondTIType.NULL}

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
        NULL
    }

}
