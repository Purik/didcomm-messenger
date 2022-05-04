package com.socialsirius.messenger.ui.activities.invite

import android.content.Intent
import android.util.Log
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel

import javax.inject.Inject

class HandleWebInviteActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider
  //  messageListenerUseCase: MessageListenerUseCase
) : BaseActivityModel( ) {


    fun analysUrl(intent: Intent) : String{

        var isFromExternalUrl = false
        var dataString = ""
        if (intent != null) {
            val data = intent.data
            if (data != null) {
                isFromExternalUrl = true
                dataString = data.toString()
                /*       List<String> queryParams =  data.getQueryParameters("c_i");
                if(queryParams!=null){
                    if(queryParams.size()>0){
                        String query = queryParams.get(0);
                        Log.d("mylog500", "query " + query);
                        Log.d("mylog500", "query.substring(1) " + query.substring(1));
                        Log.d("mylog500", "query.substring(2) " + query.substring(2));
                        Log.d("mylog500", "query.substring(3) " + query.substring(3));
                        dataString = query;
                    }

                }*/
                //dataString = data.getQuery().replace("c_i=","");
                Log.d("mylog500", "data $data")
                Log.d("mylog500", "dataString $dataString")
                //    mToolbar.setTitle(R.string.dialog_scan_invitation);
            }
        }
        return dataString
    }
}