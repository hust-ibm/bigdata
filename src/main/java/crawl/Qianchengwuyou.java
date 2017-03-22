package crawl;


import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;


import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Document;

import com.hust.utils.DB;

/**
 * Crawling news from hfut news
 *
 * @author hu
 */
public class Qianchengwuyou extends BreadthCrawler {

    /**
     * @param crawlPath crawlPath is the path of the directory which maintains
     * information of this crawler
     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
     * links which match regex rules from pag
     */
	
    public Qianchengwuyou(String crawlPath, boolean autoParse) {
    	
        super(crawlPath, autoParse);
        /*种子页面*/
        // 程序的入口，也是第一个爬取的网站;
        this.addSeed("http://www.51job.com/");
        /*正则规则设置*/
        //爬取符合 http://jobs.51job.com/.*/[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9].html.*的网站
        this.addRegex("http://jobs.51job.com/.*/[0-9][0-9]"
        		+ "[0-9][0-9][0-9][0-9][0-9][0-9].html.*");
        /*不要爬取 jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*不要爬取包含 # 的URL*/
        this.addRegex("-.*#.*");
    }

    public void visit(Page page, CrawlDatums next) {
        String url = page.getUrl();
        /*判断是否是目标网页，网页的URL规律可以通过观察得出。并用则正表达式过滤*/
        if (page.matchUrl("http://jobs.51job.com/.*/[0-9][0-9][0-9]"
        		+ "[0-9][0-9][0-9][0-9][0-9].html.*")) {
            /*使用CSS选择器提取信息*/
            String title = page.select("div.cn>h1").text();   //职位名称
            String companyName = page.select("p.cname").text();  //公司名称
            String jobBenefits = page.select("p.t2").text();   //职位福利
            String salary = page.select("div.cn>strong").text();  //薪资
            String jobInformation = page.select("div.job_msg").text();  //职位信息     
            String place = page.select("span.lname").text();  //工作地点
           //格式为：民营公司   |  50-150人   |  计算机软件
            String information = page.select("p.ltype").text();  
            String[] names = information.split("\\|"); //以|为标志分割字符串
            String field = names[0]; //公司领域
            String type = names[1];  //公司性质
            String scale = names[2];// 公司规模
            //sql插入语句
   /*         String sql = "insert into qianchengwuyou(url,title,companyName,"
            		+ "jobBenefits,salary,jobInformation,place,field,type,scale)"
            		+ "values('"+url+"','"+title+"','"+companyName+"','"+jobBenefits+"'"
            				+ ",'"+salary+"','"+jobInformation+"','"+place+"','"+field+"',"
            						+ "'"+type+"','"+scale+"')";  */
            String sql = "insert into fenlei(title,content)values('"+title+"','"+jobInformation+"')";
			DB db = new DB();
			db.open(sql);
			db.close();
        }
    }
    
    
    public static void main(String[] args) throws Exception {
     
     /*如果autoParse是true(构造函数的第二个参数)，爬虫会自动抽取网页中符合正则规则的URL，
           作为后续任务，当然，爬虫会去掉重复的URL，不会爬取历史中爬取过的URL。
       autoParse为true即开启自动解析机制*/
    	Qianchengwuyou crawler = new Qianchengwuyou("crawl", true);
        /*线程数*/
        crawler.setThreads(500);
        /*设置每次迭代中爬取数量的上限*/
        crawler.setTopN(50000);
        
        /*可以设置每个线程visit的间隔，这里是毫秒。主要用于具有反爬虫机制的网站*/
      // crawler.setExecuteInterval(10000);
         
        /*设置是否为断点爬取，如果设置为false，任务启动前会清空历史数据。
           如果设置为true，会在已有crawlPath(构造函数的第一个参数)的基础上继
           续爬取。对于耗时较长的任务，很可能需要中途中断爬虫，也有可能遇到
           死机、断电等异常情况，使用断点爬取模式，可以保证爬虫不受这些因素
           的影响，爬虫可以在人为中断、死机、断电等情况出现后，继续以前的任务
           进行爬取。断点爬取默认为false*/
    //    crawler.setResumable(true);
        /*开始深度为4的爬取，这里深度和网站的拓扑结构没有任何关系
            可以将深度理解为迭代次数，往往迭代次数越多，爬取的数据越多*/
        crawler.start(200);
    }

}