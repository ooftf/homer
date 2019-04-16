package com.ooftf.homer.lib.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IpcResponseBody implements Parcelable {
    int code = 200;
    String message;
    String stringBody;
    byte[] bytesBody;
    public IpcResponseBody(){

    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    protected IpcResponseBody(Parcel in) {
        code = in.readInt();
        message = in.readString();
        stringBody = in.readString();
        bytesBody = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
        dest.writeString(stringBody);
        dest.writeByteArray(bytesBody);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IpcResponseBody> CREATOR = new Creator<IpcResponseBody>() {
        @Override
        public IpcResponseBody createFromParcel(Parcel in) {
            return new IpcResponseBody(in);
        }

        @Override
        public IpcResponseBody[] newArray(int size) {
            return new IpcResponseBody[size];
        }
    };
}
