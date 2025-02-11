package com.telus.cmb.account.informationhelper.dao;
/*
 * Created by Inbaselvan Gandhi for WL10 Upgrade
 */
import java.util.Collection;
import java.util.List;

import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.framework.info.MemoInfo;


public interface MemoDao {

	  /**
	   * Retrieve Last Memos  By billing account number (BAN)
	   *
	   * @param   int     billing account number (BAN)
	   * @param   int     maximum memos
	   *
	   * @returns  Memo Infos	
	   *
	   */
	List<MemoInfo> retrieveMemos(int ban , int count) ;
	
	  /**
	   * Retrieves memos by given set of criteria
	   *
	   * @param MemoCriteria criteria for search
	   * @returns MemoInfo result set value objects
	   */
	List<MemoInfo> retrieveMemos(MemoCriteriaInfo memoCriteria);
	
	  /**
	   * Retrieve Last Memo  By billing account number (BAN), Memo Type
	   *
	   * @param   int     billing account number (BAN)
	   * @param   String Memo Type
	   *
	   * @returns  Memo Info
	   *
	   */
	MemoInfo retrieveLastMemo(int ban , String memoType);
	
}
