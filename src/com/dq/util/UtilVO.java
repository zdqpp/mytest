package com.longtop.cls.frm.util;

import org.apache.commons.beanutils.BeanUtils;

import com.hylandtec.mps.bas.vo.BasUserVO;
import com.hylandtec.mps.org.vo.LogonUserVO;

public class UtilVO {
	
	/**
	 * ����BasUserVO�е������Ϣ��LogonUserVO�� 
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
	 * ����LOGONUSERVO�е������Ϣ��BASUSERVO�� ���ṩ�����ĺ���ʹ�ã�
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
	 * ����LOGONUSERVO�е������Ϣ��BASUSERVO�� ��TXN ���ṩ�����ĺ���ʹ�ã�
	 * @param LogonUserVO ��½�û�VO 
	 * 		  TXN ҵ����
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
		
		//jiangyicong,�ܲ��÷���Ͳ��÷���
		
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
		
		//jiangyicong,�ܲ��÷���Ͳ��÷���
		
//		copyProperty(logonUserVO, "userId", basUserVO.getUserId());
//		copyProperty(logonUserVO, "userLoginId", basUserVO.getUserLoginId());
//		copyProperty(logonUserVO, "userName", basUserVO.getUserName());
//		copyProperty(logonUserVO, "positionId", basUserVO.getPositionId());
//		copyProperty(logonUserVO, "branchId", basUserVO.getBranchId());
//		copyProperty(logonUserVO, "branchCode", basUserVO.getBranchCode());
//		copyProperty(logonUserVO, "groupId", basUserVO.getGroupId());
	}
	/**
	 * ��ֵ�淶��last��ص���Ϣ��
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
	 * ��ֵ�淶��last��ص���Ϣ��ֻ��ֵ�û���ص���Ϣ
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
