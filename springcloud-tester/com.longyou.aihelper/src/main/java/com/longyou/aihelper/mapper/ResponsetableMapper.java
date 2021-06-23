package com.longyou.aihelper.mapper;

import com.longyou.aihelper.model.Responsetable;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ResponsetableMapper {

  /**
   * delete by primary key
   *
   * @param id primaryKey
   * @return deleteCount
   */
  int deleteByPrimaryKey(Long id);

  /**
   * insert record to table
   *
   * @param record the record
   * @return insert count
   */
  int insert(Responsetable record);

  int insertOrUpdate(Responsetable record);

  int insertOrUpdateSelective(Responsetable record);

  /**
   * insert record to table selective
   *
   * @param record the record
   * @return insert count
   */
  int insertSelective(Responsetable record);

  /**
   * select by primary key
   *
   * @param id primary key
   * @return object by primary key
   */
  Responsetable selectByPrimaryKey(Long id);


  List<Responsetable> selectByCondition(Map<String, Object> params);

  /**
   * update record selective
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKeySelective(Responsetable record);

  /**
   * update record
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKey(Responsetable record);

  int updateBatch(List<Responsetable> list);

  int batchInsert(@Param("list") List<Responsetable> list);
}