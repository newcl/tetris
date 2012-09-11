package info.chenliang.tetris;

public interface Tickable {
	public void tick(int timeElapsed);
	public boolean isFinished();
}
