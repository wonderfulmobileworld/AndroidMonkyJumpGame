package com.golden3dpack.handingmonkey;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Monkey extends Activity {
	private CCGLSurfaceView mGLSurfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    	 
	       mGLSurfaceView = new CCGLSurfaceView(this);
	       setContentView(mGLSurfaceView);
	       CCDirector.sharedDirector().attachInView(mGLSurfaceView);
	}
	
	public void setScaleValue()
	{
		Global.g_screenSize = CCDirector.sharedDirector().winSize();
		Global.g_fScaleX = Global.g_screenSize.width/1024;
		Global.g_fScaleY  = Global.g_screenSize.height/768;
	}
	
	 @Override
	    public void onStart() {
	        super.onStart();
	        setScaleValue();
			CCScene scene = CCScene.node();
			scene.addChild(MenuScene.scene());
			CCDirector.sharedDirector().runWithScene(scene);
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        CCDirector.sharedDirector().pause();
	        Global.SetVolume(true);
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        CCDirector.sharedDirector().resume();
	        Global.SetVolume(false);
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        Global.SetVolume(true);
	        CCTextureCache.sharedTextureCache().removeAllTextures();
	    }
	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monkey, menu);
		return true;
	}

}
