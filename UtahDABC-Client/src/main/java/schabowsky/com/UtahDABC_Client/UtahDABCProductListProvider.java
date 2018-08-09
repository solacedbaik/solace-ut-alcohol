package schabowsky.com.UtahDABC_Client;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.demo.utahdabc.datamodel.Product;

@SpringBootApplication
@EnableScheduling
public class UtahDABCProductListProvider {

	private static final Logger log = LoggerFactory.getLogger(UtahDABCProductListProvider.class);

	@Autowired
	private JmsTemplate jmsTemplate = null;

	public static void main(String args[]) {
		SpringApplication.run(UtahDABCProductListProvider.class);
	}
	
    @PostConstruct 
    private void fixJMSTemplate()
    {
		// Code that makes the JMS Template Cache Connections for Performance.
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
		jmsTemplate.setConnectionFactory(ccf);
		jmsTemplate.setPubSubDomain(true);
    }
    
    //Run every 24 hours
	@Scheduled(cron="0 0 5 * * ?", zone = "America/Denver")
    //@Scheduled(fixedRate=86400000)
	public void sendPriceList() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		log.info("Starting: Request to Utah DABC For Product List");
		try {
			List<Product> dabcProductList = getPriceList();
			log.info("Starting to Publish All Products in Utah DABC List");
			for (Product p : dabcProductList) {
				String classCode = p.getClass_code();
				if (!classCode.isEmpty()) {
					char[] codeArray = classCode.toCharArray();
					String topicname = "product/";

					for (int i = 0; i < codeArray.length; i++) {
						topicname = topicname + Character.toString(codeArray[i]);
						if (i < codeArray.length - 1) {
							topicname = topicname + "/";
						}
					}
					log.info("Sending message to: " + topicname );
					// Send to Solace
					jmsTemplate.convertAndSend(topicname, mapper.writeValueAsString(p));
				} else {
					log.error("Problem Creating topic name for Item: " + p.getName() + " and CSC Code: " + p.getCsc());
				}
			}
			log.info("Completed Publishing Products");
		} catch (Exception e) {
			log.error("Problem Parsing or Accessing Price list");
			throw e;
		}
		log.info("Run ended: Now sleeping per the scheduled timer");
	}

	private List<Product> getPriceList()
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		List<Product> productList = new ArrayList<Product>();
		RestTemplate restTemplate = new RestTemplate();
		String quote = restTemplate.getForObject(
				"https://webapps2.abc.utah.gov/Production/OnlinePriceList/DisplayPriceList.aspx", String.class);
		Document doc = Jsoup.parse(quote);
		Elements tables = doc.select("table");
		for (Element element : tables) {
			Elements trs = element.select("tr");
			for (Element tr : trs) {
				Product p = new Product();
				Elements tds = tr.select("td");
				if (tds.size() != 0) {
					p.setName(tds.get(0).text());
					p.setDiv_code(tds.get(1).text());
					p.setDept_code(tds.get(2).text());
					p.setClass_code(tds.get(3).text());
					p.setSize(Integer.parseInt(tds.get(4).text()));
					p.setCsc(Integer.parseInt(tds.get(5).text()));
					p.setPrice(NumberFormat.getCurrencyInstance(Locale.US).parse(tds.get(6).text()).doubleValue());
					p.setSPA(tds.get(7).text());
					productList.add(p);
				}
			}
		}
		return productList;
	}
}
