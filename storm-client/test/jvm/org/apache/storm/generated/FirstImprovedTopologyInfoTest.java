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
    private boolean expectedEqualsNPE;
    private TopologyType toCopyType;
    private SecondTIType comparisonType;

    private void topologyConfig(int i){
        if(i==1) firstTopologyConfig();
        else if(i==2) secondTopologyConfig();
    }

    private void firstTopologyConfig(){

        this.primaryTI = new TopologyInfo();
        this.primaryTI.set_id("first_id");
        this.primaryTI.set_name("first_name");
        this.primaryTI.set_uptime_secs(42);
        this.primaryTI.set_executors(null);
        this.primaryTI.set_status("status_ok");
        this.primaryTI.set_errors(null);
    }

    private void secondTopologyConfig(){

        this.secundaryTI = new TopologyInfo();
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.ID, "first_id");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.NAME, "first_name");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.UPTIME_SECS, 42);
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.EXECUTORS, null);
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.STATUS, "status_ok");
        this.secundaryTI.setFieldValue(TopologyInfo._Fields.ERRORS, null);
    }

    public FirstImprovedTopologyInfoTest(TopologyType toCopy, SecondTIType comparison){

        //configure the master topologyInfo class
        firstTopologyConfig();

        configureDeepCopyParam(toCopy);

        configureEqualsParam(comparison);

    }

    public void configureDeepCopyParam(TopologyType copy){

        this.toCopyTI = new TopologyInfo();
        this.expectedCopyNPE = (copy == TopologyType.NULL);
        this.toCopyType = copy;

        switch(copy){

            //case VALID: getValidTI();

            //next 2 added to improve badua coverage
            case VALID_EMPTY: getValidTI(); break;

            case VALID_FULL: getFullValidTI(); break;

            case NOT_VALID: getNotValidTI(false); break;

            default: this.toCopyTI = null; break;
        }
    }

    public void configureEqualsParam(SecondTIType comparison){

        this.secundaryTI = new TopologyInfo();
        this.comparisonType = comparison;
        this.expectedEqualsNPE = ( comparison == SecondTIType.NULL);

        switch(comparison){

            case SAME: topologyConfig(2); break;

            case DIFFERENT: getDifferentTI(); break;

            case NOT_VALID: getNotValidTI(true);

            default: this.secundaryTI = null;
        }
    }

    //added to improve badua coverage
    private void getFullValidTI(){
        getValidTI();
        this.toCopyTI.set_component_debug(new HashMap<>());
        this.toCopyTI.set_storm_version("versione_10");
        this.toCopyTI.set_sched_status("statusss");
        this.toCopyTI.set_owner("test_owner");
        this.toCopyTI.set_replication_count(10);
        this.toCopyTI.set_requested_cpu(2);
        this.toCopyTI.set_requested_memoffheap(3);
        this.toCopyTI.set_requested_memonheap(3);
        this.toCopyTI.set_assigned_cpu(2);
        this.toCopyTI.set_assigned_memoffheap(3);
        this.toCopyTI.set_assigned_memonheap(3);

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
            this.primaryTI = new TopologyInfo(this.toCopyTI);
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
            topologyConfig(1);
        }
    }

    @Test
    public void testEquals(){

        //configure the master topologyInfo class
        topologyConfig(1);

        try {
            boolean areEquals = this.primaryTI.equals(this.secundaryTI);

            if (areEquals) Assert.assertEquals(this.comparisonType, SecondTIType.SAME);

            else Assert.assertNotEquals(this.comparisonType, SecondTIType.SAME);
        }
        catch(NullPointerException npe){
            Assert.assertTrue( this.expectedEqualsNPE );
        }
        catch(Exception e ){
            e.printStackTrace();
        }

    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //deepcopy param              equals param
                {TopologyType.VALID_EMPTY,    SecondTIType.SAME},
                {TopologyType.VALID_FULL,    SecondTIType.NOT_VALID},
                {TopologyType.NOT_VALID,    SecondTIType.DIFFERENT},
                {TopologyType.NULL,    SecondTIType.NULL}

        });
    }



    private enum TopologyType{
        //old
        //VALID

        //next 2 added to improve badua coverage
        VALID_EMPTY,
        VALID_FULL,
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
