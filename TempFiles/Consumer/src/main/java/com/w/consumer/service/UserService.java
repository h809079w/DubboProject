package com.w.consumer.service;



import com.w.common.domain.User;
import com.w.consumer.dao.UserDao;
import com.w.consumer.redis.RedisService;
import com.w.consumer.redis.UserKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

//@Service(value = "ConsumerPackage")
@Service
public class UserService {


	public static final String COOKI_NAME_TOKEN = "token";

	@Autowired
	UserDao userDao;

	@Autowired
	RedisService redisService;

	public boolean insertUserToDB(User user){
		User tempUser=userDao.getUserById(user.getId());
		if (tempUser!=null){return false;}
		else {
			userDao.InsertUser(user);
			return  true;
		}
	}

	public User getUserById(long id) {
		//取缓存
		User user = redisService.get(UserKey.getUserByIdKey, ""+id, User.class);
		if(user != null) {
			return user;
		}
		//取数据库
		user = userDao.getUserById(id);
		if(user != null) {
			redisService.set(UserKey.getUserByIdKey, ""+id, user);
		}
		return user;
	}
	// http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
//	public boolean updatePassword(String token, long id, String formPass) {
//		//取user
//		User user = getUserById(id);
//		if(user == null) {
//			throw new BussinessException(CodeMsg.MOBILE_NOT_EXIST);
//		}
//		//更新数据库
//		User toBeUpdate = new User();
//		toBeUpdate.setId(id);
//		toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//		userDao.updateUser(toBeUpdate);
//		//处理缓存
//		redisService.delete(UserKey.getUserByIdKey, ""+id);
//		user.setPassword(toBeUpdate.getPassword());
//		redisService.set(UserKey.tokenKey, token, user);
//		return true;
//	}


	public User getUserByTokenFromRedis(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		User user = redisService.get(UserKey.tokenKey, token, User.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}


//	public String login(HttpServletResponse response, LoginVo loginVo) {
//		if(loginVo == null) {
//			throw new BussinessException(CodeMsg.SERVER_ERROR);
//		}
//		Long id = loginVo.getId();
//		String formPass = loginVo.getPassword();
//		//判断手机号是否存在
//		User User = getUserById(id);
//		if(User == null) {
//			//throw new BussinessException(CodeMsg.MOBILE_NOT_EXIST);
//			User=new User();
//			User.setId(id);
//			String saltDB="123456";
//			User.setSalt(saltDB);
//			String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//			User.setPassword(calcPass);
//			userDao.InsertUser(User);
//
//		}else {
//			//验证密码
//			String dbPass = User.getPassword();
//			String saltDB = User.getSalt();
//			String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//			if (!calcPass.equals(dbPass)) {
//				throw new BussinessException(CodeMsg.PASSWORD_ERROR);
//			}
//		}
//		//生成cookie
//		String token	 = UUIDUtil.uuid();
//		addCookie(response, token, User);
//		return token;
//	}

	private void addCookie(HttpServletResponse response, String token, User User) {
		redisService.set(UserKey.tokenKey, token, User);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.tokenKey.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
