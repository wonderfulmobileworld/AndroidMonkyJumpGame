package com.golden3dpack.handingmonkey;

import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;

public class GameOver extends CCLayer{
	
	private CCSprite m_spGameOverBg;
	private CCMenuItemImage game_over;
	private CCMenu menu;
	
	public GameOver(int high_score, int cur_score) {
		// TODO Auto-generated constructor stub
		super();
		this.setIsTouchEnabled(true);
		
		LoadUI();
	}
	
	public void LoadUI(){
		  
		game_over = CCMenuItemImage.item("GameOver.png", "GameOver.png", this, "onReplay");
		game_over.setScaleX(Global.g_fScaleX); game_over.setScaleY(Global.g_fScaleY);
		game_over.setPosition(512 * Global.g_fScaleX, 384 * Global.g_fScaleY);
		
		menu = CCMenu.menu(game_over);
		menu.setPosition(0, 0);
		addChild(menu, 0);
	}
	
	
	public void onReplay(Object sender)
	{
		Global.playEffect(R.raw.btn_start);
		CCDirector.sharedDirector().replaceScene(MenuScene.scene());
	}
}
