package com.jereibaselibrary.tools;

import java.util.HashMap;

/***
 * 数据操作结果类
 * @ClassName: JRDataResult
 * @Description: TODO
 * @author Guo Jingbing
 * @date 2013-2-17 下午2:32:23
 */
public class JRDataResult
{
	private String resultCode;
	private String resultMessage;
	private Object resultObject;
	private HashMap <String,Object>resultMap;
	public JRDataResult(){}
	public JRDataResult(String resultCode, String resultMessage)
	{
		this.resultCode=resultCode;
		this.resultMessage=resultMessage;
	}
	public JRDataResult(String resultCode, String resultMessage, Object resultObject)
	{
		this.resultCode=resultCode;
		this.resultMessage=resultMessage;
		this.resultObject=resultObject;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Object getResultObject() {
		return resultObject;
	}
	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
}
