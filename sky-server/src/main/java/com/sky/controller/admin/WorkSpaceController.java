package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Wraindy
 * @DateTime 2024/05/11 15:12
 * Description
 * Notice
 **/
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO vo = workspaceService.getBusinessData();
        return Result.success(vo);
    }
}
