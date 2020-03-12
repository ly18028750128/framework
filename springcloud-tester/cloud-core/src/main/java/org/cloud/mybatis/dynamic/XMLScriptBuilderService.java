package org.cloud.mybatis.dynamic;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class XMLScriptBuilderService 
{
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    public XMLScriptBuilder getXMLScriptBuilder(String sqlContext,String root, Class<?> parameterType)
    {
        XPathParser xpathParser = new XPathParser(sqlContext);
        XNode context = xpathParser.evalNode(root);
        return new XMLScriptBuilder(sqlSessionFactory.getConfiguration(),context, parameterType);
    }
}
