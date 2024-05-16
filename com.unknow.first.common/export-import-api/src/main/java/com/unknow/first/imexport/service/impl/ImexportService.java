package com.unknow.first.imexport.service.impl;

import com.alibaba.fastjson.JSON;
import com.unknow.first.imexport.constant.ImexportConstants;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.dto.ImportExportTaskCreateDTO;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import com.unknow.first.mongo.utils.MongoDBUtil;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.dimension.utils.DataDimensionUtil;
import org.cloud.entity.LoginUserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static com.unknow.first.mongo.vo.MongoDBEnum.metadataFileAuthRangePersonal;

@Service
public class ImexportService {
    @Autowired
    FrameImportExportTaskService importExportTaskService;

    public FrameImportExportTask create(ImportExportTaskCreateDTO exportTaskCreateDTO, ImexportConstants.TaskType taskType,
                                        MultipartFile file) throws IOException {
        Assert.isTrue(StringUtils.hasLength(exportTaskCreateDTO.getBelongMicroservice()), "system.error.imexport.task.belongService.notEmpty");

        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        FrameImportExportTask importExportTaskCreate = new FrameImportExportTask();
        String fileName = null;
        if (ImexportConstants.TaskType.IMPORT.value == taskType.value) {
            Assert.notNull(file, "system.error.import.file.notEmpty");
            ObjectId fileId = MongoDBUtil.single().storeFile(userDetails, metadataFileAuthRangePersonal.value(), file);
            importExportTaskCreate.setFileId(fileId.toString());

            fileName = String.format("%s-%s-%d-%s-%s", "IMPORT", exportTaskCreateDTO.getTaskName(), userDetails.getId(), userDetails.getUsername(),
                    file.getOriginalFilename());
        }
        BeanUtils.copyProperties(exportTaskCreateDTO, importExportTaskCreate);
        if (ImexportConstants.TaskType.EXPORT.value == taskType.value) {
            fileName = String.format("%s-%s-%d-%s-%s.%s", "EXPORT", exportTaskCreateDTO.getTaskName(), userDetails.getId(), userDetails.getUsername(),
                    CoreConstant.DateTimeFormat.FULLDATETIME_NO_SPLIT.getDateFormat().format(new Date()), exportTaskCreateDTO.getExtension());
        }
        importExportTaskCreate.setFileName(fileName);
        importExportTaskCreate.setTaskType(taskType.value);
        Map<String, Set<String>> dataDimension = DataDimensionUtil.single().getCurrentUserAllDataDimension();
        if (dataDimension != null) {
            importExportTaskCreate.setDataDimension(JSON.toJSONString(dataDimension));
        }
        if (importExportTaskService.save(importExportTaskCreate)) {
            return importExportTaskService.getById(importExportTaskCreate.getTaskId());
        }
        return null;
    }
}
