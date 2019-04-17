package com.ooftf.homer.lib.aidl;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class IpcRequestBody implements Parcelable {
    private Uri uri;
    private String stringBody;
    private byte[] bytesBody;

    public IpcRequestBody() {
    }

    public IpcRequestBody(Uri uri) {
        this.uri = uri;
    }

    protected IpcRequestBody(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        stringBody = in.readString();
        bytesBody = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeString(stringBody);
        dest.writeByteArray(bytesBody);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IpcRequestBody> CREATOR = new Creator<IpcRequestBody>() {
        @Override
        public IpcRequestBody createFromParcel(Parcel in) {
            return new IpcRequestBody(in);
        }

        @Override
        public IpcRequestBody[] newArray(int size) {
            return new IpcRequestBody[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getStringBody() {
        return stringBody;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
    }

    public byte[] getBytesBody() {
        return bytesBody;
    }

    public void setBytesBody(byte[] bytesBody) {
        this.bytesBody = bytesBody;
    }

}
