package org.apache.storm.dependency;

import org.apache.storm.blobstore.ClientBlobStore;
import org.apache.storm.generated.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class DependencyUploaderTest {

    //class under test
    DependencyUploader du;

    //Mockito.verify(fooMocked, times(0)).setSomething(anyInt());

    //test class parameters
    private fileListType fileType;
    boolean expectedNPE;
    boolean expectedFileExc;

    //method params
    boolean cleanUpIfFails;
    List<File> files;



    public DependencyUploaderTest(fileListType filteType, boolean cleanUpIfFails, boolean expectedNPE, boolean expectedFileExc) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {
        configure(filteType, cleanUpIfFails, expectedNPE, expectedFileExc);
    }

    public void configure(fileListType fileType, boolean cleanUpIfFails, boolean expectedNPE, boolean expectedFileExc) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {

        this.fileType = fileType;
        this.du = new DependencyUploader();
        this.files = new ArrayList<>();
        this.du.setBlobStore(getMockedCBS());
        this.cleanUpIfFails = cleanUpIfFails;
        this.expectedNPE = expectedNPE;
        this.expectedFileExc = expectedFileExc;

        switch(fileType){
            case VALID: this.files.add( getMockedValidFile() ); break;

            case NOT_VALID: this.files.add( getMockedNotValidFile() ); break;

            case EMPTY: break;

            default: this.files = null;

        }
    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //fileList type          cleanupIfFails     expected NullPonterExc    Expected other exception
                {fileListType.NULL,      true,              true,                     false},
                {fileListType.VALID,     false,             false,                    false},
                {fileListType.NOT_VALID,      true,         false,                    true},
                {fileListType.EMPTY,      false,            false,                    false}

        });
    }


    @Test
    public void test(){
        List<String> output;
        try{
            output=this.du.uploadFiles( this.files, this.cleanUpIfFails);
            Assert.assertEquals(output.size(), this.files.size());
        }
        catch(NullPointerException npe){
            Assert.assertTrue( this.expectedNPE );
        }
        catch(RuntimeException e){
            Assert.assertTrue( this.expectedFileExc );
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        finally{
            this.du.shutdown();
        }

    }


    private ClientBlobStore getMockedCBS() throws AuthorizationException, KeyNotFoundException {

        ClientBlobStore cbs = mock(ClientBlobStore.class);
        doNothing().when(cbs).deleteBlob(isA(String.class));
        when(cbs.getBlobMeta(isA(String.class))).thenReturn(new ReadableBlobMeta());
        return cbs;

    }


    private File getMockedValidFile(){

        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("mockedvalid");
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.exists()).thenReturn(true);

        return mockFile;

    }

    private File getMockedNotValidFile(){
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn(null);
        when(mockFile.isFile()).thenReturn(false);
        when(mockFile.exists()).thenReturn(false);

        return mockFile;
    }


    private enum fileListType{
        VALID,
        NOT_VALID,
        EMPTY,
        NULL,
    }
}
