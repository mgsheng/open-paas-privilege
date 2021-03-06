package cn.com.open.opensass.privilege.dev;

public class PrivilegeServiceDev {
	private String server_host;
	public String kafka_topic;
	public String kafka_group;
	public String zookeeper_connect;
	public String metadata_broker_list;
	
	public String kong_log_url;
	
	public String getKong_log_url() {
		return kong_log_url;
	}

	public void setKong_log_url(String kong_log_url) {
		this.kong_log_url = kong_log_url;
	}

	public String getZookeeper_connect() {
		return zookeeper_connect;
	}

	public void setZookeeper_connect(String zookeeper_connect) {
		this.zookeeper_connect = zookeeper_connect;
	}

	public String getMetadata_broker_list() {
		return metadata_broker_list;
	}

	public void setMetadata_broker_list(String metadata_broker_list) {
		this.metadata_broker_list = metadata_broker_list;
	}

	public String getKafka_topic() {
		return kafka_topic;
	}

	public void setKafka_topic(String kafka_topic) {
		this.kafka_topic = kafka_topic;
	}
	
	public String getKafka_group() {
		return kafka_group;
	}

	public void setKafka_group(String kafka_group) {
		this.kafka_group = kafka_group;
	}

	public String getServer_host() {
		return server_host;
	}

	public void setServer_host(String server_host) {
		this.server_host = server_host;
	}
	

}
