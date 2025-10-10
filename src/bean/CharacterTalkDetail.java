package bean;

import java.util.List;
import java.util.Map;

import utils.RandomUtil;

public class CharacterTalkDetail
{
	private CharacterModel owner;
	
	public Map<Situation, List<CharacterDialog>> dialogMap;
	
	public CharacterDialog nowDialog;
	
	private int talkTimer;

	public int talkStartTime;

	public int talkEndTime;
	
	public boolean isStopNextTalk;

	public boolean stopTalk;
	
	public int chengeTalkStart;

	private boolean isTalkEnd;

	public final int talkDisappear = 300;
	
	public CharacterTalkDetail(CharacterModel owner, Map<Situation, List<CharacterDialog>> dialogMap)
	{
		this.owner = owner;
		this.dialogMap = dialogMap;
		this.nowDialog = getNowDialog();
		this.talkTimer = 0;
		this.talkStartTime = 600 - talkDisappear;
		this.talkEndTime = talkStartTime + talkDisappear;
		this.chengeTalkStart = talkStartTime;
		this.isStopNextTalk = false;
		this.isTalkEnd = false;
		this.stopTalk = false;
	}
	
	public void addTimer()
	{
		talkTimer++;
	}
	
	public void timerReset()
	{
		talkTimer = 0;
	}
	
	public void timerSetting()
	{
		if(chengeTalkStart != talkStartTime)
		{
			setTalkStartTime(chengeTalkStart);
		}
		if(isStopNextTalk)
		{
			stopTalk = true;
			timerReset();
		}
	}
	
	public void changeDialog()
	{

		CharacterDialog dialog = RandomUtil.selectOne(dialogMap.get(Situation.getTimeorNormalSituation()));
		if(dialog == null)
		{
			dialog = RandomUtil.selectOne(dialogMap.get(Situation.Normal));
		}
		this.nowDialog = dialog;
	}
	
	public CharacterDialog getNowDialog()
	{
		if(nowDialog == null)
		{
			nowDialog = RandomUtil.selectOne(dialogMap.get(Situation.Normal));
		}
		return nowDialog;
	}
	public boolean isTalkChangePre()
	{
		return getTalkTimer() == talkStartTime - 1;
	}
	
	public int getTalkTimer()
	{
		return talkTimer;
	}
	
	public boolean isTalk()
	{
		return talkTimer >= talkStartTime;
	}

	public boolean isTalkEnd()
	{
		if(talkTimer >= talkEndTime)
		{
			isTalkEnd = true;
		}
		return isTalkEnd;
	}
	public void setTalkEnd(boolean isTalkEnd)
	{
		this.isTalkEnd = isTalkEnd;
	}

	public void changeTalkStart(int time)
	{
		this.chengeTalkStart = time - talkDisappear;
	}

	private void setTalkStartTime(int time)
	{
		this.talkStartTime = time;
		this.talkEndTime = talkStartTime + talkDisappear;
	}

	public void setStopNextTalk(boolean isStopNextTalk)
	{
		this.isStopNextTalk = isStopNextTalk;
		if(isStopNextTalk == false)
		{
			stopTalk = false;
		}
	}

	public boolean isStopNextTalk()
	{
		return this.isStopNextTalk;
	}
}
