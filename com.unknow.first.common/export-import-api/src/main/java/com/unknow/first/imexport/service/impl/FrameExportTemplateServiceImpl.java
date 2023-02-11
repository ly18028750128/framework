package com.unknow.first.imexport.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.mapper.FrameExportTemplateMapper;
import com.unknow.first.imexport.service.FrameExportTemplateService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【t_frame_export_template(导出模板表)】的数据库操作Service实现
 * @createDate 2023-02-08 14:52:06
 */
@Service
public class FrameExportTemplateServiceImpl extends ServiceImpl<FrameExportTemplateMapper, FrameExportTemplate> implements FrameExportTemplateService {

    @Override
    public FrameExportTemplate getTemplateByCode(String templateCode) throws Exception {
        return this.getBaseMapper().selectOne(Wrappers.<FrameExportTemplate>lambdaQuery().eq(FrameExportTemplate::getTemplateCode, templateCode));
    }
}




