package in.dawt.devmusic;

import java.util.Arrays;

public class Song {

    String title;
    String[] singers;
    int posterImage;
    int audioFile;

    public Song(String title, String[] singers, int posterImage, int audioFile) {
        this.title = title;
        this.singers = singers;
        this.posterImage = posterImage;
        this.audioFile = audioFile;
    }
    public String getSingersAsString() {
        String s = Arrays.toString(this.singers);
        return s.substring(1, s.length() - 1);
    }

}
