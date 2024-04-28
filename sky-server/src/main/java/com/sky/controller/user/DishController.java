package com.sky.controller.user;

/**
 * @Author Wraindy
 * @DateTime 2024/04/28 08:29
 * Description
 * Notice
 **/

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@Api(tags = "菜品相关接口")
@Slf4j
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> getByCategoryId(Long categoryId){

        // 先查询redis，查看是否有缓存
        String key = RedisConstant.CATEGORY_CACHE + categoryId.toString();
        List<DishVO> list = (List<DishVO>)redisTemplate.opsForValue().get(key);
        if(list != null && !list.isEmpty()){
            // 若有缓存，则直接返回
            return Result.success(list);
        }
        // 若无，查询数据库
        List<DishVO> dishVOList = dishService.getByCategoryIdWithFlavors(categoryId);
        redisTemplate.opsForValue().set(key, dishVOList);
        return Result.success(dishVOList);
    }
}
