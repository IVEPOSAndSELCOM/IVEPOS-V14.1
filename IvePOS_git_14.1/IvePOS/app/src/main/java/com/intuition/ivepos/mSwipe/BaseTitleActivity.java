package com.intuition.ivepos.mSwipe;

import android.app.Activity;
import android.os.Bundle;

import com.intuition.ivepos.R;
import com.mswipetech.wisepad.sdk.MSWisepadController;
import com.mswipetech.wisepad.sdk.MSWisepadController.NETWORK_SOURCE;

public class BaseTitleActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment() == MSWisepadController.GATEWAY_ENVIRONMENT.PRODUCTION)
		{
			this.setTitle(false);
		}
		else{
			this.setTitle(true);
		}
	}

	protected void setTitle(boolean aIsGatewayVertical)
	{
		if(aIsGatewayVertical)
		{
			if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.WIFI)
				this.setTitle(getString(R.string.app_name) + "- (Labs)-W");
			else if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.SIM)
				this.setTitle(getString(R.string.app_name) + "- (Labs)-S");
			else if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.EHTERNET)
				this.setTitle(getString(R.string.app_name) + "- (Labs)-E");
			
		}
		else{
			
			if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.WIFI)
				this.setTitle(getString(R.string.app_name) + "- (Live)-W");
			else if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.SIM)
				this.setTitle(getString(R.string.app_name) + "- (Live)-S");
			else if(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource() == NETWORK_SOURCE.EHTERNET)
				this.setTitle(getString(R.string.app_name) + "- (Live)-E");

		}
	}
}
