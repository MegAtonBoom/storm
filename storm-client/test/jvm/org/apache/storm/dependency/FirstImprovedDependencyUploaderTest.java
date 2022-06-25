package org.apache.storm.dependency;

import org.apache.storm.blobstore.ClientBlobStore;
import org.apache.storm.blobstore.MockedClientBlobStore;
import org.apache.storm.generated.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class FirstImprovedDependencyUploaderTest {

    //class under test
    @Spy
    DependencyUploader du;

    //Mockito.verify(fooMocked, times(0)).setSomething(anyInt());

    //test class parameters
    private fileListType fileType;
    private blobType blob;
    boolean expectedNPE;
    boolean expectedFileExc;

    //method params
    boolean cleanUpIfFails;
    List<File> files;



    public FirstImprovedDependencyUploaderTest(blobType blob, fileListType filteType, boolean cleanUpIfFails, boolean expectedNPE, boolean expectedFileExc) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {
        configure(blob, filteType, cleanUpIfFails, expectedNPE, expectedFileExc);
    }

    public void configure(blobType blob, fileListType fileType, boolean cleanUpIfFails, boolean expectedNPE, boolean expectedFileExc) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException, IOException {

        this.blob=blob;
        this.fileType = fileType;
        this.du = new DependencyUploader();

        //added to improve pit coverage
        this.du= Mockito.spy(this.du);

        this.files = new ArrayList<>();
        this.cleanUpIfFails = cleanUpIfFails;
        this.expectedNPE = expectedNPE;
        this.expectedFileExc = expectedFileExc;

        switch(fileType){
            case VALID: this.files.add( getMockedValidFile() ); break;

            case NOT_VALID: this.files.add( getMockedNotValidFile() ); break;

            case EMPTY: break;

            default: this.files = null;

        }

        this.du.setBlobStore(getMockedCBS(blob));

    }


    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                //blob                    file list type          cleanupIfFails    expectedNPE  expectedFileException           expectednpe
                {blobType.VALID_WITH_KEY, fileListType.NULL,      true,              true,       false},
                {blobType.VALID_WITH_KEY, fileListType.VALID,     false,             false,      false},
                {blobType.VALID_WITH_KEY, fileListType.NOT_VALID,      true,              false,      true},
                {blobType.VALID_WITH_KEY, fileListType.EMPTY,      false,              false,        false},

                //added to improve jacoco coverage
                //
                {blobType.VALID_WITHOUT_KEY, fileListType.VALID,      false,              false,        false},
                {blobType.NULL, fileListType.NULL,      true,              true,        false},
                //{blobType.NOT_VALID, fileListType.VALID,      false,              true,        false},

                //added to improve pit mutation coverage
                {blobType.VALID_WITH_KEY, fileListType.VALID,      true,              false,        false},
                {blobType.VALID_WITH_KEY, fileListType.EMPTY,      true,              false,        false},
                //{blobType.NOT_VALID, fileListType.VALID,      true,              true,        false},
                {blobType.VALID_WITHOUT_KEY, fileListType.EMPTY,      false,              false,        false},
                {blobType.VALID_WITHOUT_KEY, fileListType.VALID,      true,              false,        false}
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
            Assert.assertTrue( this.expectedFileExc || (this.blob == blobType.VALID_WITHOUT_KEY));
        }
        catch(Exception e){
            Assert.fail();
        }
        finally{

            this.du.shutdown();
            //added to improve pit coverage
            if(!this.cleanUpIfFails){
                verify(this.du, times(0)).deleteBlobs(isA(List.class));
            }

        }

    }


    private ClientBlobStore getMockedCBS(blobType blob) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException {

        MockedClientBlobStore cbs= new MockedClientBlobStore(blob);
        return cbs.getCbs();

    }


    private File getMockedValidFile(){

        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("mockedvalid");
        when(mockFile.toPath()).thenReturn(getMockedPath());
        when(mockFile.isFile()).thenReturn(true);
        when(mockFile.exists()).thenReturn(true);

        return mockFile;

    }

    private Path getMockedPath(){
        Path mockedPath = mock(Path.class);
        return mockedPath;
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
