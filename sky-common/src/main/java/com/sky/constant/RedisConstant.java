package com.sky.constant;

/**
 * @Author Wraindy
 * @DateTime 2024/04/27 12:47
 * Description
 * Notice
 **/

public class RedisConstant {

    // 店铺营业状态
    public static final String SHOP_STATUS = "SHOP_STATUS";

    // 根据分类id查询的菜品缓存
    public static final String CATEGORY_CACHE = "CATEGORY:";
    // 分类包含菜品（Dish）和套餐（SetMeal）
    // 为区分套餐的缓存管理，菜品的在Redis中的key使用<DishCache::>
    // 而套餐管理的key使用<SetMealCache::>
}
