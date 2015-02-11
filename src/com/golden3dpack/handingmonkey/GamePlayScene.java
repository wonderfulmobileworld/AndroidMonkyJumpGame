package com.golden3dpack.handingmonkey;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.utils.CCFormatter;






import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;

public class GamePlayScene extends CCLayer{
	
	private static int m_nHighScore;
	public ArrayList<CCSprite> m_spSticks;
	public CCSprite m_spBackgrounds[];
	
	 CCSprite hero;
	 CCSprite lines[];//5
	    
	    CCAction hero_jump;
	    CCAction hero_die;
	    CCIntervalAction hero_start;
	    CCAction hero_normal;
	    
	    boolean isJump;
	    boolean isOn;
	    boolean isTouchesCheck = false;
	    
	    float off_r[];//5
	    
	    int iii;
	
	enum MONKEY_STATE{
		STOP_STATE,
		START_JUMPING,
		GAME_JUMPING,
		GAME_END,
	};
	
	MONKEY_STATE m_nMonkeyState;
	private CCSprite m_spHighScore;
	private CCSprite m_spScore;
	GameOver m_lGameOverLayer;
	private CCTexture2D txt;
	private CCTexture2D tex_monkey;
	private CCLabel lblhigh_Score;
	private CCLabel lblcur_Score;
	private int m_nCurrentScore;
	private boolean m_nGamePauseChk = false;
	private CCMenuItemImage pauseBt;
	private CCMenuItemImage resumeBt;
	private int loof_index;
	private float of;
	
	public GamePlayScene()
	{
		loadUI();
		initParam();
		
		setBaseValue();
		    
		createLines();
		createHero();
//		startGame();

		this.isTouchEnabled_ = true;
		this.schedule("onTimer");
	}
	
	public static CCScene scene()
	{
	    CCScene s = CCScene.node();
	    GamePlayScene l =  new  GamePlayScene();
	    s.addChild(l);
	    return s;
	}
	
	public void initParam()
	{
		m_nMonkeyState = MONKEY_STATE.STOP_STATE;
	}
	
	public void setBaseValue()
	{
		
		off_r = new float[7];
		
	    off_r[0] = 1.8f;
	    off_r[1] = -1.4f;
	    off_r[2] = 1.8f;
	    off_r[3] = -0.8f;
	    off_r[4] = 1.2f;
	    off_r[5] = -1.2f;
	    off_r[6] = 1.3f;
//	    off_r[7] = -1.2f;
//	    off_r[8] = 1.4f;
//	    off_r[9] = -1.4f;
	    
	    iii = -1;
	}
	
	public void createLines()
	{
		lines = new CCSprite[10];
	    for (int i = 0; i<7; i++) {
	        lines[i] = CCSprite.sprite("Vain.png");
	        lines[i].setScaleX(Global.g_fScaleX); lines[i].setScaleY(Global.g_fScaleY);
	        lines[i].setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
	        if(i< 5){
	        	lines[i].setPosition(CGPoint.ccp((i + 1)* 280 * Global.g_fScaleX, Global.g_screenSize.height));
	        }else{
	        	lines[i].setAnchorPoint(0.5f,1.0f);
	        	lines[i].setPosition(CGPoint.ccp((i + 3)* 280 * Global.g_fScaleX, Global.g_screenSize.height));
	        }
	        	        
	       
	        addChild(lines[i], 9);
	        lines[i].setAnchorPoint(0.5f,0.5f);
	    }
	    CCTexture2D tex = CCTextureCache.sharedTextureCache().addImage("vain_money.png");
	     lines[0].setTexture(tex);
	     lines[0].setScaleX(Global.g_fScaleX * 7.4f);
	     lines[0].setScaleY(Global.g_fScaleY);
	}
	public void createHero()
	{
		hero = CCSprite.sprite("MonkeyJumpAntic_0004.png");
		hero.setScaleX(Global.g_fScaleX); hero.setScaleY(Global.g_fScaleY);
		hero.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
//		hero.setPosition(CGPoint.ccp(Global.g_screenSize.width*0.25f, Global.g_screenSize.height*0.2f));
		hero.setPosition(CGPoint.ccp(lines[0].getPosition().x, Global.g_screenSize.height*0.7f));
	    this.addChild(hero, 10);
	    hero.setVisible(false);
	    
	    CCAnimation walkAnim = CCAnimation.animation("WALK", 0.2f);;
	    
	    for(int i = 1; i <= 11; i++)
	    {
	    	walkAnim.addFrame(CCFormatter.format("MonkeyJumpStart_000%d.png", i));
	    }

	    hero_start = CCAnimate.action(walkAnim);
	    
	    CCAnimation deadAnim = CCAnimation.animation("DEAD", 0.1f);
	    
	    for (int i = 1; i<=10; i++) {
	    	deadAnim.addFrame(CCFormatter.format("MonkeyDead_000%d.png", i));
	    }
	    
	    CCMoveTo moveAction = CCMoveTo.action(1.0f, CGPoint.ccp(hero.getPosition().x + 150.0f * Global.g_fScaleX, Global.g_screenSize.height/6.0f));
	    
	    hero_die = CCSpawn.actions(CCAnimate.action(deadAnim), moveAction);

	    
	    CCAnimation jumpAnim = CCAnimation.animation("Jump", 0.1f);
	    for (int i = 1; i<=4; i++) {
	    	jumpAnim.addFrame(CCFormatter.format("MonkeyJumpAntic_000%d.png", i));
	    }
	    
	    CCCallFunc func = CCCallFunc.action(this, "makeJump");
	    CCJumpBy jumpaction = CCJumpBy.action(0.5f, CGPoint.ccp(0.0f, 0.0f), 20.0f, 1);
	    hero_jump = CCSequence.actions(CCSpawn.actions(jumpaction, CCAnimate.action(jumpAnim)), func);
	}

	public void makeJump()
	{
//	    isJump = false;
	    if (isOn == false) {
	    	hero.runAction(hero_die);
	    	Global.playEffect(R.raw.btn_hero_fall);
	    } 
	}

	public void startGame()
	{
		CCMoveBy action = CCMoveBy.action(1.0f, CGPoint.ccp(5.0f * Global.g_fScaleX, Global.g_screenSize.height*0.45f));
//		CCMoveBy action = CCMoveBy.action(1.0f, CGPoint.ccp(0, Global.g_screenSize.height*0.45f));
	    
		CCAction seq = CCSpawn.actions(action, hero_start);
	    hero.runAction(seq);
	}
	public void loadUI()
	{
		loadGame_runInfo();
		m_spBackgrounds = new CCSprite[2];
		txt = CCTextureCache.sharedTextureCache().addImage("Vain.png");  
		tex_monkey = CCTextureCache.sharedTextureCache().addImage("vain_money.png");
		//load backgrounds
		for(int i = 0; i < 2; i ++)
		{
			m_spBackgrounds[i] =  CCSprite.sprite("BG_MidGround.png");
			m_spBackgrounds[i].setScaleX(Global.g_fScaleX); m_spBackgrounds[i].setScaleY(Global.g_fScaleY);
			m_spBackgrounds[i].setPosition(Global.g_screenSize.width/2.0f + i * Global.g_screenSize.width, Global.g_screenSize.height/2.0f);
			this.addChild(m_spBackgrounds[i], 2);
		}
	
		pauseBt = CCMenuItemImage.item("Pause.png", "Pause.png", this, "onPauseGame");
//		pauseBt.setScaleX(Global.g_fScaleX); pauseBt.setScaleY(Global.g_fScaleY);
		pauseBt.setScale(Global.g_fScaleX);
		pauseBt.setPosition(930 * Global.g_fScaleX, 680 * Global.g_fScaleY);

		resumeBt = CCMenuItemImage.item("Play.png", "Play.png", this, "onPauseGame");
//		resumeBt.setScaleX(Global.g_fScaleX); resumeBt.setScaleY(Global.g_fScaleY);
		resumeBt.setScale(Global.g_fScaleX);
		resumeBt.setPosition(930 * Global.g_fScaleX, 680 * Global.g_fScaleY);
		resumeBt.setVisible(false);
		
		CCMenu menu = CCMenu.menu(pauseBt, resumeBt);
		menu.setPosition(0, 0);
		this.addChild(menu, 11);
		
		m_spHighScore = CCSprite.sprite("hscore.png");
		m_spHighScore.setScaleX(Global.g_fScaleX); m_spHighScore.setScaleY(Global.g_fScaleY);
		m_spHighScore.setPosition(150 * Global.g_fScaleX, 700 * Global.g_fScaleY);
		this.addChild(m_spHighScore, 11);
		lblhigh_Score = newLabel(String.format("%d", m_nHighScore), 50, ccColor3B.ccWHITE, CGPoint.ccp(320 * Global.g_fScaleX, 700 * Global.g_fScaleY));
		lblcur_Score = newLabel("0", 50, ccColor3B.ccWHITE, CGPoint.ccp(700 * Global.g_fScaleX, 700 * Global.g_fScaleY));
		
		m_spScore = CCSprite.sprite("score.png");
		m_spScore.setScaleX(Global.g_fScaleX); m_spScore.setScaleY(Global.g_fScaleY);
		m_spScore.setPosition(512 * Global.g_fScaleX, 700 * Global.g_fScaleY);
		this.addChild(m_spScore, 11);
		
	}
	
	CCLabel newLabel(String str ,int size ,ccColor3B color ,CGPoint pos){
	    CCLabel label = CCLabel.makeLabel(str ,"AmericanTypewriter",size);
	    label.setScaleX(Global.g_fScaleX); label.setScaleY(Global.g_fScaleY);
	    addChild(label, 12);
	    label.setColor(color);
	    
	    label.setAnchorPoint(0, 0.5f);
	    label.setPosition(pos);
	    return label;
	}
	
	public static void savegame_runInfo(){
		int mode = Activity.MODE_PRIVATE;
    	SharedPreferences _sharedPref = CCDirector.sharedDirector().getActivity().getSharedPreferences("Game_SaveInfo", mode);
    	SharedPreferences.Editor editor = _sharedPref.edit();
    	
    	boolean ret = true;
    	editor.putBoolean(String.valueOf(0), ret);
    	
    	String key = "Score";
    	editor.putInt(key,m_nHighScore-1);
		editor.commit();
	}
	public static void loadGame_runInfo(){
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences _sharedPref = CCDirector.sharedDirector().getActivity().getSharedPreferences("Game_SaveInfo", mode);
		
		boolean ret = false;
		ret = _sharedPref.getBoolean(String.valueOf(0), false);
		
		if(!ret) 
			return;
		
		String key = "Score";
		m_nHighScore = _sharedPref.getInt(key, 0);	
	}
	public void onPauseGame(Object sender)
	{
		Global.playEffect(R.raw.btn_start);
		if(!m_nGamePauseChk){
			pauseBt.setVisible(false);
			resumeBt.setVisible(true);
			
			this.setIsTouchEnabled(false); 
			
			unschedule("onTimer");   
			hero.pauseSchedulerAndActions();
			m_nGamePauseChk = true;
		}else{
			
			resumeBt.setVisible(false);
			pauseBt.setVisible(true);
			 this.setIsTouchEnabled(true);
			   
			 schedule("onTimer");
			 hero.resumeSchedulerAndActions();
			 m_nGamePauseChk = false;
		}
	}
	
	 public boolean ccTouchesBegan(MotionEvent event) {
//		 CGPoint touchLoc = CGPoint.ccp(event.getX(), event.getY());
		 
		 	if(m_nMonkeyState != MONKEY_STATE.START_JUMPING){
		 		m_nMonkeyState = MONKEY_STATE.START_JUMPING;	
		 		isTouchesCheck = false;
		 		 loof_index = 1;
		 		 of = 0;
		 	} 
		 	if(isTouchesCheck == true){
		 		isTouchesCheck = false;
		 		 if(isJump == false)
				    {
		 			  
				        if (iii!= -1) {
				        	
				        	Global.playEffect(R.raw.btn_switch_vine);
					    	hero.setVisible(true);
					    	hero.setPosition(lines[iii].getPosition().x - lines[iii].getRotation() * 4.0f, hero.getPosition().y);
//					    	MoveHero();
					        isOn = false;
					        
					        hero.stopAllActions();
					        hero.runAction(hero_jump);
					        
				        	CCTexture2D txt = CCTextureCache.sharedTextureCache().addImage("Vain.png");        	
				            lines[iii].setTexture(txt);
				            lines[iii].setScaleX(Global.g_fScaleX); lines[iii].setScaleY(Global.g_fScaleY);
				        }
				        
				    }
		 	}
		    isJump = true;
		 return CCTouchDispatcher.kEventIgnored;
	 }
	 
//	 public void setnormalHero()
//	 {
//		 CCTexture2D txt = CCTextureCache.sharedTextureCache().addImage("MonkeyJumpAntic_0004.png");
//		 hero.setTexture(txt);
//	 }
	 
	 public void onTimer(float dt)
	 {
		
		 if (m_nMonkeyState != MONKEY_STATE.START_JUMPING) 
			 return;
		 m_spBackgrounds[0].setPosition(m_spBackgrounds[0].getPosition().x - 5 * Global.g_fScaleX , m_spBackgrounds[0].getPosition().y);
		 m_spBackgrounds[1].setPosition(m_spBackgrounds[1].getPosition().x - 5 * Global.g_fScaleX , m_spBackgrounds[1].getPosition().y);
		 
		 if(m_spBackgrounds[0].getPosition().x < -Global.g_screenSize.width / 2.0f){
			 for(int i = 0; i < 2; i ++)
				{
					m_spBackgrounds[i].setPosition(Global.g_screenSize.width/2.0f + i * Global.g_screenSize.width, Global.g_screenSize.height/2.0f);
				}
		 }
		 
	     for (int i=0; i<7; i++) {
	    	 float r = lines[i].getRotation() + off_r[i];
	    	 lines[i].setRotation(r);
	    	if(Math.abs(lines[i].getRotation()) > 50)
		    {
		    	off_r[i] = - off_r[i];
		    }
//	    	 if(i < 5){
//	    		 if(Math.abs(lines[i].getRotation()) > 80)
//		    	 {
//		    		 off_r[i] = -off_r[i];
//		    	 }
//	    	 }
//	    	 if(i >=5){
//	    		 if(Math.abs(lines[i].getRotation()) > 20)
//		    	 {
//		    		 off_r[i] = -off_r[i];
//		    	 }
//	    	 }
	    	 
	     }
	    
//	     if (isJump) {
//	         MoveHero();
//	     }
	     moveLines();
	     if(hero.getPosition().y < 200 * Global.g_fScaleY){
	    	 GameOver();
	     }
	 }

//	 public void MoveHero()
//	 {
//		 hero.setPosition(CGPoint.ccp(hero.getPosition().x + 1.0f * Global.g_fScaleX , hero.getPosition().y));
//	 }

	 public void moveLines()
	 {
		 
	     for (int i=0; i<7; i++) {
		    	if(hero.getPosition().x > Global.g_screenSize.width){
		    		of = 30;
		    		hero.setPosition(320 * Global.g_fScaleX, hero.getPosition().y);
		    		this.setIsTouchEnabled(false);
		    	}else{
		    		this.setIsTouchEnabled(true);
		    	}
		    CGRect temp;
		    temp = CGRect.make(hero.getPosition().x , hero.getPosition().y, 55 * Global.g_fScaleX, 55 * Global.g_fScaleY);
		    	 
		    if ( CGRect.intersects(lines[i].getBoundingBox(),temp) && iii != i && isJump == true) {
		          changeLine(i);
		          iii = i;
		    }
	    	if(i < 5){
	    		if(loof_index == 1){
		    		of = 1.2f * Global.g_fScaleX;	
		    	} 
		    	lines[i].setPosition(CGPoint.ccp(lines[i].getPosition().x - of, lines[i].getPosition().y));
		        if (lines[i].getPosition().x< - 180 * Global.g_fScaleX) {
		        	 float ran_index = (float) Math.random();
			 			Log.d("random index", String.format("%f", ran_index));
			    	 of = (ran_index + i + 1) * Global.g_fScaleX;
			    	 loof_index ++;
		             lines[i].setPosition(CGPoint.ccp(Global.g_screenSize.width + 280 * Global.g_fScaleX, lines[i].getPosition().y));
		         }
	    	}else{
//	    		float ran_index = (float) Math.random();
	    		float of_1 = 0.8f * Global.g_fScaleX;
	    		lines[i].setPosition(CGPoint.ccp(lines[i].getPosition().x - of_1, lines[i].getPosition().y));
	    		 if (lines[i].getPosition().x < - 280 * Global.g_fScaleX) {
		             lines[i].setPosition(CGPoint.ccp(Global.g_screenSize.width + 280 * Global.g_fScaleX, lines[i].getPosition().y));
		         }
	    	}
	         if(lines[i].getPosition().x< - 120 * Global.g_fScaleX){
	        	  if (isJump == false && i == iii) {
	        		  Global.playEffect(R.raw.btn_hero_fall);
		                 GameOver();
		                 return;
		             }    	
			      lines[i].setTexture(txt);
			      lines[i].setScaleX(Global.g_fScaleX); lines[iii].setScaleY(Global.g_fScaleY);
	         }
	     }
	 }

	 public void GameOver()
	 {
	     isJump = false;
	     isOn = false;
	     m_nMonkeyState = MONKEY_STATE.GAME_END;
	     iii = -1;
	     hero.stopAllActions();
	     hero.setVisible(true);
	     hero.setTexture(CCTextureCache.sharedTextureCache().addImage("MonkeyDead_00010.png"));
	     hero.runAction(hero_die);
	     unscheduleAllSelectors();
	     
	     if (m_nCurrentScore > m_nHighScore){
	    	 m_nHighScore = m_nCurrentScore;
	    	 savegame_runInfo();
	     }
	     if(m_lGameOverLayer != null){
//		    	removeChildByTag(10, true);
		    	m_lGameOverLayer.cleanup();
		    }
	     m_lGameOverLayer = new GameOver(0,0);  
		  addChild(m_lGameOverLayer, 12);
	    
	 }

	 public void changeLine(int sender)
	 {
		 Global.playEffect(R.raw.btn_grab_vine);
		 hero.stopAllActions();
	     isOn = true;
	     isJump = false;
	     hero.setVisible(false);
	     hero.setPosition(CGPoint.ccp(hero.getPosition().x, Global.g_screenSize.height*0.7f));
	     
	     lines[sender].setTexture(tex_monkey);
	     lines[sender].setScaleX(Global.g_fScaleX * 7.4f);
	     lines[sender].setScaleY(Global.g_fScaleY);
	     if(Math.abs(lines[sender].getRotation()) < 50)
		 {
	    	 off_r[sender] = - off_r[sender];
		 }
	    
	     m_nCurrentScore = m_nCurrentScore + 1;
	     lblcur_Score.setString(String.format("%d", m_nCurrentScore-1));
	     isTouchesCheck = true;
	 }
	
}
