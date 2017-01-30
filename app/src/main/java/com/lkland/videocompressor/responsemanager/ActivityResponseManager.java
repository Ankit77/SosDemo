package com.lkland.videocompressor.responsemanager;

import com.lkland.videocompressor.video.IVideo;

import app.sosdemo.fragment.DashboardFragment;

public class ActivityResponseManager extends AbstractResponseManager{
	protected DashboardFragment mFragment;
	public ActivityResponseManager(DashboardFragment fragment) {
		this.mFragment = fragment;	
	}

	@Override
	public void onProgress(IVideo v, final String str) {
		
		if(mFragment!=null)
			mFragment.progress(v, str);
	}

	@Override
	public void onQueueFinished() {
		if(mFragment!=null)
			mFragment.queueFinished();
	}

	@Override
	public void onPop(IVideo v) {
		if(mFragment!=null)
			mFragment.remove(v);
	}

	@Override
	public void onQueueStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemove(IVideo v) {
		if(mFragment!=null)
			mFragment.remove(v);
	}

	@Override
	public void onAdd(IVideo v) {
		if(mFragment!=null)
			mFragment.add(v);
	}

}
