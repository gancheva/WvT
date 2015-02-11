package nl.scanazon.scanapp.networking;

import nl.scanazon.scanapp.model.Message;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class LoginRequest extends SpringAndroidSpiceRequest<Message> {
	
	private String email;
	private String password;
	
		public LoginRequest(String email, String password) {
		    super(Message.class);
		    this.email = email;
		    this.password = password;
		}

	  @Override
	  public Message loadDataFromNetwork() throws Exception {
		  
		  	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			parameters.set("e-mail", email);
			parameters.set("password", password);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
			
			String url = String.format(RequestHelper.HOST_URL + "/login");
			
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

			return restTemplate.postForObject(url, request, Message.class);
	  }
	  
	  public String createCacheKey() {
	      return "login." + email;
	  }
}
