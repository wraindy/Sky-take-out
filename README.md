<div align="center">
    <a href="https://github.com/wraindy">
        <img src="./README.assets/Avatar.png" width="360" height="300" style="display: inline-block;"></a>
    <a>&nbsp;|&nbsp;</a>
    <a href="https://github.com/wraindy">
    	<img src="https://github-readme-stats.vercel.app/api?username=wraindy" width="400" height="300" style="display: inline-block;"></a>
</div>
<h1 align="center">Sky-take-out</h1>
<p align="center">
    苍穹外卖-后端源码
    <br />
    <a href="https://github.com/wraindy/Sky-take-out"><strong>可以给我点个star吗^_^</strong></a>
    <br />
    <br />
    <a href="https://github.com/wraindy/Sky-take-out">Fork一下</a>
    ·
    <a href="https://github.com/wraindy/Sky-take-out/issues">报告Bug</a>
    ·
    <a href="https://github.com/wraindy/Sky-take-out/issues">提出新特性</a>
    <hr>
	<p align="center">



<p align="center">
     <a href="https://github.com/wraindy/Calculator">
    	<img src="https://img.shields.io/github/contributors/wraindy/Calculator?style=for-the-badge&logo=GitHub"></a>
	<a href="https://github.com/wraindy/Calculator">
    	<img src="https://img.shields.io/github/stars/wraindy/Calculator?style=for-the-badge&logo=GitHub"></a>
	<a href="https://github.com/wraindy/Calculator">
    	<img src="https://img.shields.io/github/forks/wraindy/Calculator?style=for-the-badge&logo=GitHub"></a>
	<a href="https://github.com/wraindy/Calculator">
    	<img src = "https://img.shields.io/github/issues/wraindy/Calculator?style=for-the-badge&logo=GitHub"></a>
    <a href="https://github.com/wraindy/Calculator">
    	<img src = "https://img.shields.io/github/license/wraindy/Calculator?style=for-the-badge&logo=GitHub"></a>
</p>





# 功能特色Highlight

> 菜品图片从阿里云OSS改用MinIO存储



# 安装运行Installation

## 项目环境

1. JDK1.8（java-se-8u43-ri）
2. Maven（idea自带的即可，3.9.5）

## 启动方法

1. 新建application-dev.yml放入到application.yml相同路径，填写必要的MySQL、Redis、MinIO、微信参数
2. 使用sql文件初始化数据库
3. 将菜品图片上传至MinIO，根据数据库中dish表image字段，修改菜品图片名字
4. 按照黑马的视频要求，修改微信小程序配置即可

必要的文件在build文件夹中。



# 特别注意

## MinIO永久公开访问

将对应bucket的policy改成public

## 微信支付处理

由于sb微信支付至今为止没有沙箱环境（20240512），因此被迫跳过微信支付。

后端有详细的说明，前端微信小程序修改如下。

> 在微信小程序源码的/pages/pay/index.js中，全局搜索`function handleSave()`函数，在`res.code === 1` if条件判断为true的时候，移除调起微信支付的方法，改成打印后端响应的假数据，并直接弹出对话框，表示支付成功
>
> 参考：[界面 / 交互 / wx.showModal (qq.com)](https://developers.weixin.qq.com/miniprogram/dev/api/ui/interaction/wx.showModal.html)

```js
if (res.code === 1) {
            // 注释微信调起支付的函数
            // wx.requestPayment({
            //   nonceStr: res.data.nonceStr,
            //   package: res.data.packageStr,
            //   paySign: res.data.paySign,
            //   timeStamp: res.data.timeStamp,
            //   signType: res.data.signType,
            //   success:function(res){
            //     wx.showModal({
            //       title: '提示',
            //       content: '支付成功',
            //       success:function(){
            //         uni.redirectTo({url: '/pages/success/index?orderId=' + _this.orderId });
            //       }
            //     })
            //     console.log('支付成功!')
            //   }
            // })
            console.log('\n---------------' 
            + '后端payment2方法的假数据：'
            + '\n timeStamp: ' + res.data.timeStamp
            + '\n onceStr: ' + res.data.nonceStr
            + '\n packageStr: ' + res.data.packageStr
            + '\n paySign: ' + res.data.paySign
            + '\n signType: ' + res.data.signType
            + '\n---------------')
            wx.showModal({
              title: '提示',
              content: '支付成功',
              success: function () {
                // 无论用户点击确认还是取消，都是跳转页面
                uni.redirectTo({ url: '/pages/success/index?orderId=' + _this.orderId });
              }
            })

          }
```

## 微信小程序部署

微信小程序获取用户信息的函数在基础调试库2.27及以后失效，因此需要注意基础调试库版本不要过高。

官方公告：[小程序用户头像昵称获取规则调整公告 | 微信开放社区 (qq.com)](https://developers.weixin.qq.com/community/develop/doc/00022c683e8a80b29bed2142b56c01)



------



# 作者Author

本人 [泠雨汐wraindy@Github](https://github.com/wraindy)



# 许可证License

该项目签署了MIT 授权许可，详情请参阅 [LICENSE.txt](https://github.com/wraindy/Sky-take-out/blob/master/LICENSE.txt)



# 鸣谢**Thanks**


- [ChatGPT](https://chat.openai.com/)
- [苍穹外卖-黑马程序员](https://www.bilibili.com/video/BV1TP411v7v6)



