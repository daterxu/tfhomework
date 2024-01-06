package com.demo.medium.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.medium.client.MediumClient;
import com.demo.medium.model.Item;
import com.demo.medium.model.Post;
import com.demo.medium.model.Recommend;
import com.demo.medium.service.MediumService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Service
public class MediumServiceImpl implements MediumService {

    @Resource
    private MediumClient mediumClient;

    @Override
    public void getData() {
        for (int num = 0; num <= 480; num = num + 20) {
            log.info("进度: num = [{}]", num);
            JSONObject jsonObject = mediumClient.getData(num);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject tagFromSlug = data.getJSONObject("tagFromSlug");
            JSONObject viewerEdge = tagFromSlug.getJSONObject("viewerEdge");
            JSONObject recommendedPostsFeed = viewerEdge.getJSONObject("recommendedPostsFeed");
            Recommend recommended = JSONObject.parseObject(String.valueOf(recommendedPostsFeed), Recommend.class);
            List<Item> list = recommended.getItems();
            for (Item item : list) {
                writeData(item.getPost());
            }
        }
    }

    @Override
    public void getArticle() {
        // 定义存放结果的Map
        Map<String, Integer> map = readData();
        //排序
        List<String> title = sort(map);
        //下载文章为pdf
        title.forEach(url -> {
            download(url);
        });
        //TODO 翻译
        //TODO 打包zip下载
    }

    private List<String> sort(Map<String, Integer> map) {
        // 根据Value进行降序排序
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(map.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        List<String> title = new ArrayList<>();
        // 输出排序后的结果
        for (int i = 0; i < Math.min(sortedList.size(), 10); i++) {
            Map.Entry<String, Integer> maps = sortedList.get(i);
            title.add(maps.getKey());
        }
        return title;
    }

    private void download(String url) {
        try {
            HttpHost myProxy = new HttpHost("127.0.0.1", 7890);
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
            clientBuilder.setProxy(myProxy).disableCookieManagement();
            HttpClient httpClient = clientBuilder.build();
            // 获取网页内容
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            byte[] content = EntityUtils.toByteArray(httpEntity);

            // 创建PDF文档
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("webpage.pdf"));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(content), null, Charset.forName("UTF-8"));
            // 关闭PDF文档
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private Map readData() {
        // 定义存放结果的Map
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./output.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // 按空格分隔字符串
                String[] parts = line.split(" ");

                if (parts.length >= 2) {
                    String key = parts[0];
                    Integer value = Integer.valueOf(parts[1]);
                    // 添加到Map中
                    map.put(key, value);
                } else {
                    log.info("Invalid input format");
                }
            }
        } catch (IOException e) {
            log.error("IOException:", e);
        }
        return map;
    }

    private void writeData(Post myPost) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("./output.txt", true))) {
            out.println(myPost.getMediumUrl() + " " + myPost.getClapCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
