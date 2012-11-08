package info.chenliang.tetris.sound;

import info.chenliang.tetris.R;
import info.chenliang.tetris.TetrisActivity;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager implements OnLoadCompleteListener{
	private List<Sound> sounds;

	private SoundPool soundPool;
	
	public SoundManager()
	{
	}
	
	public void init()
	{
		sounds = new ArrayList<Sound>();
		sounds.add(new Sound(R.raw.hit));
		sounds.add(new Sound(R.raw.move));
		
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(this);
		for(int i=0; i < sounds.size();i++)
		{
			Sound sound = sounds.get(i);
			sound.setSoundId(soundPool.load(TetrisActivity.instance.relativeLayout.getContext(), sound.getResId(), 0));	
		}
		
	}
	
	public void play(int resId)
	{
		Sound sound = getSoundByResId(resId);
		if(sound != null)
		{
			soundPool.play(sound.getSoundId(), 0.5f, 0.5f, 0, 1, 1);
		}
	}
	
	public Sound getSoundByResId(int resId)
	{
		for(int i=0; i < sounds.size(); i++)
		{
			Sound sound = sounds.get(i); 
			if(sound.getResId() == resId)
			{
				return sound;
			}
		}
		
		return null;
	}
	

	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		Sound sound = getSoundByResId(sampleId);
		if(sound != null)
		{
			if(status == 0)
			{
				sound.setLoaded(true);
			}
		}
	}
}
