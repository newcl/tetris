package info.chenliang.tetris.sound;

public class Sound {
	private int resId;
	private int soundId;
	private boolean loaded;
	
	public Sound(int resId) {
		super();
		this.resId = resId;
	}

	public int getSoundId() {
		return soundId;
	}

	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public int getResId() {
		return resId;
	}
	
	
}
