package com.phxl.hqcp.dao;

import java.util.Map;

public interface CallProcedureMapper {

	/**
	 * 写入建设科室-机构概要数据
	 * */
	void SP_DEPT_ORG(Map<String, Object> params);

}
