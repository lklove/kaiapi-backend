package com.edkai.kaiapiinterface.service.impl;

import cn.hutool.http.HttpUtil;
import com.edkai.common.ErrorCode;
import com.edkai.common.exception.BusinessException;
import com.edkai.kaiapiinterface.modle.entity.WeatherVo;
import com.edkai.kaiapiinterface.service.WeatherService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lk
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    @Override
    public WeatherVo getWeatherByCity(String city) {
        if (StringUtils.isBlank(city)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String url = "https://www.tianqi.com/%s/";
        String weatherHtml = HttpUtil.get(String.format(url, city));
        if (StringUtils.isBlank(weatherHtml)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"城市错误");
        }
        Document document = Jsoup.parse(weatherHtml);
        WeatherVo weatherVo = new WeatherVo();
        //获取城市
        Element nameElement = document.getElementsByClass("name").get(0);
        Element nameChildren = nameElement.child(0);
        TextNode cityTextNode = (TextNode) nameChildren.childNode(0);
        weatherVo.setCity(cityTextNode.getWholeText());
        // 获取天气，最大气温，最小气温，实时气温
        Elements weatherElements = document.getElementsByClass("weather");
        // 获取温度 如27摄氏度
        Element weatherElement = weatherElements.get(0);
        Elements weatherChildren = weatherElement.children();
        Element nowElement = weatherChildren.get(1);
        Elements nowChildren = nowElement.children();
        StringBuilder nowBuilder = new StringBuilder();
        for (Element child : nowChildren) {
            TextNode textNode = (TextNode) (child.childNodes().get(0));
            nowBuilder.append(textNode);
        }
        weatherVo.setNowTemp(nowBuilder.toString());
        // 一天天气和最高最低温度 多云，22~27摄氏度
        Element weaElement = weatherChildren.get(2);
        // 获取 <span><b>多云</b>22 ~ 27℃</span>
        List<Node> weaChildren = weaElement.childNodes();
        // 得到上述b标签
        Element weaNode = (Element) weaChildren.get(0);
        TextNode weaTextNode = (TextNode) weaNode.childNodes().get(0);
        //获取天气值
        String weatherValue = weaTextNode.getWholeText();
        weatherVo.setWeather(weatherValue);
        // 获取最低和最高气温
        TextNode node = (TextNode) weaChildren.get(1);
        //得到最高最低气温
        String minMaxTemp = node.getWholeText();
        weatherVo.setMinMaxTemp(minMaxTemp);
        //获取湿度，风向，有无紫外线
        Elements humidityElements = document.getElementsByClass("shidu");
        Element humidityElement = humidityElements.get(0);
        Elements humidityChildren = humidityElement.children();
        //获取湿度
        Element humElement = humidityChildren.get(0);
        TextNode humTextNode = (TextNode) humElement.childNodes().get(0);
        weatherVo.setHumidity(humTextNode.getWholeText());
        //获取风向
        Element wDElement = humidityChildren.get(1);
        TextNode wDTextNode = (TextNode) wDElement.childNodes().get(0);
        weatherVo.setWindDire(wDTextNode.getWholeText());
        //获取紫外线
        Element zWElement = humidityChildren.get(2);
        TextNode zWTextNode = (TextNode) zWElement.childNodes().get(0);
        weatherVo.setUltravioletLight(zWTextNode.getWholeText());
        //获取空气质量,PM2.5
        Elements airElements = document.getElementsByClass("kongqi");
        Element airElement = airElements.get(0);
        Elements airChildren = airElement.children();
        //空气质量
        Element airQuaElement = airChildren.get(0);
        TextNode airQuaTextNode = (TextNode) airQuaElement.childNodes().get(0);
        weatherVo.setAirQuality(airQuaTextNode.getWholeText());
        //PM2.5
        Element pMElement = airChildren.get(1);
        TextNode pMTextNode = (TextNode) pMElement.childNodes().get(0);
        weatherVo.setPM(pMTextNode.getWholeText());
        return weatherVo;
    }
}
