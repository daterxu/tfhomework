## 实现思路：
1.	从medium的https://medium.com/_/graphql 接口抓取数据，原接口返回的数据比较多，重新限制了query返回的字段(主要获取mainurl和clashcount),并限定tagSlug：software-engineering   

2.	原接口数据有分页参数，一次最多25条数据，每次能访问500篇文章，可以使用多线程，每个任务通过快排，找出这篇文章的前10 clashcount的url，然后将这些数据整合再次排序，取得前10篇url  

3.	将前10点赞的url逐个访问，使用google或者baidu的翻译的api进行翻译  

4.	将翻译后的pdf文章打包  

## 完成情况：
1. 	通过medium接口拉取所有文章的mediumUrl和clapCount存到本地txt文件(只拉取到500篇文章，未能获取该主题下的全部文档，还未实现多线程拉取)
   
2. 	读取TXT文件，对clapCount排序获取前十数据的mediumUrl(未实现快排)    

3.	根据mediumUrl可翻译并下载文章(文章可下载，翻译暂未实现）

4.	打包为zip下载。（暂未实现）
