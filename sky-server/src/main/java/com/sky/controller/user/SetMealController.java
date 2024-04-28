package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Wraindy
 * @DateTime 2024/04/28 08:35
 * Description
 * Notice
 **/
@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<DishVO>> queryByCategoryId(Long categoryId){
        List<DishVO> list = setMealService.getByCategoryId(categoryId);
        return Result.success(list);
    }


    @GetMapping("/dish/{setMealId}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result<List<DishItemVO>> getDishBySetMealId(@PathVariable Long setMealId){
        List<DishItemVO> list = dishService.queryDishItem(setMealId);
        return Result.success(list);
    }
}
