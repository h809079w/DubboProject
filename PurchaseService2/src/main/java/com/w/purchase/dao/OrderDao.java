package com.w.purchase.dao;



import com.w.common.domain.OrderInfo;
import com.w.common.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderDao {

	@Select("select * from seckill_order where user_id=#{userId} and product_id=#{productId}")
	public SeckillOrder getSeckillOrderByUserIdProductId(@Param("userId") long userId, @Param("productId") long productId);

	@Insert("insert into order_info(user_id, product_id, product_name, product_count, product_price, order_channel, status, create_date)values("
			+ "#{userId}, #{productId}, #{productName}, #{productCount}, #{productPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	public long insertOrderInfo(OrderInfo orderInfo);

	@Insert("insert into seckill_order (user_id, product_id, order_id)values(#{userId}, #{productId}, #{orderId})")
	public int insertSeckillOrder(SeckillOrder seckillOrder);

	@Select("select * from order_info where id = #{orderId}")
	public OrderInfo getOrderInfoByOrderId(@Param("orderId") long orderId);

	@Select("select * from order_info where user_id=#{userId} and product_id=#{productId}")
	public OrderInfo getOrderInfoByUserIdProductId(@Param("userId") long userId, @Param("productId") long productId);


	@Select("select * from order_info where user_id = #{userId}")
	public List<OrderInfo> getOrderInfosByUserId(@Param("userId") long userId);


	@Update("update order_info set status = -1 where user_id = #{userId} and product_id = #{productId}")
	public void CancelOrderInfoByUserIdAndProductId(long userId, long productId);

	@Update("update order_info set status = 1 where id = #{orderId}")
	public long PayOrderInfoByUserIdAndProductId(long orderId);


	@Delete("delete from order_info")
	public void deleteOrderInfos();

	@Delete("delete from seckill_order")
	public void deleteSeckillOrders();


}
