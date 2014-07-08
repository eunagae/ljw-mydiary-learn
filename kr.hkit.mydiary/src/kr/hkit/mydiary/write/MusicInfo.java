package kr.hkit.mydiary.write;

public class MusicInfo {
	private String songTitle; 
    private String singer; 

    public MusicInfo(String songTitle, String singer) { 
        this.songTitle = songTitle; 
        this.singer = singer; 
    } 

    public String getMainString() { 
        return songTitle; 
    } 

    public String getSubString() { 
        return singer; 
    }
}
