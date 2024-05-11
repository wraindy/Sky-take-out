package com.sky.service;

import com.sky.vo.BusinessDataVO;

/**
 * @Author Wraindy
 * @DateTime 2024/05/11 15:17
 * Description
 * Notice
 **/

public interface WorkspaceService {
    /**
     * 查询今日运营数据
     * @return
     */
    BusinessDataVO getBusinessData();
}
