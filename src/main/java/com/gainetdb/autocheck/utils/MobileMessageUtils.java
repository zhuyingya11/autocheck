package com.gainetdb.autocheck.utils;

import com.alibaba.fastjson.JSONObject;
import com.gainetdb.autocheck.config.SMSConfig;
import com.gainetdb.autocheck.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * [手机短信工具类]
 * @author ChenXiangyu
 * @date 2018-4-2 下午5:41:52
 * @company Gainet
 * @version 1.0
 * @copyright copyright (c) 2018
 */
@Component
@Slf4j
public class MobileMessageUtils {
    @Autowired
	private SMSConfig smsConfig;
	private static MobileMessageUtils instance;
	/** 是否需要回复短信：不需要 */
	private static final String IS_NEED_BACK = "0";
	/** 发送短信时允许的最小token生存时间 */
	private static final int MIN_TOKEN_TTL = 10;
	/** 返回状态码：获取token */
	private static final Map<String,String> CODES_GET_TOKEN = new HashMap<String,String>();
	/** 返回状态码：获取token剩余生存时间 */
	private static final Map<String,String> CODES_GET_TOKEN_TTL = new HashMap<String,String>();
	/** 返回状态码：短信发送 */
	private static final Map<String,String> CODES_SEND_MSG = new HashMap<String,String>();
	/** 短信是否发送成功：成功 */
	private static final String IS_SEND_TRUE = "1";
	

	/** token */
	private String token = "";
	
	static {  
		// 返回状态码：获取token
		CODES_GET_TOKEN.put("140100", "获取TOKEN成功");
		CODES_GET_TOKEN.put("140101", "APPID不能为空");
		CODES_GET_TOKEN.put("140102", "SECRECKEY不能为空");
		CODES_GET_TOKEN.put("140103", "SECRECKEY不正确");
		CODES_GET_TOKEN.put("140104", "生成TOKEN异常");
		CODES_GET_TOKEN.put("140105", "APPID不正确");
		// 返回状态码：获取token剩余生存时间
		CODES_GET_TOKEN_TTL.put("140600", "获取TOKEN生存时间成功");
		CODES_GET_TOKEN_TTL.put("140601", "获取TOKEN生存时间异常");
		// 返回状态码：短信发送
		CODES_SEND_MSG.put("300301", "短信发送成功！");
		CODES_SEND_MSG.put("300302", "业务系统及模块编码不正确！");
		CODES_SEND_MSG.put("300303", "业务系统编码不能为空！");
		CODES_SEND_MSG.put("300304", "模块编码不能为空！");
		CODES_SEND_MSG.put("300305", "手机号码不能为空！");
		CODES_SEND_MSG.put("300306", "短信内容不能为空！");
		CODES_SEND_MSG.put("300309", "是否需要回复不能为空！");
	}
	
	/**
	 * [从interfaceInfo.properties文件中获取配置并构造实例] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-2 下午7:25:10 <br>
	 * @return <br>
	 */
	private MobileMessageUtils() {


	}
	
	/**
	 * [单例获取实例] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-2 下午7:25:10 <br>
	 * @return <br>手机短信工具类单一实例
	 */
	public static MobileMessageUtils getInstance(){
        if(instance==null){
            instance=new MobileMessageUtils();
        }
        log.info("MobileMessageUtils get instance success:"+instance.toString());
        return instance;
    }



	/**
	 * [发送短信] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-2 下午7:26:53 <br>
	 * @param phoneNum 手机号码
	 * @param msg 要发送的短信内容（验证码）
	 * @return <br>Result.success：是否发送成功；Result.msg：调用接口的返回的结果（调用接口的信息，不可用于用户提示）；
	 */
	public Result sendMessage(String phoneNum,String msg){
		// token为空
		if (this.token == null || this.token.isEmpty()) {
			// 获取token并赋值
			Result getTokenResult = getToken();
			// 获取token失败
			if (!getTokenResult.isSuccess()) {
				return getTokenResult;
			}
			
		// token不为空，获取并判断token剩余存活时间
		} else {
			Result getTokenTtlResult = getTokenTtl();
			// 获取token剩余存活时间失败
			if (!getTokenTtlResult.isSuccess()) {
				return getTokenTtlResult;
			}
			// 判断token剩余存活时间不在允许范围内
			if (MIN_TOKEN_TTL > Integer.parseInt(getTokenTtlResult.getObj().toString())) {
				// 获取token并赋值
				Result getTokenResult = getToken();
				// 获取token失败
				if (!getTokenResult.isSuccess()) {
					return getTokenResult;
				}
			}
		}
		
		// 发送短信
		return sendMsg(phoneNum,msg);
	}
	
	/**
	 * [获取token] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-3 上午9:18:05 <br>
	 * @return <br>Result.success：是否成功获取token；Result.msg：调用接口的返回的结果（调用接口的信息，不可用于用户提示）；
	 */
	private Result getToken(){
		Result result = new Result();
		// 调用接口
		String interfResult = "";
		URL restURL;
		HttpURLConnection conn = null;
		BufferedReader bReader = null;
		try {
			restURL = new URL(smsConfig.getUrlGetToken());
		
	        conn = (HttpURLConnection) restURL.openConnection();
	        //请求方式
	        conn.setRequestMethod("POST");
	        //设置是否从httpUrlConnection读入、读出
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
	        conn.setAllowUserInteraction(false);
	        // 设置请求头信息
	        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
	        
	        PrintStream ps = new PrintStream(conn.getOutputStream());
	        String query = "{'appId':'"+ smsConfig.getAppId() +"','secrectKey':'"+ smsConfig.getSecrectKey() +"'}";
	        ps.print(query);
	        ps.close();
	        bReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
	        String line = "";
	        while(null != (line=bReader.readLine()))
	        {
	        	interfResult +=line;
	        }
		} catch (MalformedURLException e) {
			log.error("token get throw "+e.getClass().getName()+":"+e.getMessage());
		} catch (ProtocolException e) {
			log.error("token get throw "+e.getClass().getName()+":"+e.getMessage());
		} catch (IOException e) {
			log.error("token get throw "+e.getClass().getName()+":"+e.getMessage());
		} finally {
			try {
				if (bReader != null) {
					bReader.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e2) {
				log.error("token get throw "+e2.getClass().getName()+":"+e2.getMessage());
			}
		}
		
		// 调用获取token接口失败
		if (interfResult == null || interfResult.isEmpty()) {
			result.setSuccess(false);
			result.setMsg("获取token失败！");
			log.error("token get failed");
			return result;
		}
		
		// 转换接口返回值
		JSONObject resultObj = JSONObject.parseObject(interfResult);
		
		// 获取接口返回值
		String code = resultObj.getString("code");
		String token = resultObj.containsKey("data") ? resultObj.getString("data") : "";
		String msg = CODES_GET_TOKEN.get(code) != null ? CODES_GET_TOKEN.get(code) : "";
		boolean succ = resultObj.getBoolean("succ");
		
		// 获取token成功
		if (succ) {
			result.setSuccess(true);
			result.setMsg(msg);
			this.token = token;
			log.info("token get msg:" + msg);
		// 获取token失败
		} else {
			result.setSuccess(false);
			result.setMsg(msg);
			log.error("token get msg:" + msg);
		}
		return result;
	}
	
	/**
	 * [获取token剩余时间] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-3 上午9:18:10 <br>
	 * @return <br>Result.success：是否成功获取token剩余时间；Result.msg：调用接口的返回的结果（调用接口的信息，不可用于用户提示）；Result.obj：int型，token剩余时间；
	 */
	private Result getTokenTtl(){
		Result result = new Result();
		// 调用接口
		String interfResult = "";
		String query = "token="+this.token;
		//interfResult += HttpRequestUtils.sendGet(this.urlGetTokenTtl,query,null);
	//	ResponseEntity<String> exchange=RestTemplateUtils.get(smsConfig.getUrlGetTokenTtl(),String.class,query);
		//interfResult=exchange.getBody();
		interfResult += HttpRequestUtils.sendGet(smsConfig.getUrlGetTokenTtl(),query,null);
		log.info(String.format("interfResult:%s",interfResult));
		// 调用获取token剩余时间接口失败
		if (interfResult == null || interfResult.isEmpty()) {
			result.setSuccess(false);
			result.setMsg("获取token剩余时间失败！");
			log.error("token ttl get failed");
			return result;
		}
		
		// 转换接口返回值
		JSONObject resultObj = JSONObject.parseObject(interfResult);
		
		// 获取接口返回值
		String code = resultObj.getString("code");
		int tokenTtl = resultObj.containsKey("data") ? Integer.parseInt(resultObj.get("data").toString()) : 0;
		String msg = CODES_GET_TOKEN_TTL.get(code) != null ? CODES_GET_TOKEN_TTL.get(code) : "";
		boolean succ = resultObj.getBoolean("succ");
		
		// 获取token剩余存活时间成功
		if (succ) {
			result.setSuccess(true);
			result.setMsg(msg);
			result.setObj(tokenTtl);
			log.info("token ttl get msg:"+msg);
			
		// 获取token剩余存活时间失败
		} else {
			result.setSuccess(false);
			result.setMsg(msg);
			log.error("token ttl get msg:"+msg);
		}
		return result;
	}
	
	/**
	 * [发送短信] <br>
	 * @author ChenXiangyu <br>
	 * @date 2018-4-3 上午9:18:05 <br>
	 * @param phoneNum 手机号码
	 * @param phoneMsg 要发送的短信内容
	 * @return <br>Result.success：是否发送成功；Result.msg：调用接口的返回的结果（调用接口的信息，不可用于用户提示）；Result.obj：与手机号组成唯一键，用于识别回复记录；
	 */
	private Result sendMsg(String phoneNum,String phoneMsg){
		log.info(smsConfig.toString());
		Result result = new Result();
		// 调用接口
		String interfResult = "";
		JSONObject query = new JSONObject();
		query.put("phone", phoneNum);  
		query.put("shortNo", smsConfig.getShortNo());
		query.put("modCode", "00"+smsConfig.getModCode());
		query.put("msg", phoneMsg);  
		query.put("isNeedBack", IS_NEED_BACK);
		log.info(String.format("query参数：%s",query));
		log.info(String.format("token参数：%s",token));
		// 暂时未调成功
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(smsConfig.getUrlSendMessage());
	        httpPost.setHeader("token",this.token);
	        StringEntity stringEntity = new StringEntity(query.toString(), ContentType.APPLICATION_JSON);
	        httpPost.setEntity(stringEntity);
	        response = httpClient.execute(httpPost);
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {  
	        	//按指定编码转换结果实体为String类型  
	        	interfResult = EntityUtils.toString(entity, "UTF-8");  
	        } 
		} catch (MalformedURLException e) {
			log.error("send msg throw "+e.getClass().getName()+":"+e.getMessage());
		} catch (ProtocolException e) {
			log.error("send msg throw "+e.getClass().getName()+":"+e.getMessage());
		} catch (IOException e) {
			log.error("send msg throw "+e.getClass().getName()+":"+e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e2) {
				log.error("send msg throw "+e2.getClass().getName()+":"+e2.getMessage());
			}
		}
		
		// 调用获取token接口失败
		if (interfResult == null || interfResult.isEmpty()) {
			result.setSuccess(false);
			result.setMsg("发送短信失败！");
			log.error("send msg failed");
			return result;
		}
		
		// 转换接口返回值
		JSONObject resultObj = JSONObject.parseObject(interfResult);
		JSONObject dataObj = resultObj.containsKey("data") && !"0".equals(resultObj.getString("data")) ? JSONObject.parseObject(resultObj.getString("data")) : null;
		
		// 获取接口返回值
		String code = resultObj.getString("code");
		boolean isSend = IS_SEND_TRUE.equals(dataObj == null ? "" : dataObj.getString("isSend"));
		// 与手机号组成唯一键，用于识别回复记录
		String fjm = dataObj == null ? "" : dataObj.getString("fjm");
		String msg = CODES_SEND_MSG.get(code) != null ? CODES_SEND_MSG.get(code) : "";
		boolean succ = resultObj.getBoolean("succ");
		
		// 发送成功
		if (succ && isSend) {
			result.setSuccess(true);
			result.setMsg(msg);
			result.setObj(fjm);
			log.info("send msg get msg:"+msg);
			
		// 发送失败
		} else {
			result.setSuccess(false);
			result.setMsg(msg);
			log.error("send msg get msg:"+msg);
		}
		return result;
	}



	public static void main(String[] args) {
		MobileMessageUtils.getInstance().sendMessage("15038059874","你好");
	}
}
