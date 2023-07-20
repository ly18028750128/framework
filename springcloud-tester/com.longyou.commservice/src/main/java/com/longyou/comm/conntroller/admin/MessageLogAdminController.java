package com.longyou.comm.conntroller.admin;

import static com.longyou.comm.CommonMenuConst.MENU_MESSAGE_LOG_MANAGER;
import static com.longyou.comm.CommonMenuConst.MENU_SYSTEM_MANAGER;

import com.github.pagehelper.PageInfo;
import com.longyou.comm.dto.MessageLogQueryDTO;
import com.unknow.first.mongo.dto.MongoQueryParamsDTO;
import com.unknow.first.mongo.param.MongoPagedParam;
import com.unknow.first.mongo.utils.MongoDBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.MessageLogVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "管理员:消息日志管理", value = "管理员:消息日志管理")
@RequestMapping("/admin/message/logs")
@SystemResource(path = "/admin/message/logs", parentMenuCode = MENU_SYSTEM_MANAGER, parentMenuName = "系统管理")
@Slf4j
public class MessageLogAdminController {

    @SystemResource(value = "/config/list", description = "管理员查询消息日志", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, menuName = "消息日志管理", menuCode = MENU_MESSAGE_LOG_MANAGER)
    @GetMapping
    @ApiOperation("管理员查询消息日志")
    public CommonApiResult<PageInfo<MessageLogVO>> list(MessageLogQueryDTO messageLogQueryDTO, MongoPagedParam mongoPagedParam) throws Exception {
        MongoQueryParamsDTO mongoQueryParamsDTO = MongoDBUtil.single().buildQueryParamsDTO(messageLogQueryDTO, mongoPagedParam);
        PageInfo<MessageLogVO> result = MongoDBUtil.single()
            .paged(mongoPagedParam.getPage().longValue(), mongoPagedParam.getLimit().longValue(), mongoQueryParamsDTO, MessageLogVO.class, "message_logs");
        return CommonApiResult.createSuccessResult(result);
    }


}
