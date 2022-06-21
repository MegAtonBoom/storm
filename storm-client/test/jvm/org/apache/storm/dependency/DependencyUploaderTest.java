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

    //method params
    boolean cleanUpIfFails;
    List<File> files;



    public DependencyUploaderTest(fileListType filteType, boolean cleanUpIfFails, boolean expectedNPE) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {
        configure(filteType, cleanUpIfFails, expectedNPE);
    }

    public void configure(fileListType fileType, boolean cleanUpIfFails, boolean expectedNPE) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {

        this.fileType = fileType;
        this.du = new DependencyUploader();
        this.files = new ArrayList<>();
        this.du.setBlobStore(getMockedCBS());
        this.cleanUpIfFails = cleanUpIfFails;
        this.expectedNPE = expectedNPE;

        switch(fileType){
            case VALID: this.files.add( getMockedValidFile(0) ); break;

            case NOT_VALID: this.files.add( getMockedNotValidFile(0) ); break;

            case EMPTY: break;

            default: this.files = null;

        }
    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //deepcopy param        confronto           copyNPE   confrontoNPE
                {fileListType.NULL,      true,              true },
                {fileListType.VALID,     false,             false},
                {fileListType.NOT_VALID,      true,              true },
                {fileListType.EMPTY,      false,              false }

        });
    }


    @Test
    public void test(){

        try{
            this.du.uploadFiles( this.files, this.cleanUpIfFails);

        }
        catch(NullPointerException npe){
            Assert.assertTrue(this.expectedNPE );
        }
        catch(Exception e){
            Assert.assertEquals( this.fileType, fileListType.NOT_VALID);
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


    private File getMockedValidFile(int n){

        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("mockedvalid"+String.valueOf(n));
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.exists()).thenReturn(true);

        return mockFile;

    }

    private File getMockedNotValidFile(int n){
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
