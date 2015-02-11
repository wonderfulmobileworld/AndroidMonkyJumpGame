package com.golden3dpack.handingmonkey;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

public class Global {
	public static float g_fScaleX = 1.0f;
	public static float g_fScaleY = 1.0f;
	public static CGSize g_screenSize;
	public static final int g_nSticks = 6;
	public static final int g_nStepStick = 3;
	public static final float g_fMoveSpeed = 2.0f;
	public static int g_nMonkeyJumpImageNum = 15;
	
	
	public static void playEffect(int resId)
	{
		SoundEngine1.sharedEngine().playEffect(CCDirector.sharedDirector().getActivity().getApplicationContext(), resId);
	}	
	
	public static void SetVolume(Boolean bTrue)
	{
		if(!bTrue)
		{
			SoundEngine1.sharedEngine().setEffectsVolume(1.0f);
			SoundEngine1.sharedEngine().setSoundVolume(0.5f);
		}
		else
		{
			SoundEngine1.sharedEngine().setEffectsVolume(0.0f);
			SoundEngine1.sharedEngine().setSoundVolume(0.0f);
		}
	}
}
