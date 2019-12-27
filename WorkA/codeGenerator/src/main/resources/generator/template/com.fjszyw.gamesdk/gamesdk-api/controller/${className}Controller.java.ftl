<#include "/custom.include">
<#include "/java_copyright.include">
<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package com.fjszyw.gamesdk.${classNameLowerCase}.controller;

import com.bst.gamesdk.common.utils.BeanUtil;
import com.bst.gamesdk.common.utils.BindingResultToString;
import com.bst.gamesdk.common.utils.CodeUtil;
import com.bst.gamesdk.controller.BaseController;
import com.bst.gamesdk.model.entity.Game;
import com.bst.gamesdk.model.entity.${className};
import com.bst.gamesdk.service.I${className}Service;
import com.bst.gamesdk.service.IGameService;
import com.fjszyw.gamesdk.${classNameLowerCase}.dto.${className}Response;
import com.fjszyw.gamesdk.${classNameLowerCase}.dto.${className}Vo;
import com.fjszyw.gamesdk.${classNameLowerCase}.dto.Get${className}Form;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cedoo
 */
@RestController
@RequestMapping(value = "/${classNameLowerCase}")
@Api(value = "<@tbAlias />", tags = "获取<@tbAlias />")
public class ${className}Controller extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(${className}Controller.class);

    @Autowired
    private I${className}Service ${classNameFirstLower}Service;

    @Autowired
    private IGameService gameService;

    @GetMapping("/getList")
    @ApiOperation(value = "获取<@tbAlias />", notes = "获取<@tbAlias />")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求处理完成", response = ${className}Response.class)
    })
    public String getMsg(@ModelAttribute @Valid Get${className}Form queryForm, BindingResult bindingResult){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if(bindingResult.hasErrors()){
                String errorInfo = BindingResultToString.errorInfo(bindingResult);
                CodeUtil.throwExcep(errorInfo);
            }

            String gameCode = queryForm.getGameCode();
            Game game = gameService.selectByParam(gameCode);
            if(null == game) {
                CodeUtil.throwExcep("游戏不存在");
            }

            Map<String, Object> queryParams = new HashMap<>();
            BeanUtil.putObjToMap(queryParams, queryForm);

            //TODO ${classNameFirstLower}Service.query${className}(gameCode);
            List<${className}> ${classNameFirstLower}s = new ArrayList<>();

            this.setupSuccessMap(resultMap, "获取<@tbAlias />成功");
            ${className}Response ${classNameFirstLower}Response = new ${className}Response();
            List<${className}Vo> ${classNameFirstLower}Vos = new ArrayList<>();
            for(${className} ${classNameFirstLower}: ${classNameFirstLower}s) {
                ${className}Vo ${classNameFirstLower}Vo = new ${className}Vo(${classNameFirstLower});
                ${classNameFirstLower}Vos.add(${classNameFirstLower}Vo);
            }
            ${classNameFirstLower}Response.set${className}(${classNameFirstLower}Vos);
            BeanUtil.putObjToMap(resultMap, ${classNameFirstLower}Response);
        } catch(Exception ex) {
            this.setupFailureMap(resultMap, "获取游戏公告异常");
        }
        return printResultMap(resultMap);
    }

}
