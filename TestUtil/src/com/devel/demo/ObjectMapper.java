import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URL;

public class MonitoringService {
  public void solrRequest() {
		
		List<MediaModel> medias = mediaMapper.selectAllOnlineMedia();
		
		URL url = null;
		try {
			StringBuilder link = new StringBuilder();
			link.append(ConstantBundle.SOLR_MONITORING_DOMAIN)
				.append(ConstantBundle.SOLR_STAT_INFO_CORE)
				.append(ConstantBundle.SOLR_QUERY_SELECT_CONTEXT);
			url = new URL(link.toString());
			
			String[] fields = ConstantBundle.SOLR_STAT_COLLECTION_FIELDS;
			/*
			 * error_log: article_date
			 * collection: stat_date
			 * avg: stat2_std_date 
			 */
			
			String resp = HttpConnUtil.HttpsConn(url, makeParameterSet(fields, "stat_date:20251120"), true);
			
			try {
				// 제네릭 클래스로 읽고 싶다면 반드시 new TypeReference<T>(){} 를 이용하자 
				SolrResponseRootModel<MonitoringStatCollectionModel> transModel 
									= new ObjectMapper().readValue(resp.getBytes(), new TypeReference<SolrResponseRootModel<MonitoringStatCollectionModel>>(){});
				Log.logger.info("");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

  public class SolrResponseRootModel<T> {
  	Map<String, Object> responseHeader;
  	SolrResponseBodyModel<T> response;
  }
  
  
  public class SolrResponseBodyModel<T> {
  	
  	private long numFound;
  	private long start;
  	private boolean numFoundExact;
  	private List<T> docs;
  	
  }
  
  public class MonitoringStatCollectionModel {
  
  	private String stat_key;
  	private int stat_md_oid;
  	private String stat_md_name;
  	private String stat_count;
  	private String stat_flag;
  	private String stat_date;
  	private String stat_dayweek;
  	private String article_serial_id;
  	
  }
}



