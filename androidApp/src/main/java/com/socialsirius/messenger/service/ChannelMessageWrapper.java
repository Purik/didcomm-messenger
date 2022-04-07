package com.socialsirius.messenger.service;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class ChannelMessageWrapper {
   String topic;
    String messageString;
    String contentType ;
    String did;

    public MessageWrapperMeta getMeta() {
        return meta;
    }

    public void setMeta(MessageWrapperMeta meta) {
        this.meta = meta;
    }

    MessageWrapperMeta meta;

    public String getTopic() {
        return topic;
    }

    public String getMessageString() {
        return messageString;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDid() {
        return did;
    }



    public ChannelMessageWrapper(){

    }
    public ChannelMessageWrapper(String topic, String messageString, String contentType, String did, MessageWrapperMeta meta) {
        this.topic = topic;
        this.messageString = messageString;
        this.contentType = contentType;
        this.did = did;
        this.meta = meta;
    }

    public static final String WIRED_CONTENT_TYPE = "application/ssi-agent-wire";

    public  String getMessageFromMessageString(){
        if (WIRED_CONTENT_TYPE.equals(contentType)) {
            try {
                JSONObject jsonObject = new JSONObject(messageString);
                if(jsonObject.has("wired_data")){
                   return jsonObject.getJSONObject("wired_data").toString();
                }
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return messageString;
    }
    @NonNull
    @Override
    public String toString() {
        return "{topic="+topic+" , messageString="+messageString+" , contentType="+contentType+" , did="+did+"}";
    }

    public static class MessageWrapperMeta{
       public String session_id;
        public   String content_type;
        public   String uid;
        public  Double utc;
    }
}
