package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:05
 * Description
 * Notice
 **/
@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询参数{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("要求批量删除的菜品id：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品的启售和停售")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据菜品id获取菜品信息（带口味信息）")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据菜品id获取菜品信息（带口味信息）：{}",id);
        DishVO dishVo = dishService.getByIdWithFlavors(id);
        return Result.success(dishVo);
    }

    /**
     * 根据菜品id修改菜品信息（包含口味信息）
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("根据菜品id修改菜品信息（包含口味信息）")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品的参数：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> getByCategoryId(Long categoryId){
        List<DishVO> dishVOList = dishService.getByCategoryId(categoryId);
        return Result.success(dishVOList);
    }
}
