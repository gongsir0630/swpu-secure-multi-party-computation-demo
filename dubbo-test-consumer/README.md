### 平台流程
1、企业注册：userService.register(username,password)

2、企业登录：userService.login(username,password)

3、获取模型列表：lcService.getAll()

4、申请计算：

- 获取公钥：key=keyService.getPublicKey(username)
- RedisTemplate.opsForValue.set(username,key)
- ApplyData from A and B

5、government admin permit all apply

6、待更...