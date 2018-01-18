package schabowsky.com.UtahDABC_Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.demo.utahdabc.datamodel.Product;
import com.solace.demo.utahdabc.datamodel.ProductInventoryData;
import com.solace.demo.utahdabc.datamodel.StoreInventory;

@SpringBootApplication
@Service
public class UtahDABCInventoryProvider implements MessageListener {
	private static final Logger log = LoggerFactory.getLogger(UtahDABCInventoryProvider.class);

	@Autowired
	private JmsTemplate jmsTemplate = null;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(UtahDABCInventoryProvider.class);
	}

	@PostConstruct
	private void fixJMSTemplate() {
		// Code that makes the JMS Template Cache Connections for Performance.
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
		jmsTemplate.setConnectionFactory(ccf);
		jmsTemplate.setPubSubDomain(true);

	}

	@JmsListener(destination = "GetInventoryProductData_Queue")
	public void onMessage(Message msg) {
		ObjectMapper mapper = new ObjectMapper();
		TextMessage txtmsg = (TextMessage) msg;
		Product incomingProduct = null;
		try {
			incomingProduct = mapper.readValue(txtmsg.getText(), Product.class);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Processing CSC: " + incomingProduct.getCsc());
		RestTemplate restTemplate = new RestTemplate();
		String inital = restTemplate.getForObject(
				"https://webapps2.abc.utah.gov/Production/OnlineInventoryQuery/IQ/InventoryQuery.aspx", String.class);

		Document doc = Jsoup.parse(inital);
		String VIEWSTATE = doc.select("input[id$=__VIEWSTATE]").get(0).attributes().get("value");
		String EVENTVALIDATION = doc.select("input[id$=__EVENTVALIDATION]").get(0).attributes().get("value");

		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", "Mozilla");

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("__VIEWSTATE", VIEWSTATE);
		map.add("__EVENTVALIDATION", EVENTVALIDATION);
		map.add("__ASYNCPOST", "true");
		map.add("ctl00$ContentPlaceHolderBody$tbCscCode", Integer.toString(incomingProduct.getCsc()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				"https://webapps2.abc.utah.gov/Production/OnlineInventoryQuery/IQ/InventoryQuery.aspx", request,
				String.class);

		Document responseDoc = Jsoup.parse(response.toString());
		ProductInventoryData prodInv = new ProductInventoryData();
		if (responseDoc.select("#ContentPlaceHolderBody_lblWhsInv").size() > 0) {
			prodInv.setWarehouseInventoryQty(
					Integer.parseInt(responseDoc.select("#ContentPlaceHolderBody_lblWhsInv").get(0).text()));
			prodInv.setWarehouseOnOrderQty(
					Integer.parseInt(responseDoc.select("#ContentPlaceHolderBody_lblWhsOnOrder").get(0).text()));
			prodInv.setProductStatus(responseDoc.select("#ContentPlaceHolderBody_lblStatus").get(0).text());
			prodInv.setProduct(incomingProduct);

			

			Elements table = responseDoc.select("#ContentPlaceHolderBody_gvInventoryDetails");
			Elements tableRows = table.select("tr[class='gridViewRow'");
			for (Element row : tableRows) {
				StoreInventory storeInv = new StoreInventory();
				Elements data = row.select("td");
				storeInv.setStoreID(data.get(0).text());
				storeInv.setStoreName(data.get(1).text());
				storeInv.setProductQty(Integer.parseInt(data.get(2).select("span").get(0).text()));
				storeInv.setStoreAddress(data.get(3).text());
				storeInv.setStoreCity(data.get(4).text());
				storeInv.setStorePhone(data.get(5).text());
				prodInv.setStoreInventory(storeInv);

				String classCode = incomingProduct.getClass_code();
				if (!classCode.isEmpty()) {
					char[] codeArray = classCode.toCharArray();
					String topicname = "inventory/";

					for (int i = 0; i < codeArray.length; i++) {
						topicname = topicname + Character.toString(codeArray[i]);
						if (i < codeArray.length - 1) {
							topicname = topicname + "/";
						}
					}

					// Send to Solace
					try {
						jmsTemplate.convertAndSend(topicname, mapper.writeValueAsString(prodInv));
						log.info("Sent CSC: " + incomingProduct.getCsc() + "to Topic: " + topicname + " with payload: " + mapper.writeValueAsString(prodInv));
					} catch (JmsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
