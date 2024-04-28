package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/26 14:31
 * Description
 * Notice   对应用户端的套餐查询做缓存管理（SpringCache）
 * 修改、启售停售、删除都清空全部缓存；新增则清空对应的一个缓存
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @GetMapping("page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询参数：{}", setmealPageQueryDTO);
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐信息")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("根据id查询套餐信息：{}", id);
        SetmealVO setmealVO = setMealService.getById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改套餐信息（带有菜品数据）")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐信息（带有菜品数据）：{}", setmealDTO);
        setMealService.updateWithDishes(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐的启售和停售")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        setMealService.startOrStop(id, status);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("套餐批量删除")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("要求批量删除的套餐id：{}", ids);
        setMealService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增套餐（带有菜品数据）")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐（带有菜品数据）：{}", setmealDTO);
        setMealService.saveWithDishes(setmealDTO);
        return Result.success();
    }
}
