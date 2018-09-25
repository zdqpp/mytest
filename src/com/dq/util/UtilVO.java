package com.longtop.cls.frm.util;

import org.apache.commons.beanutils.BeanUtils;

import com.hylandtec.mps.bas.vo.BasUserVO;
import com.hylandtec.mps.org.vo.LogonUserVO;

public class UtilVO {
	
	/**
	 * 复制BasUserVO中的相关信息到LogonUserVO中 
	 * @param BasUserVO
	 * @return LogonUserVO
	 */
	public static LogonUserVO basUserVO2LogonUserVO(BasUserVO basUserVO){
		LogonUserVO logonUserVO = new LogonUserVO();
		if(basUserVO == null){
			return null;
		}
		copyBasUserPeroperties(basUserVO,logonUserVO);
		return logonUserVO;
	}
	
	/**
	 * 复制LOGONUSERVO中的相关信息到BASUSERVO中 （提供给报文核心使用）
	 * @param LogonUserVO
	 * @return BasUserVO
	 */
	public static BasUserVO logonUserVO2BasUserVO(LogonUserVO logonUserVO){
		BasUserVO basUserVO = new BasUserVO();
		if(logonUserVO == null){
			return null;
		}
		copyLogonUserPeroperties(logonUserVO,basUserVO);
		return basUserVO;
	}

	/**
	 * 复制LOGONUSERVO中的相关信息到BASUSERVO中 带TXN （提供给报文核心使用）
	 * @param LogonUserVO 登陆用户VO 
	 * 		  TXN 业务码
	 * @return BasUserVO
	 */
	public static BasUserVO logonUserVO2BasUserVO(LogonUserVO logonUserVO , String txn){
		BasUserVO basUserVO = new BasUserVO();
		if(logonUserVO == null){
			return null;
		}
		copyLogonUserPeroperties(logonUserVO,basUserVO);
		copyProperty(basUserVO, "txn", txn);
		return basUserVO;
	}
	
	/**
	 * COPY LogonUserPeroperties
	 * @param LogonUserVO BasUserVO
	 * @param 
	 */
	private static void copyLogonUserPeroperties(LogonUserVO logonUserVO , BasUserVO basUserVO){
		if(basUserVO == null || logonUserVO ==null){
			return;
		}
		basUserVO.setUserId(logonUserVO.getUserId());
		basUserVO.setUserLoginId(logonUserVO.getUserLoginId());
		basUserVO.setUserName(logonUserVO.getUserName());
		basUserVO.setPositionId(logonUserVO.getPositionId());
		basUserVO.setBranchId(logonUserVO.getBranchId());
		basUserVO.setBranchCode(logonUserVO.getBranchCode());
		basUserVO.setGroupId(logonUserVO.getGroupId());		
		
		//jiangyicong,能不用反射就不用反射
		
//		copyProperty(basUserVO, "userId", logonUserVO.getUserId());
//		copyProperty(basUserVO, "userLoginId", logonUserVO.getUserLoginId());
//		copyProperty(basUserVO, "userName", logonUserVO.getUserName());
//		copyProperty(basUserVO, "positionId", logonUserVO.getPositionId());
//		copyProperty(basUserVO, "branchId", logonUserVO.getBranchId());
//		copyProperty(basUserVO, "branchCode", logonUserVO.getBranchCode());
//		copyProperty(basUserVO, "groupId", logonUserVO.getGroupId());
	}

	private static void copyBasUserPeroperties( BasUserVO basUserVO,LogonUserVO logonUserVO ){
		if(basUserVO == null || logonUserVO ==null){
			return;
		}
		logonUserVO.setUserId(basUserVO.getUserId());
		logonUserVO.setUserLoginId(basUserVO.getUserLoginId());
		logonUserVO.setUserName(basUserVO.getUserName());
		logonUserVO.setPositionId(basUserVO.getPositionId());
		logonUserVO.setBranchId(basUserVO.getBranchId());
		logonUserVO.setBranchCode(basUserVO.getBranchCode());
		logonUserVO.setGroupId(basUserVO.getGroupId());		
		
		//jiangyicong,能不用反射就不用反射
		
//		copyProperty(logonUserVO, "userId", basUserVO.getUserId());
//		copyProperty(logonUserVO, "userLoginId", basUserVO.getUserLoginId());
//		copyProperty(logonUserVO, "userName", basUserVO.getUserName());
//		copyProperty(logonUserVO, "positionId", basUserVO.getPositionId());
//		copyProperty(logonUserVO, "branchId", basUserVO.getBranchId());
//		copyProperty(logonUserVO, "branchCode", basUserVO.getBranchCode());
//		copyProperty(logonUserVO, "groupId", basUserVO.getGroupId());
	}
	/**
	 * 赋值规范中last相关的信息。
	 * 
	 * @param src
	 * @param target
	 */

	public static void copyLastProperties(BasUserVO src, Object target) {
		if (src == null || target == null) {
			return;
		}
		copyLastUserProperties(src, target);
		copyProperty(target, "lastTxn", src.getTxn());
		copyProperty(target, "lastDate", UtilDatetime.getCurrentDatetime());
	}

	/**
	 * 赋值规范中last相关的信息。只赋值用户相关的信息
	 * 
	 * @param src
	 * @param target
	 */
	public static void copyLastUserProperties(BasUserVO src, Object target) {
		if (src == null || target == null) {
			return;
		}
		copyProperty(target, "lastUserId", src.getUserId());
		copyProperty(target, "lastUserLoginId", src.getUserLoginId());
		copyProperty(target, "lastUserName", src.getUserName());
		copyProperty(target, "lastPositionId", src.getPositionId());
		copyProperty(target, "lastBranchId", src.getBranchId());
		copyProperty(target, "lastBranchCode", src.getBranchCode());
		// copyProperty(target, "lastTxn", src.getTxn());
		// copyProperty(target, "lastDate", UtilDatetime.getDatetimeString());
		
	}

	private static void copyProperty(Object target, String name, Object value) {
		try {
			BeanUtils.copyProperty(target, name, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
