package nl.scanazon.scanapp.networking;

import nl.scanazon.scanapp.model.Product;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ProductInfoRequest extends SpringAndroidSpiceRequest<Product> {
	
	private int productid;
	
	public ProductInfoRequest(int productid) {
	    super(Product.class);
	    this.productid = productid;
	 }
	
	@Override
	public Product loadDataFromNetwork() throws Exception{
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.set("product_id", Integer.toString(productid));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
		
		String url = String.format(RequestHelper.HOST_URL + "/getproduct");
		
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		return restTemplate.postForObject(url, request, Product.class);
	}
	
	public String createCacheKey() {
	      return "product." + productid;
	}
}
