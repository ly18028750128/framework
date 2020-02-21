DELIMITER $$
DROP PROCEDURE IF EXISTS `framework`.`sp_test` $$  
CREATE
    /*[DEFINER = { user | CURRENT_USER }]*/
    PROCEDURE `framework`.`sp_test`(IN v_user_name VARCHAR(2000),IN v_page INTEGER  )
    /*LANGUAGE SQL
    | [NOT] DETERMINISTIC
    | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
    | SQL SECURITY { DEFINER | INVOKER }
    | COMMENT 'string'*/
    BEGIN

	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	

IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page AS page,10 AS page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	


    END$$

DELIMITER ;