package com.golden3dpack.handingmonkey;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;


public class MenuScene extends CCLayer{

	public MenuScene()
	{
		loadUI();
	}
	
	public static CCScene scene()
	{
	    CCScene s = CCScene.node();
	    MenuScene l =  new  MenuScene();
	    s.addChild(l);
	    return s;
	}
	
	public void loadUI()
	{
		CCSprite sp = CCSprite.sprite("LoadingScreen.jpg");
		sp.setScaleX(Global.g_fScaleX); sp.setScaleY(Global.g_fScaleY);
		sp.setPosition(Global.g_screenSize.width/2.0f, Global.g_screenSize.height/2.0f);
		this.addChild(sp, 2);
		
		  CCMenuItemImage playBt = CCMenuItemImage.item("StartGame.png", "StartGame.png", this, "onPlay");
		  playBt.setScaleX(Global.g_fScaleX); playBt.setScaleY(Global.g_fScaleY);
		  playBt.setPosition(Global.g_screenSize.width/2.0f + 230 * Global.g_fScaleX, Global.g_screenSize.height/2.0f - 130 * Global.g_fScaleY);

		  
		  CCMenu menu = CCMenu.menu(playBt);
		  menu.setPosition(0, 0);
		  this.addChild(menu, 3);
	}
	
	
	
	public void onPlay(Object sender)
	{
		Global.playEffect(R.raw.btn_start);
		CCDirector.sharedDirector().replaceScene(GamePlayScene.scene());
	}
	
}
