package com.unknow.first.imexport.service;

import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_frame_export_template(导出模板表)】的数据库操作Service
* @createDate 2023-02-08 14:52:06
*/
public interface FrameExportTemplateService extends IService<FrameExportTemplate> {
    FrameExportTemplate getTemplateByCode(String templateCode) throws Exception;
}
