package com.example.demo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class modelAudio implements Parcelable {

    String audioTitle;
    String audioDuration;
    String audioArtist;
    Uri audioUri;

    public modelAudio(){

    }

    protected modelAudio(Parcel in) {
        Uri.Builder builder = new Uri.Builder();
        audioTitle = in.readString();
        audioDuration = in.readString();
        audioArtist = in.readString();
        audioUri = builder.path(in.readString()).build();
    }

    public static final Creator<modelAudio> CREATOR = new Creator<modelAudio>() {
        @Override
        public modelAudio createFromParcel(Parcel in) {
            return new modelAudio(in);
        }

        @Override
        public modelAudio[] newArray(int size) {
            return new modelAudio[size];
        }
    };

    public String getaudioTitle() {
        return audioTitle;
    }

    public void setaudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getaudioDuration() {
        return audioDuration;
    }

    public void setaudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getaudioArtist() {
        return audioArtist;
    }

    public void setaudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public Uri getaudioUri() {
        return audioUri;
    }

    public void setaudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audioTitle);
        dest.writeString(audioArtist);
        dest.writeString(audioDuration);
        String t = getaudioUri().toString();
        dest.writeString(t);
    }
}