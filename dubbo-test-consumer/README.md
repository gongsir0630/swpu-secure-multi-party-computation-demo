### 平台使用流程
1、企业注册：userService.register(username,password)

2、企业登录：userService.login(username,password)

3、获取模型列表：lcService.getListByUser(name,pageNum,pageSize)

4、申请计算：

- 获取公钥：key=keyService.getPublicKey(username)
- RedisTemplate.opsForValue.set(username,key)
- ApplyData from A and B

5、government admin permit all apply

6、申请通过，从政府取得数据A、B

7、开始计算，返回计算过程！

8、待更...