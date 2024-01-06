package com.demo.medium.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class MediumClient {

    private static final String URL = "https://medium.com/_/graphql";

    @Resource
    private RestTemplate restTemplate;

    public JSONObject getData(int from) {
        JSONObject response = getResponse(from);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Cookie", "nonce=v2HCNJDN; _gid=GA1.2.856012502.1704458921; lightstep_guid/medium-web=e6de8352c709f921; lightstep_session_id=c813bdd2b9b3876c; sz=1903; pr=1; tz=-480; sid=1:OcHwNxadG/5awQnUCoTW80F3xNb9PlsciR0+FSX5JyIAJmKikrw3UPcJkVSu+Prs; xsrf=ahaWhcAyxVHCRjt7; uid=f8abc77904af; _ga=GA1.1.1579387468.1704458921; _ga_7JY7T788PK=GS1.1.1704509381.4.0.1704509381.0.0.0; dd_cookie_test_2ff8fc81-a4ef-43e6-bfc6-48e4b6fc8418=test; _dd_s=rum=0&expire=1704510282582");
        HttpEntity httpEntity = new HttpEntity(response, httpHeaders);
        JSONObject result = restTemplate.postForObject(URL, httpEntity, JSONObject.class);
        return result;
    }

    private JSONObject getResponse(int from) {
        JSONObject response = new JSONObject();
        JSONObject paging = new JSONObject();
        paging.put("limit", 20);
        paging.put("from", String.valueOf(from));
        paging.put("to", String.valueOf(from + 20));
        JSONObject variables = new JSONObject();
        variables.put("paging", paging);
        variables.put("tagSlug", "software-engineering");
        response.put("operationName", "TagRecommendedFeedQuery");
        response.put("variables", variables);
        response.put("query", query);
        return response;
    }

    private static final String query = "query TagRecommendedFeedQuery($tagSlug: String!, $paging: PagingOptions) {\n  tagFromSlug(tagSlug: $tagSlug) {\n    id\n    viewerEdge {\n      id\n      recommendedPostsFeed(paging: $paging) {\n        items {\n          feedId\n          reason\n          moduleSourceEncoding\n          post {\n            ...StreamPostPreview_post\n            __typename\n          }\n          __typename\n        }\n        pagingInfo {\n          next {\n            from\n            limit\n            source\n            to\n            __typename\n          }\n          __typename\n        }\n        __typename\n      }\n      __typename\n    }\n    __typename\n  }\n}\n\nfragment StreamPostPreview_post on Post {\n  id\n  ...StreamPostPreviewContent_post\n    __typename\n}\n\nfragment StreamPostPreviewContent_post on Post {\n  id\n  title\n        ...PostPreviewFooter_post\n     __typename\n}\n\nfragment PostPreviewFooter_post on Post {\n    ...PostPreviewFooterMenu_post\n  __typename\n  id\n}\n\nfragment MultiVoteCount_post on Post {\n  id\n  __typename\n}\n\nfragment PostPreviewFooterMenu_post on Post {\n    collection {\n    __typename\n    id\n  }\n    ...ExpandablePostCardOverflowButton_post\n  __typename\n  id\n}\n\nfragment ExpandablePostCardOverflowButton_post on Post {\n    ...ExpandablePostCardReaderButton_post\n  __typename\n  id\n}\n\nfragment ExpandablePostCardReaderButton_post on Post {\n  id\n  collection {\n    id\n    __typename\n  }\n  mediumUrl\n  clapCount\n  ...ClapMutation_post\n  __typename\n}\n\nfragment ClapMutation_post on Post {\n  __typename\n  id\n  clapCount\n  ...MultiVoteCount_post\n}\n\n";
}
