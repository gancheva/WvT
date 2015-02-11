package nl.scanazon.scanapp.networking;

import nl.scanazon.scanapp.model.Message;
import nl.scanazon.scanapp.model.ShoppingList;
import nl.scanazon.scanapp.model.UpdatedShoppingList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class UpdateShoppingListRequest extends SpringAndroidSpiceRequest<Message> {

	private ShoppingList updatedList;
	private String email;
	private String password;
	
	public UpdateShoppingListRequest(ShoppingList updatedList, String email, String password) {
		super(Message.class);
		this.updatedList = updatedList;
		this.email = email;
		this.password = password;
	}
	
	@Override
	public Message loadDataFromNetwork() throws Exception{
		
		UpdatedShoppingList updatedShoppingList = new UpdatedShoppingList();
		updatedShoppingList.setEmail(email);
		updatedShoppingList.setPassword(password);
		updatedShoppingList.setBoodschappenlijst_id(updatedList.getBoodschappenlijst_id());
		updatedShoppingList.setBoodschappennaam(updatedList.getBoodschappennaam());
		updatedShoppingList.setProducten(updatedList.getProducten());
		
		ObjectMapper objMapper = new ObjectMapper();
		String jsonString = objMapper.writeValueAsString(updatedShoppingList);
		jsonString = StringEscapeUtils.escapeHtml4(jsonString);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = String.format(RequestHelper.HOST_URL + "/update");
		
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		return restTemplate.postForObject(url, jsonString, Message.class);
	}
	
	public String createCacheKey() {
	      return "updatelist." + email;
	}

}
