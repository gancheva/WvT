package nl.scanazon.counterapp.networking;

import nl.scanazon.counterapp.model.Message;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class DeleteListRequest extends SpringAndroidSpiceRequest<Message> {

	private String listid;
	public DeleteListRequest(String listid) {
	    super(Message.class);
	    this.listid = listid;
	}

	  @Override
	  public Message loadDataFromNetwork() throws Exception {
		  
		  	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			parameters.set("shoppinglist_id", listid);
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
			
			String url = String.format(RequestHelper.HOST_URL + "/delete");
			
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	
			return restTemplate.postForObject(url, request, Message.class);
	  }
	  
	  public String createCacheKey() {
	      return "delete." + listid;
	  }
}
