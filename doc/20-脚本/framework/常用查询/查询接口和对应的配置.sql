select *from t_frame_data_interface;
select *from t_frame_data_sql_config;


	select * from t_frame_user where 0=0
	<if test="userName!=null">
	and ( user_Name = #{userName})
	</if>
	<if test="list!=null">
	and user_Name in
	<foreach collection="list" item="id" open="(" separator="," close=")">
			#{id,jdbcType=VARCHAR}
		</foreach>
	</if>

union all
	select * from t_frame_user where 0=0
	<if test="userName!=null">
	and  ( user_Name = #{userName})
	</if>
	<if test="list!=null">
	and user_Name in
	<foreach collection="list" item="id" open="(" separator="," close=")">
			#{id,jdbcType=VARCHAR}
		</foreach>
	</if>
union all
	select * from t_frame_user where 0=0
	<if test="userName!=null">
	and   ( user_Name = #{userName})
	</if>
	<if test="list!=null">
	and user_Name in
	<foreach collection="list" item="id" open="(" separator="," close=")">
			#{id,jdbcType=VARCHAR}
		</foreach>
	</if>
order by user_Name

select *from t_frame_user