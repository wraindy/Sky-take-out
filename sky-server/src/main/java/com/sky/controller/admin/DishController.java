package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author Wraindy
 * @DateTime 2024/04/23 20:05
 * Description
 * Notice   对应用户端的菜品查询做缓存管理（Redis）
 * 修改、启售停售、删除都清空全部缓存；新增则清空对应的一个缓存
 **/
@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);

        // 清理Redis缓存（删除包含了该菜品的分类即可）
        String key = RedisConstant.CATEGORY_CACHE + dishDTO.getCategoryId().toString();
        clearCache(key, false);

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

        // 批量删除菜品有可能影响多个分类，因此简单起见，全部删除
        clearCache(RedisConstant.CATEGORY_CACHE, true);

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品的启售和停售")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status, id);

        // 只影响一个分类，但是还是要全部清空（可能可以有优化）
        clearCache(RedisConstant.CATEGORY_CACHE, true);

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

        // 如果修改了菜品的分类，将影响两个分类（菜品修改前后），简单起见，清空所有分类缓存
        clearCache(RedisConstant.CATEGORY_CACHE, true);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> getByCategoryId(Long categoryId){
        List<DishVO> dishVOList = dishService.getByCategoryId(categoryId);
        return Result.success(dishVOList);
    }

    /**
     * 清除Redis缓存
     * 输入key全称，删除该key；输入key前缀，删除所有以该前缀的key
     * @param pattern
     * @param clearAll 如果为真，则清空以pattern为前缀的key,否则只精准清空pattern的key
     */
    private void clearCache(String pattern, boolean clearAll){
        String _key = clearAll ? pattern + "*" : pattern;
        Set keys = redisTemplate.keys(_key);
        redisTemplate.delete(keys);
    }
}
