package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.SetmealOverViewVO;

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

    /**
     * 查询套餐总览
     * @return
     */
    SetmealOverViewVO setmealOverView();
}
