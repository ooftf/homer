package com.ooftf.homer.lib.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IpcRequestBody implements Parcelable {
    String stringBody;
    byte[] bytesBody;

    public IpcRequestBody() {
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

    protected IpcRequestBody(Parcel in) {
        stringBody = in.readString();
        bytesBody = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
}
