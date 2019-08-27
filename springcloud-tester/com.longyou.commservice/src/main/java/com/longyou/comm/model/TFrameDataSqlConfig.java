package com.longyou.comm.model;

/**
 * Table: t_frame_data_sql_config
 */
public class TFrameDataSqlConfig {
    /**
     * Column: data_code
     * Remark: 数据编号
     */
    private String dataCode;

    /**
     * Column: id
     */
    private Long id;

    /**
     * Column: data_source
     * Remark: 数据源
     */
    private String dataSource;

    /**
     * Column: database_type
     * Remark: MYSQL/ORACLE
     */
    private String databaseType;

    /**
     * Column: data_dao_name
     * Remark: 数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper
     */
    private String dataDaoName;

    /**
     * Column: sql_content
     * Remark: SQL内容
     */
    private String sqlContent;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode == null ? null : dataCode.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource == null ? null : dataSource.trim();
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType == null ? null : databaseType.trim();
    }

    public String getDataDaoName() {
        return dataDaoName;
    }

    public void setDataDaoName(String dataDaoName) {
        this.dataDaoName = dataDaoName == null ? null : dataDaoName.trim();
    }

    public String getSqlContent() {
        return sqlContent;
    }

    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent == null ? null : sqlContent.trim();
    }
}