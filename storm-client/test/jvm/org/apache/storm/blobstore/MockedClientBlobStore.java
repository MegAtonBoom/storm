package org.apache.storm.blobstore;

import org.apache.storm.dependency.blobType;
import org.apache.storm.generated.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class MockedClientBlobStore  {

    ClientBlobStore cbs;

    public MockedClientBlobStore(blobType blob) throws AuthorizationException, KeyNotFoundException, KeyAlreadyExistsException {
        this.cbs = spy(ClientBlobStore.class);
        doNothing().when(this.cbs).deleteBlob(isA(String.class));
        when(cbs.createBlobToExtend(isA(String.class), isA(SettableBlobMeta.class))).thenReturn(getMockedAtomicOutputStream());
        switch(blob){
            case VALID_WITH_KEY:
                when(cbs.getBlobMeta(isA(String.class))).thenReturn(new ReadableBlobMeta());
                break;

            case VALID_WITHOUT_KEY:
                when(cbs.getBlobMeta(isA(String.class))).thenThrow(new KeyNotFoundException());
                break;

            case NOT_VALID:
                when(cbs.getBlobMeta(isA(String.class))).thenReturn(null);
                break;

            default: this.cbs=null;
        }

    }

    public ClientBlobStore getCbs() { return this.cbs; }

    private AtomicOutputStream getMockedAtomicOutputStream(){
        AtomicOutputStream aos = mock(AtomicOutputStream.class);
        return aos;
    }


}
